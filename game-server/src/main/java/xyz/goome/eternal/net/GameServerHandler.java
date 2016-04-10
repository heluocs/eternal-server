package xyz.goome.eternal.net;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import xyz.goome.eternal.common.utils.ByteUtil;
import xyz.goome.eternal.message.LoginMessage;

import java.util.logging.Logger;

/**
 * Created by matrix on 16/3/26.
 */
public class GameServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger(GameServerHandler.class.getName());

//    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        channels.add(ctx.channel());

        PlayerBucket.getInstance().addPlayer(ctx.channel());

        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(byteBuf.capacity());
        int totalLength = byteBuf.readableBytes();
        byte[] buf = new byte[totalLength];
        byteBuf.readBytes(buf);
        System.out.println("total length:" + buf.length);
        short size = (short) ((buf[1] << 8) | (buf[0] & 0xff));
        System.out.println("body length:" + size);
        int msgno = (buf[5] << 24) | (buf[4] << 16) | (buf[3] << 8) | (buf[2] & 0xff);
        System.out.println("msgno:" + msgno);

        byte[] content = new byte[totalLength - 6];
        for(int i = 0; i < content.length; i++) {
            content[i] = buf[i + 6];
        }

        MessageDispatcher.getInstance().dispatch(msgno, content);

        LoginMessage.CMsgAccountLoginRequest request = LoginMessage.CMsgAccountLoginRequest.parseFrom(content);
        System.out.println(request.getAccount());

        //send to client
        LoginMessage.CMsgAccountLoginResponse response = LoginMessage.CMsgAccountLoginResponse.newBuilder()
                .setAccountid(1101)
                .build();
        byte[] content2 = response.toByteArray();
        byte[] msgno2 = ByteUtil.int2byte(0x00010002);


        short msgLength = (short) (content2.length + msgno2.length + 2);
        byte[] msgLengthArr = ByteUtil.short2byte(msgLength);
        short len = ByteUtil.byte2short(msgLengthArr);

        ByteBuf byteBuf2 = Unpooled.buffer(msgLength);
        byteBuf2.writeBytes(msgLengthArr);
        byteBuf2.writeBytes(msgno2);
        byteBuf2.writeBytes(content2);

//        channels.writeAndFlush(byteBuf2);
        PlayerBucket.getInstance().broadcast(byteBuf2);
    }
}
