package com.ms.flink.kafka.job;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;

/**
 * Skeleton for a Flink Streaming Job.
 *
 * <p>For a tutorial how to write a Flink streaming application, check the
 * tutorials and examples on the <a href="http://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
public class StreamingJob {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(5000); // 要设置启动检查点
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "hadoop1:9092");
        props.setProperty("group.id", "flink-group");

        FlinkKafkaConsumer consumer = new FlinkKafkaConsumer("test_topic",
                new org.apache.flink.api.common.serialization.SimpleStringSchema(), props);


        /*
        所有版本的Flink Kafka consumer都可以使用上面的方法来设定起始位移。
        setStartFromGroupOffsets：这是默认情况，即从消费者组提交到Kafka broker上的位移开始读取分区数据（对于老版本而言，位移是提交到Zookeeper上）。
        如果未找到位移，使用auto.offset.reset属性值来决定位移。该属性默认是LATEST，即从最新的消息位移处开始消费
        setStartFromEarliest() / setStartFromLatest()：设置从最早/最新位移处开始消费。使用这两个方法的话，Kafka中提交的位移就将会被忽略而不会被用作起始位移
         */

//
//        consumer.setStartFromEarliest();
//        consumer.setStartFromLatest();
//        consumer.setStartFromGroupOffsets();

        // 自定义位移

//        Map<KafkaTopicPartition, Long> specificStartOffsets = new HashMap<>();
//        specificStartOffsets.put(new KafkaTopicPartition("myTopic", 0), 23L);
//        specificStartOffsets.put(new KafkaTopicPartition("myTopic", 1), 31L);
//        specificStartOffsets.put(new KafkaTopicPartition("myTopic", 2), 43L);
//
//        myConsumer.setStartFromSpecificOffsets(specificStartOffsets);
        DataStream input = env.addSource(consumer);

        input.print();

        FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<String>("hadoop1:9092",
                "test_source",new org.apache.flink.api.common.serialization.SimpleStringSchema());
        input.map(line -> line + "test").addSink(myProducer);
        env.execute("Flink Kafka Demo");




//        //数据源配置，是一个kafka消息的消费者
//        FlinkKafkaConsumer<String> consumer =
//                new FlinkKafkaConsumer<>("topic001", new SimpleStringSchema(), props);
//
//        //增加时间水位设置类
//        consumer.assignTimestampsAndWatermarks(new AssignerWithPunctuatedWatermarks<String> (){
//            @Override
//            public long extractTimestamp(String element, long previousElementTimestamp) {
//                return JSONHelper.getTimeLongFromRawMessage(element);
//            }
//
//            @Nullable
//            @Override
//            public Watermark checkAndGetNextWatermark(String lastElement, long extractedTimestamp) {
//                if (lastElement != null) {
//                    return new Watermark(JSONHelper.getTimeLongFromRawMessage(lastElement));
//                }
//                return null;
//            }
//        });
//
//        env.addSource(consumer)
//                //将原始消息转成Tuple2对象，保留用户名称和访问次数(每个消息访问次数为1)
//                .flatMap((FlatMapFunction<String, Tuple2<String, Long>>) (s, collector) -> {
//                    SingleMessage singleMessage = JSONHelper.parse(s);
//
//                    if (null != singleMessage) {
//                        collector.collect(new Tuple2<>(singleMessage.getName(), 1L));
//                    }
//                })
//                //以用户名为key
//                .keyBy(0)
//                //时间窗口为2秒
//                .timeWindow(Time.seconds(2))
//                //将每个用户访问次数累加起来
//                .apply((WindowFunction<Tuple2<String, Long>, Tuple2<String, Long>, Tuple, TimeWindow>) (tuple, window, input, out) -> {
//                    long sum = 0L;
//                    for (Tuple2<String, Long> record: input) {
//                        sum += record.f1;
//                    }
//
//                    Tuple2<String, Long> result = input.iterator().next();
//                    result.f1 = sum;
//                    out.collect(result);
//                })
//                //输出方式是STDOUT
//                .print();
//
//        env.execute("Flink-Kafka demo");
    }
}
