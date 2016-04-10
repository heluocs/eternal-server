package xyz.goome.eternal.gateserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by matrix on 16/4/7.
 */
public class ChannelHarbor {

    private static ChannelHarbor instance = new ChannelHarbor();

    private ChannelGroup channels;

    private ChannelHarbor() {
        channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    public static ChannelHarbor getInstance() {
        return instance;
    }

    public void addChannel(Channel channel) {
        channels.add(channel);
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
    }

    public ChannelGroup getChannels() {
        return this.channels;
    }

    public void sendMsgToPlayer(ByteBuf byteBuf) {
        channels.writeAndFlush(byteBuf);
    }
}
