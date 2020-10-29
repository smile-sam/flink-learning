package com.ms.flink.stream;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class StreamWordCount {

    public static void main(String[] args) throws Exception {
        // 1 设置环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 2. 定义数据
        DataStream<String> text = env.socketTextStream("192.168.253.131", 9999);

        // 3. 处理逻辑
        DataStream<Tuple2<String, Integer>> counts =
                text.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                        //将每行按word拆分
                        String[] tokens = value.toLowerCase().split("\\b+");

                        //收集(类似:map-reduce思路)
                        for (String token : tokens) {
                            if (token.trim().length() > 0) {
                                out.collect(new Tuple2<>(token.trim(), 1));
                            }
                        }
                    }
                })
                        //按Tuple2里的第0项，即：word分组
                        .keyBy(value -> value.f0)
                        //然后对Tuple2里的第1项求合
                        .sum(1);

        // 4. 打印结果
        counts.print();

        // execute program
        env.execute("Streaming WordCount");

    }
}