package com.web.common.web.common.util.msg.data;

/**
 * <pre>
 * 异步任务的执行状态数据结构
 * </pre>
 *
 * @author: xiongchengwei
 * @date:2016年2月18日 下午4:42:02
 */
public class FutureContext {
    private Integer status;// 异步任务执行状态值
    private String errorMsg;// 如果异步任务执行失败，这里存储失败的原因
    private String ext;// 如果异步任务执行成功，这里存储扩展信息

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

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public FutureContext(Integer status, String errorMsg, String ext) {
        super();
        this.status = status;
        this.errorMsg = errorMsg;
        this.ext = ext;
    }

    public FutureContext() {
        super();
    }

    @Override public String toString() {
        return "FutureContext [status=" + status + ", errorMsg=" + errorMsg + ", ext=" + ext + "]";
    }
}
