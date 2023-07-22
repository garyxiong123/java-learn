package org.example;

import com.binance.nextme.api.common.util.TimeUtil;
import com.binance.nextme.messages.protobuf.KafkaMessage;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("acks", "all");
        properties.setProperty("retries", "0");
        properties.setProperty("batch.size", "16384");
        properties.setProperty("auto.commit.interval.ms", "10");
        properties.setProperty("linger.ms", "10");
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        properties.setProperty("block.on.buffer.full", "true");
        properties.setProperty("compression.type", "snappy");

        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(properties);

        KafkaMessage kafkaMessage = KafkaMessage.newBuilder()
                .setSeqNum(1L)
                .setSourceSeqNum(1L)
                .setSourceSendTime(TimeUtil.getNanoTime())
                .setSourceKafkaOffset(100L)
                .setSendTime(TimeUtil.getNanoTime())
                .setInputTime(TimeUtil.getNanoTime())
                .setStartMatchTime(TimeUtil.getNanoTime())
                .setSenderInstanceId("0")
                .setTransactionId(10000L)
                .build();

        ProducerRecord<String, byte[]> record =  new ProducerRecord<>("me_test", 0, null, kafkaMessage.toByteArray());
        producer.send(record);
    }
}