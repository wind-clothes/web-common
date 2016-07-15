package com.web.common.web.common.util.msg.data;

/**
 * 微信模板消息
 *
 * @author xiongchengwei
 */
public class TemplateMsg {

    /**
     * 接收模板消息的openid
     */
    private String toUser;
    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 开发者配置的服务器地址
     */
    private String url;

    /**
     * 颜色值
     */
    private String topColor;

    /**
     * 要发送的JSON格式数据
     */
    private String data;

    /**
     * 接收模板消息的用户ID
     */
    private Long userId;

    /**
     * 接收模板消息的用户名
     */
    private String username;

    public String getToUser() {
        return toUser;
    }

    /**
     * 设置接收模板消息的openid（必填）
     *
     * @param toUser 接收模板消息的openid
     */
    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getTemplateId() {
        return templateId;
    }

    /**
     * 设置模板ID（必填）
     *
     * @param templateId 模板ID
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getUrl() {
        return url;
    }

    /**
     * 设置开发者配置的服务器地址（必填）
     *
     * @param url 开发者配置的服务器地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public String getTopColor() {
        return topColor;
    }

    /**
     * 设置颜色值（必填）
     *
     * @param topColor 颜色值
     */
    public void setTopColor(String topColor) {
        this.topColor = topColor;
    }

    public String getData() {
        return data;
    }

    /**
     * 设置要发送的JSON格式数据（必填）
     *
     * @param data 要发送的JSON格式数据
     */
    public void setData(String data) {
        this.data = data;
    }

    public Long getUserId() {
        return userId;
    }

    /**
     * 设置接收模板消息的用户ID（必填）
     *
     * @param userId 要接收模板消息的用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    /**
     * 设置接收模板消息的用户名（必填）
     *
     * @param username 接收模板消息的用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public TemplateMsg() {
        super();
    }

    /**
     * 构造方法
     *
     * @param toUser     接收模板消息的openid
     * @param templateId 模板ID
     * @param url        开发者配置的服务器地址
     * @param topColor   颜色值
     * @param data       要发送的JSON格式数据
     * @param userId     接收模板消息的用户ID
     * @param username   接收模板消息的用户名
     */
    public TemplateMsg(String toUser, String templateId, String url, String topColor, String data,
        long userId, String username) {
        super();
        this.toUser = toUser;
        this.templateId = templateId;
        this.url = url;
        this.topColor = topColor;
        this.data = data;
        this.userId = userId;
        this.username = username;
    }


}
