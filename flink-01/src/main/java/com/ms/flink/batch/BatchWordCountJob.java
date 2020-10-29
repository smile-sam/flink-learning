package com.ms.flink.batch;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.Collector;

public class BatchWordCountJob {
    public static void main(String[] args) throws Exception {


        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        String wordFilePath = "D:\\data\\word.txt";

        DataSet<String> text = env.readTextFile(wordFilePath);

        DataSet<Tuple2<String,Integer>> counts = text.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String [] tokens = s.toLowerCase().split("\\W+");
                for(String token : tokens) {
                    collector.collect(new Tuple2<String, Integer>(token, 1));
                }
            }
        }).groupBy(0,1).sum(1);
        counts.print();
    }
}
