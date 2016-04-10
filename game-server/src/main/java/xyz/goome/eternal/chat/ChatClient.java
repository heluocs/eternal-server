package xyz.goome.eternal.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import xyz.goome.eternal.common.utils.ByteUtil;
import xyz.goome.eternal.message.LoginMessage;

/**
 * Created by matrix on 16/3/24.
 */
public class ChatClient {

    public static void main(String[] args) {
        new ChatClient("localhost", 8080).run();
    }

    private final String host;
    private final int port;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChatClientInitializer());
            Channel channel = bootstrap.connect(host, port).sync().channel();
            if(channel.isActive()) {
                sendMessage(channel);
            }

            while(true) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    private void sendMessage(Channel channel) {
        LoginMessage.CMsgAccountLoginRequest request = LoginMessage.CMsgAccountLoginRequest.newBuilder()
                .setAccount("eternal")
                .build();
        /*
        AuthMsg.AuthRequest request = AuthMsg.AuthRequest.newBuilder()
                .setUserId("010203")
                .setPassword("abcde")
                .build();
        */
        byte[] body = request.toByteArray();
        byte[] msgno = ByteUtil.int2byte(0x00010001);

        short msglen = (short) (body.length + msgno.length + 2);
        byte[] msglenArr = ByteUtil.short2byte(msglen);

        ByteBuf byteBuf = Unpooled.buffer(msglen);
        byteBuf.writeBytes(msglenArr);
        byteBuf.writeBytes(msgno);
        byteBuf.writeBytes(body);

        System.out.println(byteBuf.capacity());
        channel.writeAndFlush(byteBuf);
    }
}
