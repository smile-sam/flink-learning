//package com.ms.flink.stream;
//
//import org.apache.flink.api.java.tuple.Tuple2;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.connectors.redis.RedisSink;
//import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
//import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
//import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
//import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
//
///**
// * @description: TODO
// * @author: sam
// * @date: 2020/10/23 13:44
// * @version: v1.0
// */
//public class RedisDemo {
//
//    public static void main(String[] args) {
//
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder().setHost("127.0.0.1").build();
//
//        DataStream<String> stream = env.addSource();
//        stream.addSink(new RedisSink<Tuple2<String, String>>(conf, new RedisExampleMapper());
//    }
////    使用 Redis Cluster：
////    FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder()
////            .setNodes(new HashSet<InetSocketAddress>(Arrays.asList(new InetSocketAddress(5601)))).build();
////
////    DataStream<String> stream = ...;
////stream.addSink(new RedisSink<Tuple2<String, String>>(conf, new RedisExampleMapper());
//
//
////    使用 Redis Sentinel：
////
////    FlinkJedisSentinelConfig conf = new FlinkJedisSentinelConfig.Builder()
////            .setMasterName("master").setSentinels(...).build();
////
////    DataStream<String> stream = ...;
////stream.addSink(new RedisSink<Tuple2<String, String>>(conf, new RedisExampleMapper());
//
//    public static class RedisExampleMapper implements RedisMapper<Tuple2<String, String>> {
//
//        @Override
//        public RedisCommandDescription getCommandDescription() {
//            return new RedisCommandDescription(RedisCommand.HSET, "HASH_NAME");
//        }
//
//        @Override
//        public String getKeyFromData(Tuple2<String, String> data) {
//            return data.f0;
//        }
//
//        @Override
//        public String getValueFromData(Tuple2<String, String> data) {
//            return data.f1;
//        }
//    }
//}
