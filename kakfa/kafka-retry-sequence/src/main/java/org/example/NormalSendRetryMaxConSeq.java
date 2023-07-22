package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.config.ProducerCreator;

import java.util.Properties;

public class NormalSendRetryMaxConSeq {
    private static final String TOPIC = "test-topic";
    private static Producer<String, String> producer = null;

    public static void main(String[] args) throws InterruptedException {
        Properties properties = ProducerCreator.createDefaultProperties();
        properties.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
//        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        producer = new KafkaProducer<>(properties);
        run();
    }

    static void run() {
        long e = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            sendMessageAsync(i);
        }
        long t = System.currentTimeMillis();
        System.out.println(t - e);
        producer.flush();
    }


    static void sendMessageAsync(int i) {
        long start = System.currentTimeMillis();
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, "hello, Kafka-" + i);
        try {
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    System.err.println("消息发送失败。");
                    exception.printStackTrace();
                } else {
                    System.out.println("消息发送成功。");
                    System.out.println("topic = " + metadata.topic());
                    System.out.println("partition = " + metadata.partition());
                    System.out.println("offset = " + metadata.offset());
                    long end = System.currentTimeMillis();
                    System.out.println("sendMessageAsync time is " +(end - start)+"num is "+ i);
                }
            });
        } catch (Exception e) {
            System.out.println("Error in sending record");
            e.printStackTrace();
        }
        System.out.println("message num is "+ i);

    }

}
