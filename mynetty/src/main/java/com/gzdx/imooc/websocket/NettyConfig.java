package com.gzdx.imooc.websocket;

import io.netty.channel.ChannelConfig;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by leijiang on 2018/5/22.
 */
public class NettyConfig {

    /**
     * 存储每一个客户端接进来的Channel对象
     */
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
