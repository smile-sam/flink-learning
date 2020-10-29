package com.ms.flink.batch;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatchDemoBroadcast {

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        ArrayList<Tuple2<String, Integer>> broadData = new ArrayList<>();

        broadData.add(new Tuple2<>("zs", 18));
        broadData.add(new Tuple2<>("ls", 20));
        broadData.add(new Tuple2<>("ww", 19));
        DataSet<Tuple2<String, Integer>> tuple2DataSet = env.fromCollection(broadData);

        DataSet<HashMap<String, Integer>> toBroadcast = tuple2DataSet.map(new MapFunction<Tuple2<String, Integer>, HashMap<String, Integer>>() {
            @Override
            public HashMap<String, Integer> map(Tuple2<String, Integer> value) throws Exception {
                HashMap<String, Integer> res = new HashMap<>();
                res.put(value.f0, value.f1);
                return res;
            }
        });

        DataSource<String> dataSource = env.fromElements("zs", "ls", "ww");
        DataSet<String> result = dataSource.map(new RichMapFunction<String, String>() {
          List<HashMap<String, Integer>> broadCastMap = new ArrayList<HashMap<String, Integer>>();
          HashMap<String, Integer> allMap = new HashMap<>();

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);

                this.broadCastMap = getRuntimeContext().getBroadcastVariable("broadCastMapName");
                for(HashMap map : broadCastMap) {
                    allMap.putAll(map);
                }
            }

            @Override
            public String map(String value) throws Exception {
                Integer age = allMap.get(value);
                return value +"," + age;
            }
        }).withBroadcastSet(toBroadcast, "broadCastMapName");

       result.print();

    }
}
