package com.gzdx.imooc.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.Date;

/**
 * Created by leijiang on 2018/5/22.
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<Object> {
    private WebSocketServerHandshaker handshaker;
    private static final String WEB_SOCKET_URL = "ws://localhost:8888/websocket";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyConfig.group.add(ctx.channel());
        System.out.println("客户端与服务端连接开启...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyConfig.group.remove(ctx.channel());
        System.out.println("客户端与服务端连接关闭...");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
    }


    /**
     * 处理客户端与服务器之间的webSocket业务
     * @param ctx
     * @param frame
     */
    private void handWebSocketFrame(ChannelHandlerContext ctx,WebSocketFrame frame){
        if(frame instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
        }

        if(frame instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        if(!(frame instanceof TextWebSocketFrame)){
            System.out.println("目前我们不支持二进制消息");
            throw  new RuntimeException("【"+this.getClass().getName()+"】不支持消息");
        }

        String request = ((TextWebSocketFrame)frame).text();
        System.out.println("服务端接收到客户端的消息====>>>> " + request);
        TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString() + ctx.channel() + " ===>>> " + request);
        NettyConfig.group.writeAndFlush(tws);
    }

    /**
     * 处理客户端发起的http握手请求业务
     * @param ctx
     * @param req
     */
    private void handHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        if (!req.getDecoderResult().isSuccess() || !("websocket".equals(req.headers().get("Upgrad")))) {
            sendHttpResponse(ctx, req,
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                WEB_SOCKET_URL, null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        if (res.getStatus().code() == 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
