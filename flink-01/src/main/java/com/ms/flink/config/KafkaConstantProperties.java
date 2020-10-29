package com.ms.flink.config;

    /**
     * kafka 相关常量定义
     *
     */
    public class KafkaConstantProperties {

        /**
         * kafka broker 地址
         */
        public static final String KAFKA_BROKER = "127.0.0.1:9092";

        /**
         * zk 地址，低版本 kafka 使用，高版本已丢弃
         */
        public static final String ZOOKEEPER_HOST = "master:2181,slave1:2181,slave2:2181";

        /**
         * flink 计算使用topic 1
         */
        public static final String FLINK_COMPUTE_TOPIC_IN1 = "mastertest";

        /**
         * flink消费结果，输出到kafka, topic 数据
         */
        public static final String FLINK_DATA_SINK_TOPIC_OUT1 = "flink_compute_result_out1";

    }
