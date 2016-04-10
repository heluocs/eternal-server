package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import xyz.goome.eternal.common.message.Message;
import xyz.goome.eternal.common.utils.ByteUtil;
import xyz.goome.eternal.common.message.AccountMessage;

/**
 * Created by matrix on 16/3/24.
 */
public class Client {

    public static void main(String[] args) {
        new Client("localhost", 8888).run();
    }

    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientInitializer());
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
        AccountMessage.MsgAccountLoginRequest request = AccountMessage.MsgAccountLoginRequest.newBuilder()
                .setAccount("eternal")
                .setPassword("123456")
                .build();

        byte[] body = request.toByteArray();
        byte[] msgno = ByteUtil.int2byte(Message.MSG_ACCOUNT_LOGIN_REQUEST_C2S);

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
