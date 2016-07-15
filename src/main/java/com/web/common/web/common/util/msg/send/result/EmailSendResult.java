package com.web.common.web.common.util.msg.send.result;

/**
 * 邮件发送结果：<br>
 * 1.发送成功：success值为true，msg的值为邮件发送成功；<br>
 * 2.发送失败：success值为false，msg的值为发送失败的原因。<br>
 *
 * @author xiongchengwei
 */
public class EmailSendResult extends MsgSendResult {
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public EmailSendResult(boolean success) {
        super();
    }

    public EmailSendResult(boolean success, Integer status) {
        this.isSuccess = success;
        this.status = status;
    }

    public EmailSendResult() {
        super();
    }

    @Override public String toString() {
        return "EmailSendResult [status=" + status + ", success=" + isSuccess + ", msg=" + msg
            + "]";
    }

}
