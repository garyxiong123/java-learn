package swift;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import server.ClientHandler;
import server.ServerChannelHandler;


/**
 * Sure! Here's a simple example of a Java Netty server and client that can communicate with each other:
 */
public class NettyDemo4 {
    //http://localhost:8080/


    public static void main(String[] args) throws InterruptedException {
        SelectStrategyFactory selectStrategyFactory = DefaultSelectStrategyFactory.INSTANCE;
        NioEventLoopGroup group = new NioEventLoopGroup(10);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ServerChannelHandler());
                        }
                    });

            ChannelFuture serverFuture = serverBootstrap.bind(8080).sync();
            System.out.println("Server started on port 8080");


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

                ChannelFuture clientFuture = clientBootstrap.connect("localhost", 8080).sync();
                clientFuture.channel().write("ss");
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
