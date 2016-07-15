package com.web.common.web.common.util.msg.service;

import java.util.concurrent.Callable;

import com.web.common.web.common.util.msg.data.FutureContext;

/**
 * 邮件发送服务接口
 *
 * @author xiongchengwei
 */
public interface EmailService extends Callable<FutureContext> {

}
