package xyz.goome.eternal.dbserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.goome.eternal.common.entity.ServerAddr;

import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by matrix on 16/4/10.
 */
public class ThriftServer {

    private static final Logger logger = LoggerFactory.getLogger(ThriftServer.class);
    private static Gson gson = new GsonBuilder().create();

    private static final int ZK_TIME_OUT = 12000;
    private static final String ZK_HOST = "127.0.0.1:2181";

    private int port;
    private Map<String, Object> serviceList;
    private TServerSocket serverTransport;

    public void init() {
        try {
            serverTransport = new TServerSocket(port);
            TMultiplexedProcessor mprocessor = new TMultiplexedProcessor();
            for(Map.Entry<String, Object> entry : serviceList.entrySet()) {
                Object obj = entry.getValue();
                Class<?> serviceClass = obj.getClass();
                Class<?>[] interfaces = serviceClass.getInterfaces();
                TProcessor processor = null;
                String serviceName;
                for(Class<?> clazz : interfaces) {
                    logger.info("thrift=======" + clazz.getSimpleName());
                    String className = clazz.getEnclosingClass().getSimpleName();
                    serviceName = clazz.getEnclosingClass().getName();
                    logger.info("serviceName====" + serviceName + " " + className);
                    String pname = serviceName + "$Processor";
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    Class<?> pclass = classLoader.loadClass(pname);
                    if(!TProcessor.class.isAssignableFrom(pclass)) {
                        continue;
                    }
                    Constructor<?> constructor = pclass.getConstructor(clazz);
                    processor = (TProcessor) constructor.newInstance(obj);
                    logger.info("processor=====" + processor.getClass().getSimpleName());
                    mprocessor.registerProcessor(className, processor);
                }

                if (processor == null) {
                    throw new IllegalClassFormatException("service-class should implements Iface");
                }
            }

            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(mprocessor));
            logger.info("starting server on port {}", port);
            this.registerZookeeper("127.0.0.1", port);
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        serverTransport.close();
    }

    public void setServiceList(Map<String, Object> serviceList) {
        this.serviceList = serviceList;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private void registerZookeeper(String ip, int port) {
        try {
            ZooKeeper zk = new ZooKeeper(ZK_HOST, ZK_TIME_OUT, new Watcher() {
                    @Override
                    public void process(WatchedEvent watchedEvent) {

                    }
                });

            ServerAddr serverAddr = new ServerAddr(ip, port);
            List<ServerAddr> serverAddrList = new ArrayList<ServerAddr>();
            serverAddrList.add(serverAddr);
            String data = gson.toJson(serverAddrList);
            if(zk.exists("/dbserver", false) == null) {
                zk.create("/dbserver", data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } else {
                data =  new String(zk.getData("/dbserver", false, null));
                serverAddrList = gson.fromJson(data, new TypeToken<List<ServerAddr>>(){}.getType());
                if(!serverAddrList.contains(serverAddr)) {
                    serverAddrList.add(serverAddr);
                    zk.setData("/dbserver", gson.toJson(serverAddrList).getBytes(), -1);
                }
            }

            logger.info(new String(zk.getData("/dbserver", false, null)));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("register dbserver to zookeeper failed");
            return;
        }

        logger.info("register dbserver to zookeeper success");
    }

}
