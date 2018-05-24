package com.gzdx.imooc.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.stream.ChunkedWriteHandler;


/**
 * Created by leijiang on 2018/5/23.
 */
public class MyWebSocketChannelHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel e) throws Exception {
        e.pipeline().addLast("http-codec",new HttpClientCodec());
        e.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));
        e.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
        e.pipeline().addLast("handler",new MyWebSocketHandler());
    }
}
