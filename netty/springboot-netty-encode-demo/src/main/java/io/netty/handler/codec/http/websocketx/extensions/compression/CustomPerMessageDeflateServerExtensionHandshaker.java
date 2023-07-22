package io.netty.handler.codec.http.websocketx.extensions.compression;

import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.http.websocketx.extensions.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static io.netty.util.internal.ObjectUtil.checkNotNull;

public class CustomPerMessageDeflateServerExtensionHandshaker implements WebSocketServerExtensionHandshaker {
    private final int compressionLevel;
    private final boolean allowServerWindowSize;
    private final int preferredClientWindowSize;
    private final boolean allowServerNoContext;
    private final boolean preferredClientNoContext;
    private final WebSocketExtensionFilterProvider extensionFilterProvider;
    public static final int MIN_WINDOW_SIZE = 8;
    public static final int MAX_WINDOW_SIZE = 15;

    static final String PERMESSAGE_DEFLATE_EXTENSION = "permessage-deflate";
    static final String CLIENT_MAX_WINDOW = "client_max_window_bits";
    static final String SERVER_MAX_WINDOW = "server_max_window_bits";

    public CustomPerMessageDeflateServerExtensionHandshaker() {
        this(6, ZlibCodecFactory.isSupportingWindowSizeAndMemLevel(), MAX_WINDOW_SIZE, false, false);
    }

    public CustomPerMessageDeflateServerExtensionHandshaker(int compressionLevel, boolean allowServerWindowSize,
                                                            int preferredClientWindowSize,
                                                            boolean allowServerNoContext, boolean preferredClientNoContext) {
        this(compressionLevel, allowServerWindowSize, preferredClientWindowSize, allowServerNoContext,
                preferredClientNoContext, WebSocketExtensionFilterProvider.DEFAULT);
    }

    public CustomPerMessageDeflateServerExtensionHandshaker(
            int compressionLevel, boolean allowServerWindowSize, int preferredClientWindowSize,
            boolean allowServerNoContext, boolean preferredClientNoContext,
            WebSocketExtensionFilterProvider extensionFilterProvider) {
        if (preferredClientWindowSize > MAX_WINDOW_SIZE || preferredClientWindowSize < MIN_WINDOW_SIZE) {
            throw new IllegalArgumentException(
                    "preferredServerWindowSize: " + preferredClientWindowSize + " (expected: 8-15)");
        }
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException(
                    "compressionLevel: " + compressionLevel + " (expected: 0-9)");
        }
        this.compressionLevel = compressionLevel;
        this.allowServerWindowSize = allowServerWindowSize;
        this.preferredClientWindowSize = preferredClientWindowSize;
        this.allowServerNoContext = allowServerNoContext;
        this.preferredClientNoContext = preferredClientNoContext;
        this.extensionFilterProvider = checkNotNull(extensionFilterProvider, "extensionFilterProvider");
    }

//    @Override
    @Override
    public WebSocketServerExtension handshakeExtension(WebSocketExtensionData extensionData) {
        if (!PERMESSAGE_DEFLATE_EXTENSION.equals(extensionData.name())) {
            return null;
        }

        boolean deflateEnabled = true;
        int clientWindowSize = MAX_WINDOW_SIZE;
        int serverWindowSize = MAX_WINDOW_SIZE;
        boolean serverNoContext = false;
        boolean clientNoContext = false;
        boolean clientWindowSizeAllowed = false;

        Iterator<Map.Entry<String, String>> parametersIterator =
                extensionData.parameters().entrySet().iterator();
        while (deflateEnabled && parametersIterator.hasNext()) {
            Map.Entry<String, String> parameter = parametersIterator.next();

            if (CLIENT_MAX_WINDOW.equalsIgnoreCase(parameter.getKey())) {
                // use preferred clientWindowSize because client is compatible with customization
                clientWindowSize = preferredClientWindowSize;
                clientWindowSizeAllowed = true;
            } else if (SERVER_MAX_WINDOW.equalsIgnoreCase(parameter.getKey())) {
                // use provided windowSize if it is allowed
                if (allowServerWindowSize) {
                    serverWindowSize = Integer.parseInt(parameter.getValue());
                    if (serverWindowSize > MAX_WINDOW_SIZE || serverWindowSize <= MIN_WINDOW_SIZE) {
                        deflateEnabled = false;
                    }
                } else {
                    deflateEnabled = false;
                }
            } else if (ClientNoContextWrapperPerMessageDeflateServerExtensionHandshaker.CLIENT_NO_CONTEXT.equalsIgnoreCase(parameter.getKey())) {
                // use preferred clientNoContext because client is compatible with customization
                clientNoContext = preferredClientNoContext;
            } else if (ClientNoContextWrapperPerMessageDeflateServerExtensionHandshaker.SERVER_NO_CONTEXT.equalsIgnoreCase(parameter.getKey())) {
                // use server no context if allowed
                if (allowServerNoContext) {
                    serverNoContext = true;
                } else {
                    deflateEnabled = false;
                }
            } else {
                // unknown parameter
                deflateEnabled = false;
            }
        }

        if (deflateEnabled) {
            return new PermessageDeflateExtension(
                    compressionLevel, serverNoContext, serverWindowSize, clientNoContext,
                    clientWindowSize, clientWindowSizeAllowed, allowServerWindowSize, extensionFilterProvider);
        } else {
            return null;
        }
    }

    private static class PermessageDeflateExtension implements WebSocketServerExtension {

        private final int compressionLevel;
        private final boolean serverNoContext;
        private final int serverWindowSize;
        private final boolean clientNoContext;
        private final int clientWindowSize;
        private final boolean clientWindowSizeAllowed;
        private final boolean serverWindowSizeAllowed;
        private final WebSocketExtensionFilterProvider extensionFilterProvider;

        PermessageDeflateExtension(int compressionLevel, boolean serverNoContext,
                                   int serverWindowSize, boolean clientNoContext,
                                   int clientWindowSize, boolean clientWindowSizeAllowed,
                                   boolean serverWindowSizeAllowed, WebSocketExtensionFilterProvider extensionFilterProvider) {
            this.compressionLevel = compressionLevel;
            this.serverNoContext = serverNoContext;
            this.serverWindowSize = serverWindowSize;
            this.clientNoContext = clientNoContext;
            this.clientWindowSize = clientWindowSize;
            this.clientWindowSizeAllowed = clientWindowSizeAllowed;
            this.serverWindowSizeAllowed = serverWindowSizeAllowed;
            this.extensionFilterProvider = extensionFilterProvider;
        }

//        @Override
        @Override
        public int rsv() {
            return RSV1;
        }

//        @Override
        @Override
        public WebSocketExtensionEncoder newExtensionEncoder() {
            return new io.netty.handler.codec.http.websocketx.extensions.compression.PerMessageDeflateEncoder(compressionLevel, serverWindowSize, serverNoContext,
                    WebSocketExtensionFilter.ALWAYS_SKIP);
        }

//        @Override
        @Override
        public WebSocketExtensionDecoder newExtensionDecoder() {
            return new io.netty.handler.codec.http.websocketx.extensions.compression.PerMessageDeflateDecoder(clientNoContext, extensionFilterProvider.decoderFilter());
        }

//        @Override
        @Override
        public WebSocketExtensionData newReponseData() {
            HashMap<String, String> parameters = new HashMap<>(4);
            if (serverNoContext) {
                parameters.put(ClientNoContextWrapperPerMessageDeflateServerExtensionHandshaker.SERVER_NO_CONTEXT, null);
            }
            if (clientNoContext) {
                parameters.put(ClientNoContextWrapperPerMessageDeflateServerExtensionHandshaker.CLIENT_NO_CONTEXT, null);
            }
            parameters.put(SERVER_MAX_WINDOW, Integer.toString(serverWindowSize));
            if (clientWindowSizeAllowed) {
                parameters.put(CLIENT_MAX_WINDOW, Integer.toString(clientWindowSize));
            }
            return new WebSocketExtensionData(PERMESSAGE_DEFLATE_EXTENSION, parameters);
        }
    }
}
