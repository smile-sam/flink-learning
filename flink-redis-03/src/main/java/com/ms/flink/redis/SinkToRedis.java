package com.ms.flink.redis;

import com.google.gson.Gson;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * @description: TODO
 * @author: sam
 * @date: 2020/10/23 13:55
 * @version: v1.0
 */
public class SinkToRedis extends RichSinkFunction<TeacherInfo> {

    private static final Logger logger = LoggerFactory.getLogger(SinkToRedis.class);

    private Jedis jedis = null;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        jedis = RedisUtil.getResource();
        logger.info("==========> open jedis: " + jedis.toString());
    }

    @Override
    public void invoke(TeacherInfo value, Context context) throws Exception {
        jedis.set(value.getId() + "", new Gson().toJson(value));
        logger.info("==========> set value to redis: " + value.toString());
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (null != jedis) {
            logger.info("==========> close jedis: " + jedis.toString());
            jedis.close();
        }
    }
}