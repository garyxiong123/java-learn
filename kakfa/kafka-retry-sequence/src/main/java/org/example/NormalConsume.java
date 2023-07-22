package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class NormalConsume {
    private static final String TOPIC = "test-topic";
    private static AtomicInteger value = new AtomicInteger(0);
    public static void main(String[] args) {
        Properties properties = new Properties();
        //borker地址
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //反序列化方式
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //指定消费者组id，必须
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group");
        //earliest:offset偏移至最早时候开始消费；latest：偏移到从最新开始消费（默认）
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        //每批次最小拉取数据大小,默认1byte
        properties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1);
        //每批次最大拉取数据大小，默认50M
        properties.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 50 * 1024 * 1024);
        //一批次数据，未达到最小数据大小时候，最大等待时间.默认500ms
        properties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 500);
        //单次调用 poll() 返回的最大记录数,默认500
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        KafkaConsumer kafkaConsumer = new KafkaConsumer(properties);
        //A.设置要订阅的topic 列表
        List<TopicPartition> topicPartitions = new ArrayList<>();
        topicPartitions.add(new TopicPartition(TOPIC, 0));
        kafkaConsumer.assign(topicPartitions);

        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(2L));
            for (ConsumerRecord<String, String> record : records) {
                if (record.value().indexOf(String.valueOf(value.get()))==-1){
                    System.out.println("消费数据。topic: " + record.topic() + "|partition:" + record.partition() + "|数据：" + record.value());
                }
                value.incrementAndGet();
            }
            if(records.isEmpty()){
                System.out.println("consumer end!");
            }

        }
    }


}
