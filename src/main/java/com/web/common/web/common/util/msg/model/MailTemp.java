package com.web.common.web.common.util.msg.model;


import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * 邮件信息
 */
public class MailTemp implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;//ID
    private String mailFrom;//发件人 （必填）
    private String[] mailSend;//收件人(一个或多个) （必填）
    private String mailSendStr;//数据库中存储的收件人，如果有多个收件人，收件人之间用英文半角逗号隔开
    private String mailSubject;//邮件标题   （必填）
    private Integer mailType;//邮件类型
    private String mailCc;//抄送人，一个或多个，如果是多个，则用英文半角逗号隔开
    private Date mailSendtime;//邮件发送时间
    private String mailContent;//邮件内容  （必填）
    private String mailRemark;//邮件备注
    private Integer mailStatus;//邮件状态：1、3表示失败；2表示成功
    private Date createTime;//发送记录生成时间
    private String creator;//发件人用户名 
    private String mailSuccesstime;//邮件成功发送的时间
    private String fileName;//文件名称
    private String urlPath;//文件链接路径

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    /**
     * 设置发件人    （必填）
     *
     * @param mailFrom 发件人
     */
    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom == null ? null : mailFrom.trim();
    }

    public String[] getMailSend() {
        return mailSend;
    }

    /**
     * 设置收件人    （必填）
     *
     * @param mailSend 收件人
     */
    public void setMailSend(String[] mailSend) {
        this.mailSend = mailSend;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    /**
     * 设置邮件标题   （必填）
     *
     * @param mailSubject 邮件标题
     */
    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject == null ? null : mailSubject.trim();
    }

    public String getMailCc() {
        return mailCc;
    }

    /**
     * 设置抄送人    （可选）
     *
     * @param mailCc 抄送人，若要抄送多人，则使用英文半角逗号隔开
     */
    public void setMailCc(String mailCc) {
        this.mailCc = mailCc == null ? null : mailCc.trim();
    }

    public Integer getMailType() {
        return mailType;
    }

    /**
     * 设置邮件类型   （可选）
     *
     * @param mailType 邮件类型，参见{@link MailType}
     */
    public void setMailType(Integer mailType) {
        this.mailType = mailType;
    }

    public Date getMailSendtime() {
        return mailSendtime;
    }

    /**
     * 设置邮件发送时间     （可选）
     * 如果不传，默认使用当前时间作为邮件发送时间
     *
     * @param mailSendtime 邮件发送时间
     */
    public void setMailSendtime(Date mailSendtime) {
        this.mailSendtime = mailSendtime;
    }

    public String getMailContent() {
        return mailContent;
    }

    /**
     * 设置邮件内容   （必填）
     *
     * @param mailContent 邮件内容
     */
    public void setMailContent(String mailContent) {
        this.mailContent = mailContent == null ? null : mailContent.trim();
    }

    public String getMailRemark() {
        return mailRemark;
    }

    /**
     * 设置邮件备注   （可选）
     *
     * @param mailRemark 邮件备注
     */
    public void setMailRemark(String mailRemark) {
        this.mailRemark = mailRemark == null ? null : mailRemark.trim();
    }


    public Integer getMailStatus() {
        return mailStatus;
    }

    public void setMailStatus(Integer mailStatus) {
        this.mailStatus = mailStatus;
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

    /**
     * 设置发送邮件的用户名   （可选）
     *
     * @param creator 发送邮件的用户名
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * 设置文件名称   （可选）
     * 如果文件名和文件路径都不为空，则会为邮件添加附件
     *
     * @param fileName 文件名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrlPath() {
        return urlPath;
    }

    /**
     * 设置文件路径   （可选）
     * 如果文件名和文件路径都不为空，则会为邮件添加附件
     *
     * @param urlPath 文件路径
     */
    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getMailSuccesstime() {
        return mailSuccesstime;
    }

    public void setMailSuccesstime(String mailSuccesstime) {
        this.mailSuccesstime = mailSuccesstime;
    }

    public String getMailSendStr() {
        return mailSendStr;
    }

    public void setMailSendStr(String mailSendStr) {
        this.mailSendStr = mailSendStr;
    }

    @Override public String toString() {
        return "MailTemp [id=" + id + ", mailFrom=" + mailFrom + ", mailSend=" + Arrays
            .toString(mailSend) + ", mailSendStr=" + mailSendStr + ", mailSubject=" + mailSubject
            + ", mailType=" + mailType + ", mailCc=" + mailCc + ", mailSendtime=" + mailSendtime
            + ", mailContent=" + mailContent + ", mailRemark=" + mailRemark + ", mailStatus="
            + mailStatus + ", createTime=" + createTime + ", creator=" + creator
            + ", mailSuccesstime=" + mailSuccesstime + ", fileName=" + fileName + ", urlPath="
            + urlPath + "]";
    }

    /**
     * 邮件状态
     */
    public static enum MailStatus {
        SENDFAILURE(1, "发送失败", ""), SENDSUCCESS(2, "发送成功", ""), SENDTIMER(3, "定时发送",
            ""), INVALID_ARGS(4, "参数非法", "");
        private int id;
        private String status;
        private String desc;

        private MailStatus(int id, String status, String desc) {
            this.id = id;
            this.status = status;
            this.desc = desc;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }


    /**
     * 邮件类型
     */
    public static enum MailType {
        FIRSTTRANS(1, "首次交单", ""), DISTRIBUTE(2, "分发", ""), LOCKED(3, "锁单", ""), UNREPEAT(4, "排重",
            ""), FALLBACK(5, "业务打回", ""), NOT_APPROVED(6, "审核不通过", ""),
        SIGNING_CONTRACT(7, "确认已放款", ""), SENDMODE(8, "发送模板", ""), REJECTED(9, "拒绝受理", "");
        private int id;
        private String status;
        private String desc;

        private MailType(int id, String status, String desc) {
            this.id = id;
            this.status = status;
            this.desc = desc;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}

