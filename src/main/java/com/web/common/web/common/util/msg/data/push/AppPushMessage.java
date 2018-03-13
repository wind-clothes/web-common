package com.web.common.web.common.util.msg.data.push;

/**
 * 消息
 *
 * @author: chengweixiong@uworks.cc
 * @date: 2016年1月1日 下午4:13:09
 */
public class AppPushMessage extends AbstractAppPushModel {

    /**
     * 消息
     */
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
