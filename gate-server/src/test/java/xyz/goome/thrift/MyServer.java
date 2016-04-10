package xyz.goome.thrift;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

/**
 * Created by matrix on 16/4/8.
 */
public class MyServer {

    public static void startServer(AdditionService.Processor<AdditionServiceHandler> processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));
            System.out.println("start server");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyServer().startServer(new AdditionService.Processor<AdditionServiceHandler> (new AdditionServiceHandler()));
    }

}
