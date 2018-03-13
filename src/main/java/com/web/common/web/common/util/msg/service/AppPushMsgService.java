package com.web.common.web.common.util.msg.service;

import com.web.common.web.common.util.msg.model.AppPushMsgSendRecord;
import com.web.common.web.common.util.msg.send.result.AppPushMsgSendResult;

/**
 * <pre>
 *  APP推送消息服务
 * </pre>
 *
 * @author: xiongchengwei
 * @date: 2016年2月22日 下午4:27:58
 */
public interface AppPushMsgService {

    /**
     * 百度云推送APP消息
     *
     * @param appMsgSendRecord APP消息记录
     * @return 返回APP推送记录的结果
     */
    public AppPushMsgSendResult bPushSend(AppPushMsgSendRecord appMsgSendRecord);

    /**
     * 极光推送APP消息
     *
     * @param appMsgSendRecord APP消息记录
     * @return 返回APP推送记录的结果
     */
    public AppPushMsgSendResult JPushSend(AppPushMsgSendRecord appMsgSendRecord);
}
