package com.ms.flink.kafka.consumer;

import com.ms.flink.config.KafkaConstantProperties;
import com.ms.flink.kafka.formatter.TestBizDataLineSplitter;
import com.ms.flink.kafka.serializer.KafkaTuple4StringSchema;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 用java 写消费者
 *
 */
public class ConsumeKafkaByJava {

    private static final String CONSUMER_GROUP_ID = "test.flink.consumer1";

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.enableCheckpointing(1000);

        Properties kafkaProps = new Properties();
        kafkaProps.setProperty("bootstrap.servers", KafkaConstantProperties.KAFKA_BROKER);
        kafkaProps.setProperty("group.id", CONSUMER_GROUP_ID);

        FlinkKafkaConsumer<String> myConsumer = new FlinkKafkaConsumer<String>(
                KafkaConstantProperties.FLINK_COMPUTE_TOPIC_IN1,
                new SimpleStringSchema(),
                kafkaProps);

        DataStream<String> dataStream = env.addSource(myConsumer);
        // 四元组数据为: 订单号,统计维度标识,订单数,订单金额
        DataStream<Tuple4<String, String, Integer, Double>> counts = dataStream
                .flatMap(new TestBizDataLineSplitter())
                .keyBy(1)
                .timeWindow(Time.of(30, TimeUnit.SECONDS))
                .reduce((value1, value2) -> {
                    return new Tuple4<>(value1.f0, value1.f1, value1.f2 + value2.f2, value1.f3 + value2.f3);
                });

        // 暂时输入与输出相同
        counts.addSink(new FlinkKafkaProducer<>(
                KafkaConstantProperties.FLINK_DATA_SINK_TOPIC_OUT1,
                new KafkaTuple4StringSchema(),
                kafkaProps)
        );
        // 统计值多向输出
        dataStream.print();
        counts.print();
        env.execute("Test Count from Kafka data");
    }

}