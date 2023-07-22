package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.example.config.ProducerCreator;

import java.util.Properties;

public class NormalSendNeedTime {
    private static final String TOPIC = "test-topic";
    private static Producer<String, String> producer = null;

    public static void main(String[] args) throws InterruptedException {
        Properties properties = ProducerCreator.createDefaultProperties();
        properties.put(ProducerConfig.RETRIES_CONFIG, 2);
        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        producer = new KafkaProducer<>(properties);
        run();
    }

    static void run() throws InterruptedException {
        long e = System.currentTimeMillis();
        for (int i = 0; i < 20_000_000; i++) {
            sendMessageAsync(i);
        }
        long t = System.currentTimeMillis();
        producer.flush();
        System.out.println("1 is " + (t-e));
    }



    static void sendMessageAsync(int i) {
        long start = System.currentTimeMillis();
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, "hello, Kafka-" + i);
        try {
            producer.send(record);
        } catch (Exception e) {
            System.out.println("Error in sending record");
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
//        System.out.println("sendMessageAsync time is "+ (end-start));
    }

    static void sendMessageSync(int i) {
        long start = System.currentTimeMillis();
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, "hello, Kafka-" + i);
        try {
            RecordMetadata metadata = producer.send(record).get();
//            System.out.println("Record sent to partition " + metadata.partition() + " with offset " + metadata.offset());

        } catch (Exception e) {
            System.out.println("Error in sending record");
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("sendMessageSync time is "+ (end-start));
    }
}
