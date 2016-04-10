package xyz.goome.eternal.dbserver;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Constructor;
import java.util.Map;


/**
 * Created by matrix on 16/4/10.
 */
public class ThriftServer {

    private static final Logger logger = LoggerFactory.getLogger(ThriftServer.class);

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

}
