package swift;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.SneakyThrows;
import net.openhft.affinity.AffinityLock;
import server.ClientHandler;

import java.util.ArrayDeque;

public class NettyTestUtil {

    public static void createServer() {
        EventLoopGroup bossGroup = new KQueueEventLoopGroup(1);
        SelectStrategyFactory threadFactory = DefaultSelectStrategyFactory.INSTANCE;
        EventLoopGroup workerGroup = new KQueueEventLoopGroup(16, threadFactory);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(KQueueServerSocketChannel.class).childHandler(new ChannelInitializer() {
                @Override
                protected void initChannel(Channel ch) {
                    // Configure your channel pipeline
                    // e.g., add handlers for request/response processing
                }

                @Override
                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                    System.out.println(AffinityLock.dumpLocks());
                    System.out.println(msg);
                    ctx.fireChannelRead(msg);
                }
            });


            new Thread(() -> {
                createClient();
            }).start();


            // Start the server
            serverBootstrap.bind(9999).sync().channel().closeFuture().sync();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @SneakyThrows
    public static void createClient() {

        //Client
        NioEventLoopGroup clientGroup = new NioEventLoopGroup();

        try {
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap.group(clientGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new ClientHandler());
                }
            });

            ChannelFuture clientFuture = clientBootstrap.connect("localhost", 9999).sync();
            System.out.println("Client connected to server");
            while (true) {

                clientFuture.channel().write("ss");
            }


//            clientFuture.channel().closeFuture().sync();
        } finally {
            clientGroup.shutdownGracefully();
        }
    }

}