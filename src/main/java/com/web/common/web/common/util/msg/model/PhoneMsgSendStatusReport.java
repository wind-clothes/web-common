package com.web.common.web.common.util.msg.model;

import java.util.Date;

import org.apache.http.client.utils.DateUtils;

/**
 * 短信发送状态报告（从第三方服务商主动获取）
 *
 * @author xiongchengwei
 */
public class PhoneMsgSendStatusReport {
    private String batchNo;// 短信发送批次号
    private Date reportTime;// 报告生成时间
    private String phoneNo;// 手机号
    private int sendStatus;// 1表示成功，0表示失败
    private String provider;// 服务商：1表示漫道科技，2表示拓鹏云信，3表示红树科技
    private String originReport;// 原始的报告内容
    private Date createTime;// 报告获取时间

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOriginReport() {
        return originReport;
    }

    public void setOriginReport(String originReport) {
        this.originReport = originReport;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    public PhoneMsgSendStatusReport() {
        super();
    }

    @Override public String toString() {
        return "PhoneMsgSendStatusReport [batchNo=" + batchNo + ", reportTime=" + (
            reportTime == null ? "" : DateUtils.formatDate(reportTime, "yyyy-MM-dd HH:mm:ss"))
            + ", phoneNo=" + phoneNo + ", sendStatus=" + sendStatus + ", provider=" + provider
            + ", originReport=" + originReport + "]";
    }

}
