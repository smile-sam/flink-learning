package com.ms.flink.redis;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Properties;

/**
 * @description: TODO
 * @author: sam
 * @date: 2020/10/23 13:53
 * @version: v1.0
 */
public class KafkaToRedis implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(KafkaToRedis.class);

    public static void execute() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Checkpoint
        env.enableCheckpointing(5000, CheckpointingMode.AT_LEAST_ONCE);

        // parallelism
        env.setParallelism(1);
        env.setMaxParallelism(1);

        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "hadoop1:9092");
        props.setProperty("group.id", "demoGroup");

        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<String>("test_redis", new SimpleStringSchema(), props);
        kafkaConsumer.setStartFromLatest();

        env.addSource(kafkaConsumer)
                .map(new MapFunction<String, TeacherInfo>() {
                    @Override
                    public TeacherInfo map(String value) throws Exception {
                        logger.info("==========> begin map, value: " + value);
                        TeacherInfo info = new TeacherInfo();
                        String[] arr = null;
                        try {
                            arr = value.split(",");
                            if(arr == null) {
                                return null;
                            }
                            info.setId(Integer.parseInt(arr[0]));
                            info.setName(arr[1]);
                            info.setGender(arr[2]);
                            info.setAge(Integer.parseInt(arr[3]));
                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                        return info;
                    }
                })
                .addSink(new SinkToRedis());
        env.execute();
    }
}