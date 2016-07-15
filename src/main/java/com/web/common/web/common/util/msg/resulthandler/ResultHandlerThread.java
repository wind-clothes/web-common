package com.web.common.web.common.util.msg.resulthandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.web.common.web.common.util.msg.data.AsynTaskBean;

/**
 * <pre>
 * 异步执行任务的结果处理线程（单线程）由{@link DefaultAsynTaskResultHandler} 中提出
 * </pre>
 *
 * @author: xiongchengwei
 * @date:2016年2月18日 下午7:58:08
 */
public class ResultHandlerThread implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(ResultHandlerThread.class);

    private static volatile boolean proccessFuture = true;

    private BlockingQueue<AsynTaskBean> resultHandlerQueue =
        new LinkedBlockingQueue<AsynTaskBean>();

    @Resource private ThreadResultCallBack threadResultCallBack;

    // 处理业务，比如需要将异步执行的信息保存到数据库中，可以在该类中处理
    public ResultHandlerThread() {
        super();
    }

    public ResultHandlerThread(BlockingQueue<AsynTaskBean> resultHandlerQueue) {
        super();
        this.resultHandlerQueue = resultHandlerQueue;
    }

    @Override public void run() {
        // 无限的循环读取队列中的异步执行结果，直到队列中数据不存在，就阻塞
        while (proccessFuture) {
            try {
                AsynTaskBean asynTaskBean = resultHandlerQueue.take();
                // 开始处理业务
                threadResultCallBack.proccessFuture(asynTaskBean);
            } catch (Exception e) {
                logger.info("handler resultHandlerQueue take data is error", e);
            }
        }
    }

    public void cleanup() {
        proccessFuture = false;
    }
}
