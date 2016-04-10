package xyz.goome.eternal.gateserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by matrix on 16/4/7.
 */
public class GateServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
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
        for (int i = 0; i < content.length; i++) {
            content[i] = buf[i + 6];
        }

        ChannelHarbor.getInstance().addChannel(ctx.channel());
    }


}
