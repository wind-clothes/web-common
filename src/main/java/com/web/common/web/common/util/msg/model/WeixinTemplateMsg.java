package com.web.common.web.common.util.msg.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信模板消息发送
 *
 * @author xiongchengwei
 */
public class WeixinTemplateMsg implements Serializable {

    private static final long serialVersionUID = 1L;
    public Long id; // ID
    private String toUser; // 接收模板消息的openid
    private String templateId; // 模板ID
    private String data; // 要发送的JSON格式数据
    private String sysName; // 发送端系统名称
    private Integer status; // 发送状态：0表示初始状态；1表示发送失败；2表示发送成功
    private Date createTime; // 发送时间
    private Long userId; // 接收模板消息的用户ID
    private String username; // 接收模板消息的用户名
    private String errorDesc; // 发送失败时存储错误描述
    private String url;//开发者绑定的服务器地址
    private String topColor;//颜色值

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTopColor() {
        return topColor;
    }

    public void setTopColor(String topColor) {
        this.topColor = topColor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public WeixinTemplateMsg() {
        super();
    }
}
