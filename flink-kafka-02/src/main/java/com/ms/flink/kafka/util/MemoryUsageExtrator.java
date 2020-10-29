package com.ms.flink.kafka.util;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;


/**
 * @description: TODO
 * @author: sam
 * @date: 2020/10/22 15:39
 * @version: v1.0
 */
public class MemoryUsageExtrator {

    private static OperatingSystemMXBean mxBean =
            (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    /**
     * Get current free memory size in bytes
     * @return  free RAM size
     */
    public static long currentFreeMemorySizeInBytes() {
        return mxBean.getFreePhysicalMemorySize();
    }
}