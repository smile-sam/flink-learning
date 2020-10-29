package com.ms.flink.kafka.job;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.api.common.serialization.SimpleStringSchema;

import java.util.Properties;

public class Demo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(5000); // 要设置启动检查点
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "hadoop1:9092");
        props.setProperty("group.id", "flink-group");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        DataStream input = env.addSource(new FlinkKafkaConsumer("test_topic",
                new SimpleStringSchema(), props));
        input.print();

        FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<String>("hadoop1:9092",
                "test_source", new SimpleStringSchema());
        input.map(line -> line + "test").addSink(myProducer);
        env.execute("Flink Kafka Demo");
    }
}
