package org.example.config;

public class KafkaConstants {
    public static final String BROKER_LIST = "10.81.113.239:19092,10.81.113.19:19092,10.81.112.67:19092";
    public static final String LOCAL_BROKER_LIST = "localhost:9092,localhost:9093,localhost:9094";
    public static final String ERROR_BROKER_LIST = "localhost:1092";
    public static final String CLIENT_ID = "client1";
    public static String GROUP_ID_CONFIG="consumerGroup1";
    private KafkaConstants() {

    }
}
