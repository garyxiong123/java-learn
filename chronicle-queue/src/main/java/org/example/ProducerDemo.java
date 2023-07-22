package org.example;

import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;

/*
* This is a simple producer that writes 10 messages to a queue.
* 接下来，让我们创建一个生产者和一个消费者，以演示Chronicle Queue的用法。

生产者示例：
    */
public class ProducerDemo {
    public static void main(String[] args) {
        try (ChronicleQueue queue = ChronicleQueue.singleBuilder(ConsumerDemo.path).build()) {
            ExcerptAppender appender = queue.acquireAppender();

            for (int i = 0; i < 1000; i++) {
                String message = "Message " + i;
                appender.writeText(message);
                System.out.println("Produced: " + message);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
