package com.gzdx.mynetty.base.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
    private final int port;

    public EchoServer(int port){
        this.port = port;
    };

    public static void main(String[] args) throws Exception {
        if(args.length != 1){
            System.err.println("Usage:  " + EchoServer.class.getSimpleName()  +
                    " <port>");
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    private void start() throws Exception {
        final EchoServerHander serverHander = new EchoServerHander();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();     //引导和绑定服务器
            b.group(group)
                    .channel(NioServerSocketChannel.class)           //进行事件的处理
                    .localAddress(new InetSocketAddress(port))          //指定服务器绑定
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverHander);         //使用一个EchoServerHandler的实例初始化每一个新的Channel
                        }
                    });
            ChannelFuture f = b.bind().sync();          //调用ServerBootstrap.bind()方法以绑定服务器
            f.channel().closeFuture().sync();
        }finally {

        }
    }
}
