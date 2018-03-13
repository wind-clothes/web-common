package com.web.common.web.common.util.msg.model;

import java.io.Serializable;
import java.util.Date;

public class AppPushMsgSendRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    private Long id;
    /**
     * 手机ID
     */
    private String userId;
    /**
     * 通道标识
     */
    private String channelId;
    /**
     * 组标识
     */
    private String tag;
    /**
     * 要推送的消息内容
     */
    private String message;
    /**
     * 推送类型
     */
    private Integer pushType;
    /**
     * 设备类型
     */
    private Integer deviceType;
    /**
     * 消息类型
     */
    private Integer messageType;
    /**
     * 推送时间
     */
    private Date createTime;
    /**
     * 推送状态<br>
     * 0-初始化<br>
     * 1-成功<br>
     * 2-失败<br>
     */
    private Integer status;
    /**
     * 错误信息描述
     */
    private String errorMsg;
    /**
     * 系统名称
     */
    private String sysName;

    /**
     * 扩展信息
     */
    private String ext;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 推送设备指定
     */
    private String audience;

    /**
     * 设备类型
     */
    private String platform;


    public AppPushMsgSendRecord() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override public String toString() {
        return "AppMsgSendRecord [id=" + id + ", userId=" + userId + ", channelId=" + channelId
            + ", tag=" + tag + ", message=" + message + ", pushType=" + pushType + ", deviceType="
            + deviceType + ", messageType=" + messageType + ", createTime=" + createTime
            + ", status=" + status + ", errorMsg=" + errorMsg + ", sysName=" + sysName + ", ext="
            + ext + "]";
    }
}
