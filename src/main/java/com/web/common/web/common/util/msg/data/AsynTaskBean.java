package com.web.common.web.common.util.msg.data;

import java.util.concurrent.Future;

/**
 * <pre>
 *
 * </pre>
 *
 * @author: xiongchengwei
 * @date:2016年2月18日 下午6:47:39
 */
public class AsynTaskBean {
    private Future<FutureContext> future;
    private AsynTaskContent content;

    public AsynTaskBean() {
        super();
    }

    public AsynTaskBean(Future<FutureContext> future, AsynTaskContent content) {
        this.future = future;
        this.content = content;
    }

    public Future<FutureContext> getFuture() {
        return future;
    }

    public void setFuture(Future<FutureContext> future) {
        this.future = future;
    }

    public AsynTaskContent getContent() {
        return content;
    }

    public void setContent(AsynTaskContent content) {
        this.content = content;
    }

}
