package com.web.common.web.common.util.msg.send.result;

/**
 * 微信模板消息发送结果 1.发送成功：success值为true，msg的值为模板消息的msgid；<br>
 * 2.发送失败：success值为false，msg的值为模板消息发送失败的错误原因。<br>
 *
 * @author xiongchengwei
 */
public class WeixinTemplateMsgSendResult extends MsgSendResult {

    private Integer errorType;// 错误类型

    public Integer getErrorType() {
        return errorType;
    }

    public void setErrorType(Integer errorType) {
        this.errorType = errorType;
    }

    public WeixinTemplateMsgSendResult() {
        super();
    }

    public WeixinTemplateMsgSendResult(boolean success) {
        this.isSuccess = success;
    }

}
