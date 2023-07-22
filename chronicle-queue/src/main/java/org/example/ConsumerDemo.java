package org.example;

import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptTailer;

/**
 * 在上述代码中，我们创建了一个ExcerptTailer实例，用于从队列中读取消息。在无限循环中，我们尝试读取文本消息并将其打印到控制台。然后，我们使用Thread.sleep(1000)进行简单的暂停，以便在没有新消息时降低CPU使用率。
 * <p>
 * 确保将代码中的"path/to/queue-dir"替换为实际的队列目录路径，该路径将用于存储Chronicle Queue的数据文件。
 * <p>
 * 这是一个简单的示例，展示了如何使用Chronicle Queue进行基本的生产者-消费者通信。您可以根据需要扩展此示例，以满足您的需求。
 */
public class ConsumerDemo {
    public static String path = "/Users/gary/Documents/code/learn/java/daily-test/chronicle-queue/src/main/java/org/example";

    public static void main(String[] args) {

        try (ChronicleQueue queue = ChronicleQueue.singleBuilder(path).build()) {
            ExcerptTailer tailer = queue.createTailer();

            while (true) {
//                if (tailer.readText(System.out)) {
                StringBuffer sb = new StringBuffer();
//                    if (tailer.readText()) {

                System.out.println("Consumed: " + tailer.readText());
//                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
