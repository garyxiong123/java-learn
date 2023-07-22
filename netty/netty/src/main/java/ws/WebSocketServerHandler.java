package ws;

import io.netty.channel.ChannelHandler;
import io.netty.util.concurrent.EventExecutorGroup;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // Handle incoming WebSocket frames
        if (frame instanceof TextWebSocketFrame) {
            // Handle text frame
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            String message = textFrame.text();
            System.out.println("Received message from client: " + message);

            // Echo the message back to the client
            ctx.writeAndFlush(new TextWebSocketFrame("Server echo: " + message));
        } else {
            // Handle other frame types (e.g., BinaryWebSocketFrame)
            System.out.println("Received unsupported frame type");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
