package encode;


import static encode.NettyTestUtil.createClient;
import static encode.NettyTestUtil.createServer;

public class Banding2Core {
    public static void main(String[] args) {

        createServer();
        createClient();


    }

}
