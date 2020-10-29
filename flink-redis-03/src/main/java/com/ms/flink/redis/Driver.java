package com.ms.flink.redis;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @description: TODO
 * @author: sam
 * @date: 2020/10/23 13:52
 * @version: v1.0
 */
public class Driver implements Serializable {

    public static void main(String[] args) throws Exception {
        // String className = "com.ms.flink.redis.KafkaToRedis";
        // ParameterTool tool = ParameterTool.fromArgs(args);

        try {

        } catch (Exception e) {

        }
        String className = "com.ms.flink.redis.KafkaToRedis";
        Class clazz = Class.forName(className);
        Method method = clazz.getDeclaredMethod("execute");
        method.invoke(clazz.newInstance());
    }
}
