//package com.ms.flink.kafka;
//
//import com.ms.flink.config.KafkaConstantProperties;
//import com.ms.flink.kafka.serializer.KafkaTuple4StringSchema;
//import org.apache.flink.api.java.tuple.Tuple4;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.api.windowing.time.Time;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
//import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
//
//import java.util.Properties;
//import java.util.concurrent.TimeUnit;
//
//public class Main {
//    private static final String CONSUMER_GROUP_ID = "test.flink.consumer1";
//    public static void main(String[] args) {
//
//
//        // 1. 获取运行环境
//        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//
//        // 2. 配置接入数据源
//        Properties kafkaProps = new Properties();
//        kafkaProps.setProperty("bootstrap.servers", KafkaConstantProperties.KAFKA_BROKER);
//        kafkaProps.setProperty("group.id", CONSUMER_GROUP_ID);
//
//        FlinkKafkaConsumer<String> myConsumer = new FlinkKafkaConsumer<String>(
//                KafkaConstantProperties.FLINK_COMPUTE_TOPIC_IN1,
//                new SimpleStringSchema(),
//                kafkaProps);
//        DataStream<String> dataStream = env.addSource(myConsumer);
//
//        // 3. 处理数据
//        DataStream<Tuple4<String, String, Integer, Double>> counts = dataStream
//                .flatMap(new ProposalBizDataLineSplitter())
//                .keyBy(1)
//                .timeWindow(Time.of(30, TimeUnit.SECONDS))
//                .reduce((value1, value2) -> {
//                    return new Tuple4<>(value1.f0, value1.f1, value1.f2 + value2.f2, value1.f3 + value2.f3);
//                });
//
//        // 4. 输出处理结果
//        counts.addSink(new FlinkKafkaProducer<>(
//                KafkaConstantProperties.FLINK_DATA_SINK_TOPIC_OUT1,
//                new KafkaTuple4StringSchema(),
//                kafkaProps)
//        );
//        // 统计值多向输出
//        dataStream.print();
//        counts.print();
//
//        // 5. 正式提交运行
//        env.execute("Test Count from Kafka data");
//    }
//}
