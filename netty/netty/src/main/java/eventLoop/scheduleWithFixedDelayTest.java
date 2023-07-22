package eventLoop;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.nio.NioEventLoop;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class scheduleWithFixedDelayTest {


    public static void main(String[] args) {

        //单线程
        EventExecutorGroup executors = new DefaultEventLoop();

        LinkedBlockingQueue<Runnable> runnables = new LinkedBlockingQueue<>();

        ThreadPoolExecutor executors1 = new ThreadPoolExecutor(
                1, //corePoolSize
                1, //maximumPoolSize
                0, //keepAliveTime
                TimeUnit.MILLISECONDS,
                runnables
        );

        //上次任务开始开始计时
        executors.scheduleWithFixedDelay(() -> {
            System.out.println("Hello world!");
        }, 0, 1, java.util.concurrent.TimeUnit.SECONDS);
    }


    //上次任务结束开始计时
//        executors.schedule(() ->
//        {
//            System.out.println("Hello world!");
//        }, 1, java.util.concurrent.TimeUnit.SECONDS);
//    }
//

}