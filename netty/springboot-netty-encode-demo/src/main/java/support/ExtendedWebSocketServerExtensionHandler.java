package support;

import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketServerExtensionHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.ClientNoContextWrapperPerMessageDeflateServerExtensionHandshaker;
import io.netty.handler.codec.http.websocketx.extensions.compression.CustomPerMessageDeflateServerExtensionHandshaker;

public class ExtendedWebSocketServerExtensionHandler extends WebSocketServerExtensionHandler {

    public ExtendedWebSocketServerExtensionHandler() {
        super(
                new ClientNoContextWrapperPerMessageDeflateServerExtensionHandshaker(
                        new CustomPerMessageDeflateServerExtensionHandshaker(),
                        false),
                new ClientNoContextWrapperPerMessageDeflateServerExtensionHandshaker(
                        new CustomPerMessageDeflateServerExtensionHandshaker(
                                6,
                                ZlibCodecFactory.isSupportingWindowSizeAndMemLevel(),
                                15,
                                true,
                                false),
                        false),
                new ClientNoContextWrapperPerMessageDeflateServerExtensionHandshaker(
                        new CustomPerMessageDeflateServerExtensionHandshaker(
                                6,
                                ZlibCodecFactory.isSupportingWindowSizeAndMemLevel(),
                                15,
                                false,
                                true),
                        true),
                new ClientNoContextWrapperPerMessageDeflateServerExtensionHandshaker(
                        new CustomPerMessageDeflateServerExtensionHandshaker(
                                6,
                                ZlibCodecFactory.isSupportingWindowSizeAndMemLevel(),
                                15,
                                true,
                                true),
                        true));
    }
}
