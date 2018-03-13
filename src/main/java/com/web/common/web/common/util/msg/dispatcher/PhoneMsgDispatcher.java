package com.web.common.web.common.util.msg.dispatcher;

import com.web.common.web.common.util.msg.data.PhoneMsg;
import com.web.common.web.common.util.msg.send.result.PhoneMsgSendResult;

/**
 * <pre>
 * 短信发送的门面，分发到具体的类中处理
 * </pre>
 *
 * @author: xiongchengwei
 * @date: 2016年2月21日 下午11:13:15
 */
public interface PhoneMsgDispatcher {

    PhoneMsgSendResult send(PhoneMsg phoneMsg, boolean asynExector);
}
