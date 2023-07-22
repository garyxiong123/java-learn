package encode;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.compression.JdkZlibEncoder;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


/**
 * Created on 2023/7/5 14:40. <br/>
 * Description: <br/>
 *
 * @author user
 */
public class TestJdkZlibDecoder {
    public static void main(String[] args) {

//        ByteBuf data = Unpooled.wrappedBuffer("message".getBytes(CharsetUtil.UTF_8));
////        ByteBuf deflatedData = Unpooled.wrappedBuffer(gzip("message"));
//
////        EmbeddedChannel chDecoderGZip = new EmbeddedChannel(new JdkZlibDecoder(ZlibWrapper.GZIP));
////        chDecoderGZip.writeInbound(deflatedData.copy());
//
//        EmbeddedChannel encoder=new EmbeddedChannel(ZlibCodecFactory.newZlibEncoder(
//                ZlibWrapper.NONE, 6, 15, 8));
////        EmbeddedChannel encoder = new EmbeddedChannel(new JdkZlibEncoder(ZlibWrapper.GZIP));
//        encoder.writeOutbound(data.retain());
//
//        if (encoder.finish()) {
//            if (encoder.readOutbound() == data) {
//                //System.out.println(encoder.readInbound());
//            }
//        }

        compressContent("1");

    }

    private static EmbeddedChannel encoder=new EmbeddedChannel(ZlibCodecFactory.newZlibEncoder(
            ZlibWrapper.NONE, 6, 15, 8));
    static final ByteBuf FRAME_TAIL = Unpooled.unreleasableBuffer(
            Unpooled.wrappedBuffer(new byte[] {0x00, 0x00, (byte) 0xff, (byte) 0xff}))
            .asReadOnly();
    private static EmbeddedChannel decoder=new EmbeddedChannel(ZlibCodecFactory.newZlibDecoder(ZlibWrapper.NONE));
    private static String compressContent(String msg) {
        try {
            ByteBuf data = Unpooled.wrappedBuffer("he".getBytes(CharsetUtil.UTF_8));
            //System.out.println(convert(data.retain()));
//            encoder.
//            encoder.writeOutbound(data.retain());
            encoder.writeOutbound(data.retain());

            ByteBuf fullCompressedContent= encoder.readOutbound();
            int realLength = fullCompressedContent.readableBytes() - FRAME_TAIL.readableBytes();
            ByteBuf compressedContent = fullCompressedContent.slice(0, realLength);
            String ss=convert(compressedContent.retain());
            System.out.println(ss);

            decoder.writeInbound(Unpooled.wrappedBuffer(ss.getBytes(CharsetUtil.UTF_8)));
            ByteBuf byteBuf = decoder.readInbound();

            System.out.println(convert(byteBuf.retain()));

            return "";
        } catch (Exception ex) {
            System.out.println(ex.getCause());
        }

        return null;

    }


    private static String convert(ByteBuf msg){
        ByteBuf buf = msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        return new String(req, StandardCharsets.UTF_8);
    }
}
