package com.web.common.web.common.util.msg.send.result;

import java.util.Map;

/**
 * <pre>
 * 消息发送结果的的基类，所有消息的发送结果都继承该类
 * </pre>
 *
 * @author: xiongchengwei
 * @date: 2016年2月19日 下午2:03:24
 */
public class MsgSendResult {

    /**
     * 是否执行成功
     */
    protected boolean isSuccess;
    /**
     * 状态描述信息
     */
    protected String msg;
    /**
     * 状态码
     */
    protected Integer code;
    /**
     * 返回数据
     */
    protected Map<String, Object> data;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

}
