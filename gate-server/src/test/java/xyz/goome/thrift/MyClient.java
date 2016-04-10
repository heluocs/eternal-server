package xyz.goome.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * Created by matrix on 16/4/8.
 */
public class MyClient {

    public static void main(String[] args) throws TException {
        TTransport transport;
        transport = new TSocket("localhost", 9090);
        transport.open();

        TProtocol protocol = new TBinaryProtocol(transport);
        AdditionService.Client client = new AdditionService.Client(protocol);
        System.out.println(client.add(1, 2));
        transport.close();
    }

}
