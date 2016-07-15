package com.web.common.web.common.util.msg.data.push;

/**
 * 通知
 *
 * @author: chengweixiong@uworks.cc
 * @date: 2016年1月1日 下午4:13:04
 */
public class AppPushNotification extends AbstractAppPushModel {

    /**
     * 标题，ios只用一个alert，android需用一个alert 和 一个title
     */
    private String title;

    /**
     * 摘要，ios只用一个alert，android需用一个alert 和 一个title
     */
    private String alert;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

}
