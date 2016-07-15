package com.web.common.web.common.util.msg.resulthandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

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
public class DefaultRefactorAsynTaskResultHandler implements AsynTaskResultHandler {

    private static Logger logger =
        LoggerFactory.getLogger(DefaultRefactorAsynTaskResultHandler.class);

    private static BlockingQueue<AsynTaskBean> resultHandlerQueue =
        new LinkedBlockingQueue<AsynTaskBean>();

    private static ResultHandlerThread resultHandlerThread = null;

    /**
     * 初始化
     *
     * @param
     * @return void
     */
    @PostConstruct public void init() {
        // 初始化(在这里可以进行相关数据的配置，比如后期配置参数可以配置在配置文件中)
        resultHandlerThread = new ResultHandlerThread(resultHandlerQueue);
        // 启动处理线程
        Thread thread = new Thread(resultHandlerThread);
        thread.start();
        logger.debug("DefaultRefactorAsynTaskResultHandler is startup");
        ;
    }

    @Override
    public void handleAsynTaskResult(Future<FutureContext> future, AsynTaskContent content) {
        resultHandlerQueue.offer(new AsynTaskBean(future, content));
    }

    @PreDestroy public void destroy() {
        resultHandlerThread.cleanup();
    }
}
