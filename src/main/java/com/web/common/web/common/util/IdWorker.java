package com.web.common.web.common.util;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 42位的时间前缀+10位的节点标识+12位的sequence避免并发的数字（12位不够用时强制得到新的时间前缀）
 * <p>
 * <b>对系统时间的依赖性非常强，需要关闭ntp的时间同步功能，或者当检测到ntp时间调整后，拒绝分配id。
 *
 * @author sumory.wu
 * @date 2012-2-26 下午6:40:28
 */
public class IdWorker {
    private final static long twepoch = 1288834974657L;
    // 机器标识位数
    private final static long workerIdBits = 5L;
    // 数据中心标识位数
    private final static long datacenterIdBits = 5L;
    // 机器ID最大值,31
    private final static long maxWorkerId = -1L ^ (-1L << workerIdBits);
    // 数据中心ID最大值,31
    private final static long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    // 毫秒内自增位,sequence值控制在0-4095
    private final static long sequenceBits = 12L;
    // 机器ID偏左移12位
    private final static long workerIdShift = sequenceBits;
    // 数据中心ID左移17位
    private final static long datacenterIdShift = sequenceBits + workerIdBits;
    // 时间毫秒左移22位
    private final static long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    //sequence值控制在0-4095
    private final static long sequenceMask = -1L ^ (-1L << sequenceBits);

    private static long lastTimestamp = -1L;

    private AtomicLong sequence = new AtomicLong(0L);
    private final long workerId;
    private final long datacenterId;

    public IdWorker(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException("worker Id can't be greater than %d or less than 0");
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(
                "datacenter Id can't be greater than %d or less than 0");
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId() {
        long timestamp = timeGen();
        if (lastTimestamp == timestamp) {
            // 当前毫秒内，则+1
            sequence.set((sequence.get() + 1) & sequenceMask);
            if (sequence.longValue() == 0) {
                // 当前毫秒内计数满了，即到了4096，则等待下一秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence.set(0);
            ;
        }
        if (timestamp < lastTimestamp) {
            try {
                throw new Exception(
                    "Clock moved backwards.  Refusing to generate id for " + (lastTimestamp
                        - timestamp) + " milliseconds");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        lastTimestamp = timestamp;
        // ID偏移组合生成最终的ID，并返回ID
        long nextId =
            ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (
                workerId << workerIdShift) | sequence.get();

        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        System.out.println(0 & 1000);
        IdWorker test = new IdWorker(4, 10);
        //    System.out.println(Long.toBinaryString(4095));
        test.test2();
    }

    public void test2() {
        final IdWorker w = new IdWorker(1, 2);
        final CyclicBarrier cdl = new CyclicBarrier(100);

        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override public void run() {
                    try {
                        cdl.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    System.out.println(w.nextId());
                }
            }).start();
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
