package com.web.common.web.common.util.msg.service;

import java.util.LinkedList;

import com.web.common.web.common.util.msg.data.TemplateMsg;
import com.web.common.web.common.util.msg.send.result.WeixinTemplateMsgSendResult;

/**
 * 微信模板消息服务接口
 *
 * @author xiongchengwei
 */
public interface WeixinTemplateMsgService {

    /**
     * 发送微信模板消息
     *
     * @param toUser     接收模板消息的openid
     * @param templateId 模板ID
     * @param url        开发者配置的服务器地址
     * @param topColor   颜色值
     * @param data       要发送的JSON格式数据
     * @param userId     接收模板消息的用户ID
     * @param username   接收模板消息的用户名
     * @param sysName    发送模板消息的系统名称
     * @return 微信模板消息发送结果
     */
    WeixinTemplateMsgSendResult send(String toUser, String templateId, String url, String topColor,
        String data, Long userId, String username, String sysName);

    /**
     * 微信模板消息按顺序发送
     *
     * @param tms     微信模板消息列表
     * @param sysName 系统名称
     * @return 返回发送结果
     */
    WeixinTemplateMsgSendResult sendByOrder(LinkedList<TemplateMsg> tms, String sysName);

}
