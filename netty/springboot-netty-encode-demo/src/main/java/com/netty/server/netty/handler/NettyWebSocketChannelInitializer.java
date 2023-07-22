package com.netty.server.netty.handler;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import support.ExtendedWebSocketServerExtensionHandler;
import support.MaxWindowBitsOutOfBoundsHandler;

@Component
@Qualifier("somethingChannelInitializer")
public class NettyWebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private TextWebSocketFrameHandler textWebSocketFrameHandler;

    @Autowired
    private WebSocketServerHandler webSocketServerHandler;


    @Override
    public void initChannel(SocketChannel ch) throws Exception {//2
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        pipeline.addLast(new MaxWindowBitsOutOfBoundsHandler());

        pipeline.addLast(new ExtendedWebSocketServerExtensionHandler());
//        pipeline.addLast(new WebSocketServerProtocolHandler("/"));


//        pipeline.addLast(new ChunkedWriteHandler());

        pipeline.addLast(webSocketServerHandler);

    }
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ctx.channel().eventLoop().execute(() -> {
//            // do something with msg
//            System.out.println("Received event: " + msg + ",Thread:" + Thread.currentThread().getName());
//        });
//        ctx.fireChannelRead(msg);
//
//    }
}
