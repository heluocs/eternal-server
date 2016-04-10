package xyz.goome.eternal.loginserver.core;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xyz.goome.eternal.common.message.Message;
import xyz.goome.eternal.common.utils.Constants;
import xyz.goome.eternal.loginserver.module.AccountModule;

/**
 * Created by matrix on 16/3/29.
 */
@Component
public class MessageDispatcher {

    private static Logger logger = LoggerFactory.getLogger(MessageDispatcher.class);

    private static MessageDispatcher instance = new MessageDispatcher();

    private MessageDispatcher() {

    }

    public static MessageDispatcher getInstance() {
        return instance;
    }

    public void dispatchModule(Channel channel, int msgno, byte[] msgbody) {
        int module = msgno >> Constants.BYTE_OFFSET;
        int action = msgno & 0xffff;
        logger.info("module:{} action:{}", module, action);
        switch(module) {
            case Message.ACCOUNT_MODULE >> Constants.BYTE_OFFSET:
                AccountModule.getInstance().dispatchAction(channel, action, msgbody);
                break;
            default:
                break;
        }
    }
}
