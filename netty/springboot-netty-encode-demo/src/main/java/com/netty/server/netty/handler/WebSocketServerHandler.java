package com.netty.server.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.compression.*;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


/**
 * Created on 2023/7/6 19:37. <br/>
 * Description: <br/>
 *
 * @author user
 */
@Component
@Qualifier("webSocketServerHandler")
@ChannelHandler.Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static ChannelHandlerContext channelHandlerContext = null;

    private CustomJdkZlibEncoder encoder=new CustomJdkZlibEncoder(ZlibWrapper.NONE, 6);

    static final ByteBuf FRAME_TAIL = Unpooled.unreleasableBuffer(
            Unpooled.wrappedBuffer(new byte[] {0x00, 0x00, (byte) 0xff, (byte) 0xff}))
            .asReadOnly();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest req = (FullHttpRequest) msg;
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(req), null, true);
            WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
            handshaker.handshake(ctx.channel(), req);
        } else if (msg instanceof WebSocketFrame) {
            if (msg instanceof CloseWebSocketFrame){
                System.out.println("CloseWebSocketFrame");
                return;
            }
            TextWebSocketFrame req = (TextWebSocketFrame) msg;
            channelHandlerContext = ctx;
            System.out.println("Received event: " + msg + ",Thread:" + Thread.currentThread().getName() + ",id=" + Thread.currentThread().getId());
//            ctx.channel().writeAndFlush(new TextWebSocketFrame("[you]" + ",Thread:" + Thread.currentThread().getName()));
//            ByteBuf data=compressContent("[you]" + ",Thread:" + Thread.currentThread().getName());
            ctx.channel().writeAndFlush(new TextWebSocketFrame(true,4,compressContent("[you]" + ",Thread:" + Thread.currentThread().getName())));

        } else {
            System.out.println("error");
        }
    }

    private String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HttpHeaderNames.HOST);
        return "ws://" + location;
    }

    private ByteBuf compressContent(String msg) {
        long start=System.nanoTime();
        try {
            long start1=System.nanoTime();
            ByteBuf data = Unpooled.copiedBuffer(msg.getBytes(CharsetUtil.UTF_8));
            System.out.println((System.nanoTime()-start1)/1000);

            long start2=System.nanoTime();
            ByteBuf fullCompressedContent = encoder.encode(data);
            System.out.println((System.nanoTime()-start2)/1000);

            long start3=System.nanoTime();
            int realLength = fullCompressedContent.readableBytes() - FRAME_TAIL.readableBytes();
            System.out.println((System.nanoTime()-start3)/1000);

            long start4=System.nanoTime();
            ByteBuf compressedContent = fullCompressedContent.slice(0, realLength);
            System.out.println((System.nanoTime()-start4)/1000);

            return compressedContent;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        //System.out.println((System.nanoTime()-start)/1000);
        return null;
    }

//    private ByteBuf compressContent1(String msg) {
//        try {
//
////            ZlibEncoder zlibEncoder=ZlibCodecFactory.newZlibEncoder(
////                    ZlibWrapper.NONE, 6, 15, 8);
//            JdkZlibEncoder1 jdkZlibEncoder1=new JdkZlibEncoder1();
//            jdkZlibEncoder1.encode();
//            ByteBuf data = Unpooled.copiedBuffer(msg.getBytes(CharsetUtil.UTF_8));
//
//            encoder.writeOutbound(data.retain());
//            ByteBuf fullCompressedContent=encoder.readOutbound();
//            int realLength = fullCompressedContent.readableBytes() - FRAME_TAIL.readableBytes();
//            ByteBuf compressedContent = fullCompressedContent.slice(0, realLength);
//            return compressedContent.retain();
//        }catch (Exception ex){
//            System.out.println(ex.getMessage());
//        }
//
//        return null;
//    }
}
