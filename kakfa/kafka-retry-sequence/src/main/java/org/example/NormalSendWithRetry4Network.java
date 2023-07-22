package org.example;

import org.apache.kafka.clients.producer.*;
import org.example.config.KafkaConstants;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static org.example.config.ProducerCreator.createDefaultProperties;

public class NormalSendWithRetry4Network {
    private static final String TOPIC = "test-topic";

    public static void main(String[] args) {
        sendMessage();
    }

    static void sendMessage() {
        Properties properties = createDefaultProperties();
        properties.setProperty("acks", "0");
        properties.setProperty("retries", "3");
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1");
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.ERROR_BROKER_LIST);
        Producer<String, String> producer = new KafkaProducer<>(properties);

        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, "hello, Kafka11!");
        try {
            //send message
            RecordMetadata metadata = producer.send(record).get();

            System.out.println("Record sent to partition " + metadata.partition() + " with offset " + metadata.offset());

        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Error in sending record");
            e.printStackTrace();
        }
        producer.close();
    }

}
