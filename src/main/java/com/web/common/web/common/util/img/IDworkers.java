package com.web.common.web.common.util.img;

/**
 * @author www.zuidaima.com
 **/
public class IDworkers {
    private final long workerId;
    private final static long twepoch = 1288834974657L;// 起始标记点，作为基准
    private long sequence = 0L;// 0，并发控制
    private final static long workerIdBits = 5L;// 只允许workid的范围为：0-1023
    private final static long maxWorkerId = -1L ^ -1L << workerIdBits;// 1023,1111111111,10位
    private final static long sequenceBits = 12L;// sequence值控制在0-4095

    private final static long workerIdShift = sequenceBits;
    private final static long timestampLeftShift = sequenceBits + workerIdBits;//22
    private final static long sequenceMask = -1L ^ -1L << sequenceBits;

    private long lastTimestamp = -1L;

    public IDworkers(final long workerId) {
        super();
        if (workerId > IDworkers.maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String
                .format("worker Id can't be greater than %d or less than 0",
                    IDworkers.maxWorkerId));
        }
        this.workerId = workerId;
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) {
            // 如果上一个timestamp与新产生的相等，则sequence加一(0-4095循环)，下次再使用时sequence是新值
            this.sequence = (this.sequence + 1) & IDworkers.sequenceMask;
            if (this.sequence == 0) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }
        if (timestamp < this.lastTimestamp) {
            try {
                throw new Exception(String
                    .format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                        this.lastTimestamp - timestamp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.lastTimestamp = timestamp;
        long nextId = ((timestamp - twepoch << timestampLeftShift)) | (this.workerId
            << IDworkers.workerIdShift) | (this.sequence);
        System.out.println(
            "timestamp:" + timestamp + ",timestampLeftShift:" + timestampLeftShift + ",nextId:"
                + nextId + ",workerId:" + workerId + ",sequence:" + sequence);
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
        IDworkers worker2 = new IDworkers(2);
        System.out.println(worker2.nextId());
        System.out.println(Long.toBinaryString(707124991702867968L));
        System.out.println(Long.toBinaryString(1457426710426l));
        System.out.println(Long.toBinaryString(2L));
        System.out.println(Long.toBinaryString(0L));
    }

}
