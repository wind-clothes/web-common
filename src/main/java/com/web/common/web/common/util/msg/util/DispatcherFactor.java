package com.web.common.web.common.util.msg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.web.common.web.common.util.msg.data.PhoneMsg;
import com.web.common.web.common.util.msg.enums.PhoneMsgDispatcherProvider;
import com.web.common.web.common.util.msg.send.PhoneMsgSender;
import com.web.common.web.common.util.msg.send.impl.LuosimaoPhoneMsgSendImpl;

/**
 * <pre>
 * </pre>
 *
 * @author: xiongchengwei
 * @date: 2016年2月21日 下午11:35:29
 */
public class DispatcherFactor {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherFactor.class);

    public PhoneMsgSender getPhoneMsgSend(PhoneMsgDispatcherProvider provider, PhoneMsg phoneMsg) {
        PhoneMsgSender phoneMsgSend = null;
        switch (provider) {
            case LUOSIMAO:
                phoneMsgSend = new LuosimaoPhoneMsgSendImpl(phoneMsg);
                break;
            default:
                phoneMsgSend = new LuosimaoPhoneMsgSendImpl(phoneMsg);
        }
        return phoneMsgSend;
    }
}
