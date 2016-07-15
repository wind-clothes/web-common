package com.web.common.web.common.util.msg.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信发送记录
 *
 * @author xiongchengwei
 */
public class SmsSend implements Serializable {
    private static final long serialVersionUID = 1156126368746326701L;
    private long id;// ID
    private String content;// 短信内容
    private String sendPhoneNumber;// 发送的手机号码
    private int sendPhoneNumberCount;// 发送手机号数量，最大支持10000个
    private int type;// 短信类型
    private int status;// 发送状态
    private Date createTime;// 创建时间
    private String creator;// 创建人
    private long creatorId;// 创建人ID
    private int creatorType;// 创建人类型
    private String signatureType;//签名类型
    private String errorMsg;//短信发送错误信息
    private String ext;//短信发送扩展信息：比如短信发送服务商返回的短信发送批次号等
    private String sysName;//系统名称
    private String msgSendProvider;//短信发送服务商
    private int businessType;//业务类型
    private String userIp;//用户浏览器端IP
    private int resendTimes;//重发次数

    public int getResendTimes() {
        return resendTimes;
    }

    public void setResendTimes(int resendTimes) {
        this.resendTimes = resendTimes;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public SmsSend(String content, String sendPhoneNumber, int type, Date createTime,
        String creator, long creatorId, int creatorType, String signatureType, String sysName) {
        super();
        this.content = content;
        this.sendPhoneNumber = sendPhoneNumber;
        this.type = type;
        this.createTime = createTime;
        this.creator = creator;
        this.creatorId = creatorId;
        this.creatorType = creatorType;
        this.signatureType = signatureType;
        this.sysName = sysName;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getMsgSendProvider() {
        return msgSendProvider;
    }

    public void setMsgSendProvider(String msgSendProvider) {
        this.msgSendProvider = msgSendProvider;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }

    public SmsSend() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendPhoneNumber() {
        return sendPhoneNumber;
    }

    public void setSendPhoneNumber(String sendPhoneNumber) {
        this.sendPhoneNumber = sendPhoneNumber;
    }

    public int getSendPhoneNumberCount() {
        return sendPhoneNumberCount;
    }

    public void setSendPhoneNumberCount(int sendPhoneNumberCount) {
        this.sendPhoneNumberCount = sendPhoneNumberCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public int getCreatorType() {
        return creatorType;
    }

    public void setCreatorType(int creatorType) {
        this.creatorType = creatorType;
    }
}
