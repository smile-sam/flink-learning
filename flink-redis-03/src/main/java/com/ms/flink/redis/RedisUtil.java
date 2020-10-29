package com.ms.flink.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @description: TODO
 * @author: sam
 * @date: 2020/10/23 13:55
 * @version: v1.0
 */
public class RedisUtil {

    private static volatile JedisPool jedisPool = null;

    public static Jedis getResource() {
        if (null == jedisPool) {
            synchronized (RedisUtil.class) {
                if (null == jedisPool) {
                    JedisPoolConfig config = new JedisPoolConfig();
                    config.setMaxTotal(4);
                    config.setMaxIdle(1);
                    config.setMaxWaitMillis(100);
                    config.setTestOnBorrow(true);
                    jedisPool = new JedisPool(config, "localhost", 6379);
                }
            }
        }
        return jedisPool.getResource();
    }

}