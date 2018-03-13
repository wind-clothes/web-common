package com.web.common.web.common.util.msg.send.impl;

import java.util.List;

import com.web.common.web.common.util.msg.data.PhoneMsg;
import com.web.common.web.common.util.msg.model.PhoneMsgSendStatusReport;
import com.web.common.web.common.util.msg.send.AbstractPhoneMsgSender;
import com.web.common.web.common.util.msg.send.result.PhoneMsgSendResult;

/**
 * <pre>
 * 螺丝帽的短信实现
 * </pre>
 *
 * @author: xiongchengwei
 * @date: 2016年2月19日 下午3:07:32
 */
public class LuosimaoPhoneMsgSendImpl extends AbstractPhoneMsgSender {

    public LuosimaoPhoneMsgSendImpl() {
        super();
    }

    public LuosimaoPhoneMsgSendImpl(PhoneMsg phoneMsg) {
        this.phoneMsg = phoneMsg;
    }

    @Override public Integer getBlance(String serviceURL, String signatureType) {
        return null;
    }

    @Override public List<PhoneMsgSendStatusReport> getSendStatusReports() {
        return null;
    }

    @Override public PhoneMsgSendResult send(PhoneMsg phoneMsg) {
        // 在这里处理短信发送的逻辑
        return null;
    }

}
