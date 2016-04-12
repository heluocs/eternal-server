package xyz.goome.eternal.loginserver.rpc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.goome.eternal.common.entity.ServerAddr;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by matrix on 16/4/10.
 */
public class ThriftClient {

    private static final Logger logger = LoggerFactory.getLogger(ThriftClient.class);
    private static Gson gson = new GsonBuilder().create();
    private static final int ZK_TIME_OUT = 12000;
    private static final String ZK_HOST = "127.0.0.1:2181";

    private TTransport transport;
    private Map<String, String> serviceMap;
    private Map<String, Object> clientMap;

    public void setServiceMap(Map<String, String> serviceMap) {
        this.serviceMap = serviceMap;
    }

    public Object getClient(String name) {
        return clientMap.get(name);
    }

    public void init() {
        ServerAddr serverAddr = this.getDBServerAddr();
        if(serverAddr == null) {
            logger.info("no db server list");
            return;
        }
        clientMap = new HashMap<String, Object>();
        try {
            transport = new TSocket(serverAddr.getIp(), serverAddr.getPort());
            TProtocol protocol = new TBinaryProtocol(transport);
            for(Map.Entry<String, String> entry : serviceMap.entrySet()){
                String obj = entry.getValue();
                System.out.println(entry.getKey() + " " + entry.getValue());
                TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
                        entry.getKey());
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                Class<?> objectClass = classLoader.loadClass(obj + "$Client");

                Constructor<?> constructor = objectClass.getDeclaredConstructor(TProtocol.class);
                Object client = constructor.newInstance(mp);
                clientMap.put(entry.getKey(), client);
            }

            transport.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        transport.close();
    }

    private ServerAddr getDBServerAddr() {
        try {
            ZooKeeper zk = new ZooKeeper(ZK_HOST, ZK_TIME_OUT, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {

                }
            });

            String json =  new String(zk.getData("/dbserver", false, null));
            List<ServerAddr> serverAddrList = gson.fromJson(json, new TypeToken<List<ServerAddr>>(){}.getType());
            return serverAddrList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("regist dbserver to zookeeper failed");
            return null;
        }
    }
}
