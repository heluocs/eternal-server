package xyz.goome.thrift;

import org.apache.thrift.TException;
import xyz.goome.thrift.AdditionService.Iface;

/**
 * Created by matrix on 16/4/8.
 */
public class AdditionServiceHandler implements Iface {
    @Override
    public int add(int n1, int n2) throws TException {
        return n1 + n2;
    }
}
