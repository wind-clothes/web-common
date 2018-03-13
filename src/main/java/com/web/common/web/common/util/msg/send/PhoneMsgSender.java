package com.web.common.web.common.util.msg.send;

import java.util.List;
import java.util.concurrent.Callable;

import com.web.common.web.common.util.msg.data.FutureContext;
import com.web.common.web.common.util.msg.data.PhoneMsg;
import com.web.common.web.common.util.msg.model.PhoneMsgSendStatusReport;
import com.web.common.web.common.util.msg.send.result.PhoneMsgSendResult;

/**
 * <pre>
 * 发送短信模块的接口
 * </pre>
 *
 * @author: xiongchengwei
 * @date:2016年2月19日 下午1:49:20
 */
public interface PhoneMsgSender extends Callable<FutureContext> {

    /**
     * 发送短信
     *
     * @param phoneMsg   短信实例
     * @param serviceURL 发送接口的URL
     * @return 短信发送结果
     */
    public PhoneMsgSendResult send(PhoneMsg phoneMsg);

    /**
     * 短信的剩余可发送条数
     *
     * @param serviceURL    查询接口的URL，有的服务商发送接口URL和余额查询接口URL相同，<br>
     *                      有的服务商不同.这里我们默认传的是短信发送接口的URL
     * @param signatureType 签名类型
     * @return 剩余可发送条数
     */
    public Integer getBlance(String serviceURL, String signatureType);

    /**
     * 获取短信发送状态报告
     *
     * @return 返回短信发送状态报告
     */
    public List<PhoneMsgSendStatusReport> getSendStatusReports();

}
