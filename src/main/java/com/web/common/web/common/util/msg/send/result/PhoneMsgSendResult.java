package com.web.common.web.common.util.msg.send.result;

import com.alibaba.fastjson.JSON;

/**
 * <pre>
 * 短信的发送结果
 * </pre>
 *
 * @author: xiongchengwei
 * @date: 2016年2月19日 下午2:02:35
 */
public class PhoneMsgSendResult extends MsgSendResult {

    public PhoneMsgSendResult() {
        super();
    }

    @Override public String toString() {
        return JSON.toJSONString(this);
    }
}
