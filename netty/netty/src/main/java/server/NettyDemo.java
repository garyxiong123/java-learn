package server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;


/**
 * Sure! Here's a simple example of a Java Netty server and client that can communicate with each other:
 */
public class NettyDemo {
    //http://localhost:8080/


    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(64 * 1024));
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
                            pipeline.addLast(new ServerChannelHandler());
                        }
                    });

            ChannelFuture serverFuture = serverBootstrap.bind(9999).sync();
            System.out.println("Server started on port 9999");


            //Client
            NioEventLoopGroup clientGroup = new NioEventLoopGroup();

            try {
                Bootstrap clientBootstrap = new Bootstrap();
                clientBootstrap.group(clientGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast(new ClientHandler());
                            }
                        });

                ChannelFuture clientFuture = clientBootstrap.connect("localhost", 9999).sync();
                clientFuture.channel().writeAndFlush(new TextWebSocketFrame("[you]" + ",Thread:" + Thread.currentThread().getName()));
                System.out.println("Client connected to server");

                clientFuture.channel().closeFuture().sync();
            } finally {
                clientGroup.shutdownGracefully();
            }

            serverFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
