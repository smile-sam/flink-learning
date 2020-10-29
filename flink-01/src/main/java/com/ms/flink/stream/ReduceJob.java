package com.ms.flink.stream;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @description: TODO
 * @author: sam
 * @date: 2020/10/23 11:47
 * @version: v1.0
 */
public class ReduceJob {
    private static final Logger LOG = LoggerFactory.getLogger(ReduceJob.class);
    private static final String[] TYPE = {"苹果", "梨", "西瓜", "葡萄", "火龙果"};

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //添加自定义数据源,每秒发出一笔订单信息{商品名称,商品数量}
        DataStreamSource<Tuple2<String, Integer>> orderSource = env.addSource(new SourceFunction<Tuple2<String, Integer>>() {
            private volatile boolean isRunning = true;
            private final Random random = new Random();

            @Override
            public void run(SourceFunction.SourceContext<Tuple2<String, Integer>> ctx) throws Exception {
                while (isRunning) {
                    TimeUnit.SECONDS.sleep(1);
                    ctx.collect(Tuple2.of(TYPE[random.nextInt(TYPE.length)], 1));
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }

        }, "order-info");

        orderSource.keyBy(0)
                //将上一元素与当前元素相加后，返回给下一元素处理
                .reduce(new ReduceFunction<Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2)
                            throws Exception {
                        return Tuple2.of(value1.f0, value1.f1 + value2.f1);
                    }
                })
                .print();

        env.execute("Flink Streaming Java API Skeleton");
    }
}
