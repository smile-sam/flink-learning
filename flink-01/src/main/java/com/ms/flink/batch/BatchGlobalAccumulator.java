package com.ms.flink.batch;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.configuration.Configuration;


public class BatchGlobalAccumulator {

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> dataSource = env.fromElements("a", "b", "", "d", "e");

        DataSet<String> result = dataSource.map(new RichMapFunction<String, String>() {

            private IntCounter numLines = new IntCounter();

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                getRuntimeContext().addAccumulator("num-lines", this.numLines);
            }

            @Override
            public String map(String s) throws Exception {
                this.numLines.add(1);
                return s;
            }
        }).setParallelism(8);
        result.writeAsText("d:\\data\\count10");
        JobExecutionResult jobExecutionResult = env.execute("counter");
        int num = jobExecutionResult.getAccumulatorResult("num-lines");
        System.out.println("num:" + num);


    }
}
