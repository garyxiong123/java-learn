package swift;

import static swift.NettyTestUtil.createClient;
import static swift.NettyTestUtil.createServer;

public class Banding2Core {
    public static void main(String[] args) {

        createServer();
        createClient();


    }

}