package cost;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;


/**
 * Sure! Here's a simple example of a Java Netty server and client that can communicate with each other:
 */
public class UnpooledCopyBuffer {


    public static void main(String[] args) throws InterruptedException {
        original();
        constString();
        byteConcat();
        wrappedBufferConcat();
    }

    static void original() {
        String data = "1688980557117000 3042 382 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        int i = 50;
        long sum = 0;
        while (i > 0) {
            i--;
            long start = System.nanoTime();

            String str = "{\"stream\":\"rerere\",\"data\":" + new String(bytes) + ")}}";

            ByteBuf compressedByteBufWithCombined = Unpooled.wrappedBuffer(str.getBytes(StandardCharsets.UTF_8));
            ByteBufUtil.getBytes(compressedByteBufWithCombined);

            long end = System.nanoTime();

//            System.out.println((end - start) / 1000);

            sum += end - start;
        }
        System.out.println("original: "+(sum) / 1000 / 50+" us");
    }

    static void constString() {
        String data = "1688980557117000 3042 382 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);

        String con = "{\"stream\":\"rerere\",\"data\":";
        String con1 = "\")}}\"";


        int i = 50;
        long sum = 0;
        while (i > 0) {
            i--;
            long start = System.nanoTime();

            String str = con + new String(bytes) + con1;

            ByteBuf compressedByteBufWithCombined = Unpooled.wrappedBuffer(str.getBytes(StandardCharsets.UTF_8));
            ByteBufUtil.getBytes(compressedByteBufWithCombined);

            long end = System.nanoTime();

//            System.out.println((end - start) / 1000);

            sum += end - start;
        }
        System.out.println("constString: "+(sum) / 1000 / 50+" us");
    }

    static void byteConcat() {
        String data = "1688980557117000 3042 382 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);

        String con = "{\"stream\":\"rerere\",\"data\":";
        String con1 = "\")}}\"";

        byte[] first = con.getBytes(StandardCharsets.UTF_8);
        byte[] last = con1.getBytes(StandardCharsets.UTF_8);


        int i = 50;
        long sum = 0;
        while (i > 0) {
            i--;
            long start = System.nanoTime();

            ByteBufUtil.getBytes(Unpooled.wrappedBuffer(byteConcat(first, bytes, last)));

            long end = System.nanoTime();

//            System.out.println((end - start) / 1000);

            sum += end - start;
        }
        System.out.println("byteConcat: "+(sum) / 1000 / 50+" us");
    }

    static void wrappedBufferConcat() {
        String data = "1688980557117000 3042 382 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);

        String con = "{\"stream\":\"rerere\",\"data\":";
        String con1 = "\")}}\"";

        byte[] first = con.getBytes(StandardCharsets.UTF_8);
        byte[] last = con1.getBytes(StandardCharsets.UTF_8);


        int i = 50;
        long sum = 0;
        while (i > 0) {
            i--;
            long start = System.nanoTime();

            ByteBufUtil.getBytes(Unpooled.wrappedBuffer(first, bytes, last));

            long end = System.nanoTime();

//            System.out.println((end - start) / 1000);

            sum += end - start;
        }
        System.out.println("wrappedBufferConcat: "+(sum) / 1000 / 50+" us");
    }


    public static byte[] byteConcat(byte[] bt1, byte[] bt2, byte[] bt3) {
        byte[] bt4 = new byte[bt1.length + bt2.length + bt3.length];
        int len = 0;
        System.arraycopy(bt1, 0, bt4, 0, bt1.length);
        len += bt1.length;
        System.arraycopy(bt2, 0, bt4, len, bt2.length);
        len += bt2.length;
        System.arraycopy(bt3, 0, bt4, len, bt3.length);
        return bt4;
    }
}
