package com.web.common.web.common.util.msg;

import java.util.concurrent.Callable;

import com.web.common.web.common.util.msg.data.AsynTaskContent;
import com.web.common.web.common.util.msg.data.FutureContext;

/**
 * <pre>
 * 异步执行task接口
 * </pre>
 *
 * @author: xiongchengwei
 * @date:2016年2月18日 下午4:35:54
 */
public interface AsynTaskExecutor {

    void excute(Callable<FutureContext> task, AsynTaskContent content);

    void excuteSequence(Callable<FutureContext> task, AsynTaskContent content);
}
