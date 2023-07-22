package com.netty.server;


import com.netty.server.netty.handler.NettyWebSocketChannelInitializer;
import com.netty.server.netty.handler.WebSocketServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOption;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@PropertySource(value = "classpath:/nettyserver.properties")
@SpringBootApplication
@EnableScheduling
public class NettyApplication {

    @Value("${tcp.port}")
    private int tcpPort;

    @Value("${boss.thread.count}")
    private int bossCount;

    @Value("${worker.thread.count}")
    private int workerCount;

    @Value("${so.keepalive}")
    private boolean keepAlive;

    @Value("${so.backlog}")
    private int backlog;

    private EmbeddedChannel encoder=new EmbeddedChannel(ZlibCodecFactory.newZlibEncoder(
            ZlibWrapper.NONE, 6, 15, 8));

    private EmbeddedChannel decoder=new EmbeddedChannel(ZlibCodecFactory.newZlibDecoder(ZlibWrapper.NONE));

    @Bean(name = "serverBootstrap")
    public ServerBootstrap bootstrap() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))

                .childHandler(nettyWebSocketChannelInitializer);
        Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions();
        Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
        for (@SuppressWarnings("rawtypes") ChannelOption option : keySet) {
            b.option(option, tcpChannelOptions.get(option));
        }
        return b;
    }

    @Autowired
    @Qualifier("somethingChannelInitializer")
    private NettyWebSocketChannelInitializer nettyWebSocketChannelInitializer;

    @Bean(name = "tcpChannelOptions")
    public Map<ChannelOption<?>, Object> tcpChannelOptions() {
        Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
        options.put(ChannelOption.SO_KEEPALIVE, keepAlive);
        options.put(ChannelOption.SO_BACKLOG, backlog);
        return options;
    }

    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossCount);
    }

    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {

        return new NioEventLoopGroup(workerCount);
    }

    @Bean(name = "tcpSocketAddress")
    public InetSocketAddress tcpPort() {
        return new InetSocketAddress(tcpPort);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Thread:" + Thread.currentThread().getName() + ",id=" + Thread.currentThread().getId());

        ConfigurableApplicationContext context = SpringApplication.run(NettyApplication.class, args);
        TCPServer tcpServer = context.getBean(TCPServer.class);
        tcpServer.start();
    }
    //@Scheduled(cron = "*/6 * * * * ?")
    public void sayHello() {
        if (WebSocketServerHandler.channelHandlerContext == null) {
            return;

        }
        int i = 10;
        System.out.println("Thread:" + Thread.currentThread().getName() + ",id=" + Thread.currentThread().getId());

        while (i-- > 0) {
            WebSocketServerHandler.channelHandlerContext.channel().eventLoop().execute(() -> {
                // do something with msg
                System.out.println("Received event: " + "push" + ",Thread:" + Thread.currentThread().getName() + ",id=" + Thread.currentThread().getId());

                WebSocketServerHandler.channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame(compressContent("[you]" + ",Thread:" + Thread.currentThread().getName())));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }

//    private ByteBuf compressContent(String msg) {
//        try {
//            ByteBuf data = Unpooled.wrappedBuffer(msg.getBytes(CharsetUtil.UTF_8));
////            encoder.
//            encoder.writeOutbound(data.retain());
//            ByteBuf byteBuf= encoder.readOutbound();
//            String con = ByteBufUtil.hexDump((ByteBuf) byteBuf);
//            System.out.println(con);
//            return byteBuf;
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
//
//        return null;
//
//    }

    private String compressContent(String msg) {
        try {
            ByteBuf data = Unpooled.wrappedBuffer("1".getBytes(CharsetUtil.UTF_8));
//            encoder.
            encoder.writeOutbound(data.retain());
            ByteBuf byteBuf= encoder.readOutbound();
            String con = ByteBufUtil.hexDump(byteBuf);
            System.out.println(con);

            decoder.writeInbound(byteBuf);
            byteBuf = decoder.readOutbound();

            con = ByteBufUtil.hexDump(byteBuf);
            System.out.println(con);

            return con;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return null;

    }
}
