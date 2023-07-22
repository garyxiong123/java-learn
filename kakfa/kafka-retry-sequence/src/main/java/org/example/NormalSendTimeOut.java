package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.config.ProducerCreator;

import java.util.Properties;

public class NormalSendTimeOut {
    private static final String TOPIC = "test-topic";
    private static Producer<String, String> producer = null;

    public static void main(String[] args) {

        long s = System.currentTimeMillis();
        Properties properties = ProducerCreator.createDefaultProperties();
        properties.put(ProducerConfig.RETRIES_CONFIG, 2);
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 1);
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        producer = new KafkaProducer<>(properties);
        for (int i = 0; i < 2; i++) {
            sendMessage(i);
        }
        producer.flush();
        long e = System.currentTimeMillis();
        System.out.println(e-s);
    }

    static void sendMessage(int i) {
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, "hello, Kafka-" + i);
        try {
            producer.send(record);
//            RecordMetadata metadata = producer.send(record).get();
//
//            System.out.println("Record sent to partition " + metadata.partition() + " with offset " + metadata.offset());

        } catch (Exception e) {
            System.out.println("Error in sending record");
            e.printStackTrace();
        }
    }

}
