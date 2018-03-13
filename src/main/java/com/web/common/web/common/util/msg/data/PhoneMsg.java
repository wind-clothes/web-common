package com.web.common.web.common.util.msg.data;

import com.web.common.web.common.util.msg.enums.PhoneMsgDispatcherProvider;

/**
 * <pre>
 * 手机短信的模型数据
 * </pre>
 *
 * @author: xiongchengwei
 * @date: 2016年2月19日 下午1:57:24
 */
public class PhoneMsg {
    /**
     * 手机短信ID
     */
    private Long phoneMsgId;
    /**
     * 手机号
     */
    private String phoneNo;

    /**
     * 短信内容
     */
    private String content;
    /**
     * 短信签名类型
     *
     * @return
     */
    private String signatureType;

    /**
     * 服务商
     */
    private PhoneMsgDispatcherProvider provider;

    public PhoneMsgDispatcherProvider getProvider() {
        return provider;
    }

    public void setProvider(PhoneMsgDispatcherProvider provider) {
        this.provider = provider;
    }

    public Long getPhoneMsgId() {
        return phoneMsgId;
    }

    public void setPhoneMsgId(Long phoneMsgId) {
        this.phoneMsgId = phoneMsgId;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }

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

    public PhoneMsg(Long phoneMsgId, String phoneNo, String content, String signatureType,
        PhoneMsgDispatcherProvider provider) {
        super();
        this.phoneNo = phoneNo;
        this.content = content;
        this.signatureType = signatureType;
        this.phoneMsgId = phoneMsgId;
        this.provider = provider;
    }
}
