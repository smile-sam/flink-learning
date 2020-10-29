package com.ms.flink.kafka.util;

import com.google.gson.Gson;
import com.ms.flink.kafka.domain.SingleMessage;

/**
 * 解析原始消息的辅助类
 */
public class JSONHelper {

    /**
     * 解析消息，得到时间字段
     * @param raw
     * @return
     */
    public static long getTimeLongFromRawMessage(String raw){
        SingleMessage singleMessage = parse(raw);
        return null==singleMessage ? 0L : singleMessage.getTimeLong();
    }

    /**
     * 将消息解析成对象
     * @param raw
     * @return
     */
    public static SingleMessage parse(String raw){
        SingleMessage singleMessage = null;
        if (raw != null) {
            Gson gson = new Gson();
            singleMessage = gson.fromJson(raw, SingleMessage.class);
        }

        return singleMessage;
    }
}
