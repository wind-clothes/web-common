package com.web.common.web.common.util.msg.resulthandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.web.common.web.common.util.msg.AsynTaskResultHandler;
import com.web.common.web.common.util.msg.data.AsynTaskBean;
import com.web.common.web.common.util.msg.data.AsynTaskContent;
import com.web.common.web.common.util.msg.data.FutureContext;

/**
 * <pre>
 * 执行异步任务后的对操作结果的处理的实现类
 * </pre>
 *
 * @author: xiongchengwei
 * @date:2016年2月18日 下午6:39:23
 */
public class DefaultAsynTaskResultHandler implements AsynTaskResultHandler {

    private static Logger logger = LoggerFactory.getLogger(DefaultAsynTaskResultHandler.class);

    private static BlockingQueue<AsynTaskBean> resultHandlerQueue =
        new LinkedBlockingQueue<AsynTaskBean>();

    // 新建一个线程，用来执行队列中的返回结果
    private static ResultHandlerThread resultHandlerThread = null;

    // 检测当前类是否已经初始化了
    private static volatile boolean isAlive = true;

    // 从future中获取异步执行结果的超时时间(毫秒)
    private static long DEFAULT_GET_FUTURE_RESULT_TIME_OUT = 10000L;
    private static long GET_FUTURE_RESULT_TIME_OUT = DEFAULT_GET_FUTURE_RESULT_TIME_OUT;

    /**
     * 初始化
     *
     * @param
     * @return void
     */
    @PostConstruct public void init() {
        // 初始化(在这里可以进行相关数据的配置，比如后期配置参数可以配置在配置文件中)
        resultHandlerThread = new ResultHandlerThread();
        // 启动处理线程
        Thread thread = new Thread(resultHandlerThread);
        thread.start();
    }

    /**
     * <pre>
     * 异步执行任务的结果处理线程（单线程）
     * </pre>
     *
     * @author: xiongchengwei
     * @date:2016年2月18日 下午7:58:08
     */
    public static class ResultHandlerThread implements Runnable {

        // 处理业务，比如需要将异步执行的信息保存到数据库中，可以在该类中处理
        public ResultHandlerThread() {
            super();
        }

        @Override public void run() {
            // 无限的循环读取队列中的异步执行结果，直到队列中数据不存在，就阻塞
            while (isAlive) {
                try {
                    AsynTaskBean asynTaskBean = resultHandlerQueue.take();
                    // 开始处理业务
                    proccessFuture(asynTaskBean);
                } catch (Exception e) {
                    logger.info("handler resultHandlerQueue take data is error", e);
                }
            }
        }

        private void proccessFuture(AsynTaskBean asynTaskBean) {
            Future<FutureContext> future = asynTaskBean.getFuture();
            AsynTaskContent content = asynTaskBean.getContent();
            FutureContext fc = null;
            try {
                fc = future.get(GET_FUTURE_RESULT_TIME_OUT, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                logger.info("from future get asyn task excutor result is errror", e);
                // 执行错误处理的异常业务处理 FIXME
            }
            // 处理业务，比如更新数据库该条记录的状态 FIXME
        }

    }

    @Override
    public void handleAsynTaskResult(Future<FutureContext> future, AsynTaskContent content) {
        resultHandlerQueue.offer(new AsynTaskBean(future, content));
    }

}
