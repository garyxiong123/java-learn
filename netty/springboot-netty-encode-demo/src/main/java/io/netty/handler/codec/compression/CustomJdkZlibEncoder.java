package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.util.ReferenceCountUtil;

/**
 * Created on 2023/7/7 21:57. <br/>
 * Description: <br/>
 *
 * @author user
 */
public class CustomJdkZlibEncoder extends JdkZlibEncoder {

    public CustomJdkZlibEncoder(ZlibWrapper zlibWrapper, int compressionLevel) {
        super(zlibWrapper,compressionLevel);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf uncompressed, ByteBuf out) throws Exception {
        super.encode(null, uncompressed, out);
    }

    public ByteBuf encode(ByteBuf msg) {
        ByteBuf buf = null;
        try {
            if (acceptOutboundMessage(msg)) {
                buf = allocateBuffer(msg);
                try {
                    encode(null, msg, buf);
                } finally {
                    ReferenceCountUtil.release(msg);
                }

                if (buf.isReadable()) {
                    return buf;
                } else {
                    buf.release();
                    return Unpooled.EMPTY_BUFFER;
                }
            } else {
                return msg;
            }
        } catch (EncoderException e) {
            throw e;
        } catch (Throwable e) {
            throw new EncoderException(e);
        }
    }

    private ByteBuf allocateBuffer(ByteBuf msg) {
        int sizeEstimate = (int) Math.ceil(msg.readableBytes() * 1.001) + 12;
        return Unpooled.buffer(sizeEstimate);
    }
}
