package cost;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;


/**
 * Sure! Here's a simple example of a Java Netty server and client that can communicate with each other:
 */
public class String2Bytes {

    static String data = "{\n" +
            "  \"e\": \"depthUpdate\", // Event type\n" +
            "  \"E\": 1571889248277, // Event time\n" +
            "  \"T\": 1571889248276, // Transaction time\n" +
            "  \"s\": \"BTCUSDT\",\n" +
            "  \"U\": 390497796,\n" +
            "  \"u\": 390497878,\n" +
            "  \"pu\": 390497794,\n" +
            "  \"b\": [          // Bids to be updated\n" +
            "    [\n" +
            "      \"7403.89\",  // Price Level to be\n" +
            "      \"0.002\"     // Quantity\n" +
            "    ],\n" +
            "    [\n" +
            "      \"7403.90\",\n" +
            "      \"3.906\"\n" +
            "    ],\n" +
            "    [\n" +
            "      \"7404.00\",\n" +
            "      \"1.428\"\n" +
            "    ],\n" +
            "    [\n" +
            "      \"7404.85\",\n" +
            "      \"5.239\"\n" +
            "    ],\n" +
            "    [\n" +
            "      \"7405.43\",\n" +
            "      \"2.562\"\n" +
            "    ]\n" +
            "  ],\n" +
            "  \"a\": [          // Asks to be updated\n" +
            "    [\n" +
            "      \"7405.96\",  // Price level to be\n" +
            "      \"3.340\"     // Quantity\n" +
            "    ],\n" +
            "    [\n" +
            "      \"7406.63\",\n" +
            "      \"4.525\"\n" +
            "    ],\n" +
            "    [\n" +
            "      \"7407.08\",\n" +
            "      \"2.475\"\n" +
            "    ],\n" +
            "    [\n" +
            "      \"7407.15\",\n" +
            "      \"4.800\"\n" +
            "    ],\n" +
            "    [\n" +
            "      \"7407.20\",\n" +
            "      \"0.175\"\n" +
            "    ]\n" +
            "  ]\n" +
            "}";
    public static void main(String[] args) throws InterruptedException {
        original();
        constString();
        byteConcat();
//        wrappedBufferConcat();
    }

    static void original() {
//        String data = "1688980557117000 3042 382 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        int i = 5000;
        long sum = 0;
        while (i > 0) {
            i--;
            long start = System.nanoTime();

            String str = "{\"stream\":\"rerere\",\"data\":" + new String(bytes) + ")}}";
            str.getBytes(StandardCharsets.UTF_8);
//            ByteBuf compressedByteBufWithCombined = Unpooled.wrappedBuffer(str.getBytes(StandardCharsets.UTF_8));
//            ByteBufUtil.getBytes(compressedByteBufWithCombined);

            long end = System.nanoTime();

//            System.out.println((end - start) / 1000);

            sum += end - start;
        }
        System.out.println("original: "+(sum) / 1000 / 5000+" us");
    }

    static void constString() {

        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);

        String con = "{\"stream\":\"rerere\",\"data\":";
        String con1 = "\")}}\"";


        int i = 5000;
        long sum = 0;
        while (i > 0) {
            i--;
            long start = System.nanoTime();

            String str = con + new String(bytes) + con1;
            str.getBytes(StandardCharsets.UTF_8);
//            ByteBuf compressedByteBufWithCombined = Unpooled.wrappedBuffer(str.getBytes(StandardCharsets.UTF_8));
//            ByteBufUtil.getBytes(compressedByteBufWithCombined);

            long end = System.nanoTime();

//            System.out.println((end - start) / 1000);

            sum += end - start;
        }
        System.out.println("constString: "+(sum) / 1000 / 5000+" us");
    }

    static void byteConcat() {
//        String data = "1688980557117000 3042 382 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);

        String con = "{\"stream\":\"rerere\",\"data\":";
        String con1 = "\")}}\"";

        byte[] first = con.getBytes(StandardCharsets.UTF_8);
        byte[] last = con1.getBytes(StandardCharsets.UTF_8);


        int i = 5000;
        long sum = 0;
        while (i > 0) {
            i--;
            long start = System.nanoTime();
            byteConcat(first, bytes, last);
        //            ByteBufUtil.getBytes(Unpooled.wrappedBuffer(byteConcat(first, bytes, last)));

            long end = System.nanoTime();

            System.out.println((end - start) / 1000);

            sum += end - start;
        }
        System.out.println("byteConcat: "+(sum) / 1000 / 5000+" us");
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
