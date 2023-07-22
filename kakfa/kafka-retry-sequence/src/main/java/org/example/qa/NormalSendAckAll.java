package org.example.qa;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.config.ProducerCreator;

import java.util.Properties;

public class NormalSendAckAll {
    private static String topic;
    private static Producer<String, String> producer = null;
    private static Long start;
    private static Integer count;
    private static Boolean endFlag = false;


    public static void main(String[] args) throws InterruptedException {
        Properties properties = ProducerCreator.createQaProperties();
        properties.put(ProducerConfig.RETRIES_CONFIG, 0);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 0);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        producer = new KafkaProducer<>(properties);
        topic = args[0];
        count = Integer.valueOf(args[1]);
        for (String arg : args) {
            System.out.println(arg);
        }
        run();

        while (!endFlag) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Exception fill order sleep");
            }
        }
        System.out.println("task end");

    }

    static void run() {
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            sendMessageAsync(i);
        }
        Long run = System.currentTimeMillis();
        System.out.println("run time is " + (run - start) + "ms");
    }


    static void sendMessageAsync(int i) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "hello, Kafka-" + i);
        try {
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    System.out.println("消息发送失败," + exception.getMessage());
                    exception.printStackTrace();
                } else {
                    if (i == count - 1) {
                        long end = System.currentTimeMillis();
                        System.out.println("callback time is " + (end - start) + "ms");
                        endFlag = true;
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("Error in sending record," + e.getMessage());
        }
    }

}
