package com.web.common.web.common.util.msg.data;


/**
 * 手机短信
 *
 * @author Jianguo Luo
 * @since 2014年10月28日
 */
public class PhoneMsgEntity {
    private String phoneNo; // 一个或多个手机号
    private String content; // 短信内容
    private int phoneMsgType; // 手机短信类型:通知-1或验证码-2
    private String creator; // 创建人
    private long creatorId; // 创建人ID
    private int creatorType; // 创建人类型
    private int businessType; // 业务类型
    private String authCode; // 验证码
    private String userIp; // 用户IP
    private String signatureType; // 短信签名类型:51rongxin或renmaidai或bangdaibao

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPhoneMsgType() {
        return phoneMsgType;
    }

    public void setPhoneMsgType(int phoneMsgType) {
        this.phoneMsgType = phoneMsgType;
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

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }

    public PhoneMsgEntity() {
        super();
    }

    @Override public String toString() {
        return "PhoneMsgEntity [phoneNo=" + phoneNo + ", content=" + content + ", phoneMsgType="
            + phoneMsgType + ", creator=" + creator + ", creatorId=" + creatorId + ", creatorType="
            + creatorType + ", businessType=" + businessType + ", authCode=" + authCode
            + ", userIp=" + userIp + ", signatureType=" + signatureType + "]";
    }
}
