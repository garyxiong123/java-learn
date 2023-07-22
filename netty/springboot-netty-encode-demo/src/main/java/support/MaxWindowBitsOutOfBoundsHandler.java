package support;

import com.google.common.base.Strings;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionData;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionUtil;
import io.netty.util.ReferenceCountUtil;

import java.util.List;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS;

public class MaxWindowBitsOutOfBoundsHandler extends SimpleChannelInboundHandler<Object> {

    public static final int MIN_WINDOW_SIZE = 8;
    public static final int MAX_WINDOW_SIZE = 15;

    static final String PERMESSAGE_DEFLATE_EXTENSION = "permessage-deflate";
    static final String CLIENT_MAX_WINDOW = "client_max_window_bits";
    static final String SERVER_MAX_WINDOW = "server_max_window_bits";


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            final FullHttpRequest request = (FullHttpRequest) msg;
            final HttpHeaders headers = request.headers();
            if (WebSocketUtils.isWebsocketUpgrade(headers)) {
                final String secWebsocketExtensions = headers.getAsString(SEC_WEBSOCKET_EXTENSIONS);
                if (!Strings.isNullOrEmpty(secWebsocketExtensions)) {
                    final List<WebSocketExtensionData> extensions = WebSocketExtensionUtil
                            .extractExtensions(secWebsocketExtensions);

                    if (isMaxWindowBitsOutOfBounds(extensions)) {
                        ctx.channel()
                                .writeAndFlush(new DefaultFullHttpResponse(
                                        request.protocolVersion(), HttpResponseStatus.BAD_REQUEST))
                                .addListener(ChannelFutureListener.CLOSE);
                        try {
                            final String ext = extensions.stream()
                                    .map(data -> data.name().concat(data.parameters().toString()))
                                    .collect(Collectors.joining(","));
                        } catch (Exception e) {
                        }
                        return;
                    }
                }
            }
        }

        ctx.fireChannelRead(ReferenceCountUtil.retain(msg));
    }

    private boolean isMaxWindowBitsOutOfBounds(List<WebSocketExtensionData> extensions) {
        for (WebSocketExtensionData extension : extensions) {
            if (extension.name().equalsIgnoreCase(PERMESSAGE_DEFLATE_EXTENSION)
                    && (isBitOutOfBound(extension, SERVER_MAX_WINDOW)
                    || isBitOutOfBound(extension, CLIENT_MAX_WINDOW))) {
                return true;
            }
        }

        return false;
    }

    private boolean isBitOutOfBound(WebSocketExtensionData extension, String extensionName) {
        final String maxWindowBitStr = extension.parameters().get(extensionName);
        if (!Strings.isNullOrEmpty(maxWindowBitStr)) {
            final int maxWindowBit = Integer.parseInt(maxWindowBitStr);
            return maxWindowBit < MIN_WINDOW_SIZE || maxWindowBit > MAX_WINDOW_SIZE;
        }

        return false;
    }
}
