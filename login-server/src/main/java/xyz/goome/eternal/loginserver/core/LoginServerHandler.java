package xyz.goome.eternal.loginserver.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by matrix on 16/3/29.
 */
@Component
public class LoginServerHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(LoginServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        int totalLength = byteBuf.readableBytes();
        byte[] buf = new byte[totalLength];
        byteBuf.readBytes(buf);
        logger.info("total length: {}", buf.length);
        short size = (short) ((buf[1] << 8) | (buf[0] & 0xff));
        logger.info("body length: {}", size);
        int msgno = (buf[5] << 24) | (buf[4] << 16) | (buf[3] << 8) | (buf[2] & 0xff);
        logger.info("msgno: {}", msgno);

        byte[] msgbody = new byte[totalLength - 6];
        for(int i = 0; i < msgbody.length; i++) {
            msgbody[i] = buf[i + 6];
        }

        MessageDispatcher.getInstance().dispatchModule(ctx.channel(), msgno, msgbody);
    }

}
