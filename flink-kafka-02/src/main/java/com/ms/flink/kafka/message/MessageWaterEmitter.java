package com.ms.flink.kafka.message;

import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

/**
 * @description: MessageWaterEmitter类（根据Kafka消息确定Flink的水位）
 * @author: sam
 * @date: 2020/10/22 15:37
 * @version: v1.0
 */
public class MessageWaterEmitter implements AssignerWithPunctuatedWatermarks<String> {

    @Nullable
    @Override
    public Watermark checkAndGetNextWatermark(String lastElement, long extractedTimestamp) {
        if (lastElement != null && lastElement.contains(",")) {
            String[] parts = lastElement.split(",");
            return new Watermark(Long.parseLong(parts[0]));
        }
        return null;
    }

    @Override
    public long extractTimestamp(String element, long previousElementTimestamp) {
        if (element != null && element.contains(",")) {
            String[] parts = element.split(",");
            return Long.parseLong(parts[0]);
        }
        return 0L;
    }
}