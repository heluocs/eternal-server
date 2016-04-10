package xyz.goome.eternal.loginserver.rpc;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by matrix on 16/4/10.
 */
public class ThriftClient {

    private static final Logger logger = LoggerFactory.getLogger(ThriftClient.class);

    private int port;
    private TTransport transport;
    private Map<String, String> serviceMap;
    private Map<String, Object> clientMap;

    public void setPort(int port) {
        this.port = port;
    }

    public void setServiceMap(Map<String, String> serviceMap) {
        this.serviceMap = serviceMap;
    }

    public Object getClient(String name) {
        return clientMap.get(name);
    }

    public void init() {
        clientMap = new HashMap<String, Object>();
        try {
            transport = new TSocket("localhost", port);
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
}
