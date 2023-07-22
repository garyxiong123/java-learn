package eventLoop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;

public class EventLoopGroupTest {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new KQueueEventLoopGroup(1);
        EventLoopGroup workerGroup = new KQueueEventLoopGroup(16);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(KQueueServerSocketChannel.class)
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel ch) {
                            // Configure your channel pipeline
                            // e.g., add handlers for request/response processing
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                            ctx.channel().eventLoop().submit(() -> {
                                // do something with msg
                                System.out.println("Received event: " + msg + ",Thread:" + Thread.currentThread().getName());
                            });
                            ctx.fireChannelRead(msg);
                        }
                    });

            // Start the server
            serverBootstrap.bind(9999).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
