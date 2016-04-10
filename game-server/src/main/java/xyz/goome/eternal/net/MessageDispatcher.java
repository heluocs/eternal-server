package xyz.goome.eternal.net;

/**
 * Created by matrix on 16/3/28.
 */
public class MessageDispatcher {

    private static MessageDispatcher instance = new MessageDispatcher();

    private MessageDispatcher() {

    }

    public static MessageDispatcher getInstance() {
        return instance;
    }

    public void dispatch(int msgno, byte[] body) {
        int module = msgno >> 16;
        int action = msgno & 0xffff;
        System.out.println("module:" + module);
        System.out.println("action:" + action);
    }
}
