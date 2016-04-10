package xyz.goome.eternal.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;


/**
 * Created by matrix on 16/3/28.
 */
public class PlayerBucket {

    private static PlayerBucket instance = new PlayerBucket();

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private PlayerBucket() {

    }

    public static PlayerBucket getInstance() {
        return instance;
    }

    public void addPlayer(Channel channel) {
        channels.add(channel);
    }

    public void removePlayer(Channel channel) {
        channels.remove(channel);
    }

    public ChannelGroup getAllPlayer() {
        return channels;
    }

    public void broadcast(ByteBuf msg) {
        channels.writeAndFlush(msg);
    }

}
