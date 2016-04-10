package xyz.goome.eternal.dbserver.test;

import org.apache.thrift.TException;
import shared.SharedStruct;
import tutorial.Calculator;
import tutorial.InvalidOperation;
import tutorial.Work;

import java.util.HashMap;

/**
 * Created by matrix on 16/3/29.
 */
public class CalculatorHandler implements Calculator.Iface {

    private HashMap<Integer, SharedStruct> log;

    public CalculatorHandler() {
        log = new HashMap<Integer, SharedStruct>();
    }

    @Override
    public void ping() throws TException {
        System.out.println("ping()");
    }

    @Override
    public int add(int num1, int num2) throws TException {
        System.out.println("add(" + num1 + "," + num2 + ")");
        return num1 + num2;
    }

    @Override
    public int calculate(int logid, Work w) throws InvalidOperation, TException {
        System.out.println("calculate(" + logid + ", {" + w.op + "," + w.num1 + "," + w.num2 + "})");
        int val = 0;
        switch (w.op) {
            case ADD:
                val = w.num1 + w.num2;
                break;
            case SUBTRACT:
                val = w.num1 - w.num2;
                break;
            case MULTIPLY:
                val = w.num1 * w.num2;
                break;
            case DIVIDE:
                if(w.num2 == 0) {
                    InvalidOperation io = new InvalidOperation();
                    io.whatOp = w.op.getValue();
                    io.why = "Cannot divide by 0";
                    throw io;
                }
                val = w.num1 / w.num2;
                break;
            default:
                InvalidOperation io = new InvalidOperation();
                io.whatOp = w.op.getValue();
                io.why = "Unknown operation";
                throw io;
        }

        SharedStruct entry = new SharedStruct();
        entry.key = logid;
        entry.value = Integer.toString(val);
        log.put(logid, entry);

        return val;
    }

    @Override
    public void zip() throws TException {
        System.out.println("zip()");
    }

    @Override
    public SharedStruct getStruct(int key) throws TException {
        System.out.println("getStruct(" + key + ")");
        return log.get(key);
    }
}
