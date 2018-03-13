package com.web.common.web.common.util.msg;

import java.util.concurrent.Future;

import com.web.common.web.common.util.msg.data.AsynTaskContent;
import com.web.common.web.common.util.msg.data.FutureContext;

/**
 * <pre>
 * 执行异步任务后的对操作结果的处理
 * </pre>
 *
 * @author: xiongchengwei
 * @date:2016年2月18日 下午6:32:23
 */
public interface AsynTaskResultHandler {

    void handleAsynTaskResult(Future<FutureContext> future, AsynTaskContent content);

}
