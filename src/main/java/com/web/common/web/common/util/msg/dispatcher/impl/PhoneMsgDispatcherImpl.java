package com.web.common.web.common.util.msg.dispatcher.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.web.common.web.common.util.msg.core.DefaultAsynTaskExecutor;
import com.web.common.web.common.util.msg.data.AsynTaskContent;
import com.web.common.web.common.util.msg.data.PhoneMsg;
import com.web.common.web.common.util.msg.dispatcher.PhoneMsgDispatcher;
import com.web.common.web.common.util.msg.enums.AsynTaskType;
import com.web.common.web.common.util.msg.enums.PhoneMsgDispatcherProvider;
import com.web.common.web.common.util.msg.send.PhoneMsgSender;
import com.web.common.web.common.util.msg.send.result.PhoneMsgSendResult;
import com.web.common.web.common.util.msg.util.DispatcherFactor;

/**
 * <pre>
 *
 * </pre>
 *
 * @author: xiongchengwei
 * @date: 2016年2月21日 下午11:16:05
 */
public class PhoneMsgDispatcherImpl implements PhoneMsgDispatcher {

    private static Logger logger = LoggerFactory.getLogger(PhoneMsgDispatcherImpl.class);

    @Resource private DispatcherFactor dispatcherFactor;

    @Resource private DefaultAsynTaskExecutor asynTaskExecutor;

    @Override public PhoneMsgSendResult send(PhoneMsg phoneMsg, boolean asynExector) {
        //数据校验在service层处理 TODO
        if (logger.isDebugEnabled()) {
            logger.debug("PhoneMsgDispatcherImpl start send");
        }
        if (phoneMsg == null) {
            throw new IllegalArgumentException("短信对象不能为null！");
        }
        PhoneMsgDispatcherProvider provider = phoneMsg.getProvider();
        PhoneMsgSender sender = dispatcherFactor.getPhoneMsgSend(provider, phoneMsg);
        PhoneMsgSendResult result = null;
        // 采用异步的执行发送任务
        if (asynExector) {
            asynTaskExecutor.excute(sender, new AsynTaskContent(AsynTaskType.PHONE_MSG));
            result = new PhoneMsgSendResult();
            result.setSuccess(true);
        } else {
            // 直接发送逻辑
            result = sender.send(phoneMsg);
        }
        return result;
    }


}
