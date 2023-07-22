package io.netty.handler.codec.http.websocketx.extensions.compression;

import io.netty.handler.codec.http.websocketx.extensions.WebSocketExtensionData;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtension;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandshaker;

import java.util.Map;



public class ClientNoContextWrapperPerMessageDeflateServerExtensionHandshaker implements WebSocketServerExtensionHandshaker {

    public static final String CLIENT_NO_CONTEXT = "client_no_context_takeover";
    static final String SERVER_NO_CONTEXT = "server_no_context_takeover";
    public final WebSocketServerExtensionHandshaker handshaker;
    private final boolean clientNoContext;

    public ClientNoContextWrapperPerMessageDeflateServerExtensionHandshaker(
            WebSocketServerExtensionHandshaker handshaker,
            boolean clientNoContext) {
        this.handshaker = handshaker;
        this.clientNoContext = clientNoContext;
    }

    @Override
    public WebSocketServerExtension handshakeExtension(WebSocketExtensionData extensionData) {
        for (Map.Entry<String, String> parameter : extensionData.parameters().entrySet()) {
            if (CLIENT_NO_CONTEXT.equalsIgnoreCase(parameter.getKey()) && !this.clientNoContext) {
                return null;
            }
        }

        return this.handshaker.handshakeExtension(extensionData);
    }
}
