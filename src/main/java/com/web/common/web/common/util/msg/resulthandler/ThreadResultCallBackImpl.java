package com.web.common.web.common.util.msg.resulthandler;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.web.common.web.common.util.msg.data.AsynTaskBean;
import com.web.common.web.common.util.msg.data.AsynTaskContent;
import com.web.common.web.common.util.msg.data.FutureContext;

/**
 * <pre>
 * </pre>
 *
 * @author: xiongchengwei
 * @date: 2016年2月27日 下午5:54:37
 */
public class ThreadResultCallBackImpl implements ThreadResultCallBack {

    private static Logger logger =
        LoggerFactory.getLogger(DefaultRefactorAsynTaskResultHandler.class);

    // 从future中获取异步执行结果的超时时间(毫秒)
    private static long DEFAULT_GET_FUTURE_RESULT_TIME_OUT = 10000L;
    private static long GET_FUTURE_RESULT_TIME_OUT = DEFAULT_GET_FUTURE_RESULT_TIME_OUT;

    @Override public void proccessFuture(AsynTaskBean asynTaskBean) {
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
