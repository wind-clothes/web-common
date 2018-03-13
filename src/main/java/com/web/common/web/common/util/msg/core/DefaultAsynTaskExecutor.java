package com.web.common.web.common.util.msg.core;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.web.common.web.common.util.msg.AsynTaskExecutor;
import com.web.common.web.common.util.msg.AsynTaskResultHandler;
import com.web.common.web.common.util.msg.data.AsynTaskContent;
import com.web.common.web.common.util.msg.data.FutureContext;

/**
 * @author: xiongchengwei
 * @date:2016年2月18日 下午4:35:25
 * @since 1.0.0
 */
public class DefaultAsynTaskExecutor implements AsynTaskExecutor {

    private static Logger logger = LoggerFactory.getLogger(DefaultAsynTaskExecutor.class);

    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors() + 2;
        // 线程池大小参考操作系统可用处理器

    private static final long KEEP_ALIVE_TIME = 0L; // 空闲线程在接收新任务的等待时长

    private static final long KEEP_ALIVE_TIME_SYNCHRONOUS = 30000L; // 空闲线程在接收新任务的等待时长

    private static LinkedBlockingQueue<Runnable> excuteQueue =
        new LinkedBlockingQueue<Runnable>(POOL_SIZE * 1000);

    private static LinkedBlockingQueue<Runnable> excuteQueueSequence =
        new LinkedBlockingQueue<Runnable>(POOL_SIZE * 1000);

    // 直接提交队列
    private static SynchronousQueue<Runnable> excuteSynchronousQueueSequence =
        new SynchronousQueue<Runnable>();

    // 使用无界队列，可能会存在内存资源耗尽的可能性
    private static ExecutorService executorService =
        new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
            excuteQueue);

    private static ExecutorService executorServiceSequence =
        new ThreadPoolExecutor(1, 1, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, excuteQueueSequence);

    private static ExecutorService executorServiceSynchronousSequence =
        new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE * 10, KEEP_ALIVE_TIME_SYNCHRONOUS,
            TimeUnit.MILLISECONDS, excuteSynchronousQueueSequence,
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Resource private AsynTaskResultHandler asynTaskResultHandler;

    @Override public void excute(Callable<FutureContext> task, AsynTaskContent content) {
        asynTaskResultHandler.handleAsynTaskResult(executorService.submit(task), content);
    }

    @Override public void excuteSequence(Callable<FutureContext> task, AsynTaskContent content) {
        asynTaskResultHandler.handleAsynTaskResult(executorServiceSequence.submit(task), content);
    }

    public static int getAsynTaskWorkQueueSize() {
        return excuteQueue.size();
    }

    public static int getSendSequenceWorkQueueSize() {
        return excuteQueueSequence.size();
    }

    public static int getSendSynchronousSequenceWorkQueueSize() {
        return excuteSynchronousQueueSequence.size();
    }

    @PreDestroy public void shutdownExecutor() {
        executorService.shutdown();
        executorServiceSequence.shutdown();
        executorServiceSynchronousSequence.shutdown();
        if (logger.isDebugEnabled()) {
            logger.info(
                "executorService executorServiceSequence executorServiceSynchronousSequence is shutdown");
        }
    }
}
