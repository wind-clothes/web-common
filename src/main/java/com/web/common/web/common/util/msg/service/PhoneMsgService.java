package com.web.common.web.common.util.msg.service;

import com.web.common.web.common.util.msg.send.result.PhoneMsgSendResult;


/**
 * 手机短信服务接口
 *
 * @author xiongchengwei
 * @since 1.0 2014年7月11日 上午10:05:45
 */
public interface PhoneMsgService {

    /**
     * 发送短信
     *
     * @param phoneNo       一个或多个手机号，若为多个手机号，则手机号之间用英文逗号隔开，如：13244445555,13255554444, 13677778888
     * @param content       短信内容
     * @param msgType       短信类型：1表示通知类短信；2表示验证码类短信
     * @param creator       短信创建人
     * @param creatorId     创建人ID
     * @param creatorType   创建人类型
     * @param businessType  业务类型
     * @param authCode      验证码
     * @param userIp        用户浏览器端IP
     * @param signatureType 短信签名类型
     * @param sysName       系统名称
     * @return 返回{@link PhoneMsgSendResult}类实例
     */
    public PhoneMsgSendResult send(String phoneNo, String content, int msgType, String creator,
        long creatorId, int creatorType, int businessType, String authCode, String userIp,
        String signatureType, String sysName) throws Exception;

    /**
     * 更新手机短信发送流水的状态
     *
     * @param id     手机短信流水ID
     * @param status 状态值
     */
    public void updateSmsSendStatus(Long id, Integer status);

}
