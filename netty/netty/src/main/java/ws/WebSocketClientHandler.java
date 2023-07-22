package ws;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private final WebSocketClientHandshaker handshaker;

    public WebSocketClientHandler(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public WebSocketClientHandshaker getHandshaker() {
        return handshaker;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handshaker.handshake(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // Handle incoming WebSocket frames
        if (frame instanceof TextWebSocketFrame) {
            // Handle text frame
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            String message = textFrame.text();
            System.out.println("Received message from server: " + message);
        } else {
            // Handle other frame types (e.g., BinaryWebSocketFrame)
            System.out.println("Received unsupported frame type");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshaker.isHandshakeComplete()) {
            System.out.println("WebSocket handshake failed");
        }
        ctx.close();
    }
}
