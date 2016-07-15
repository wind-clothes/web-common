package com.web.common.web.common.util.msg.data.push;

import java.util.Map;

import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.audience.Audience;

public abstract class AbstractAppPushModel {

    /**
     * 额外信息
     */
    protected Map<String, String> extra;

    /**
     * <pre>
     * 接收者的类型
     * 1、默认为别名
     * 2、若为空，表示所有类型都设置
     * </pre>
     */
    protected Audience audienceType;

    /**
     * <pre>
     * 推送平台(默认为空)
     * 1、若为空，表示所有平台；
     * </pre>
     */
    protected Platform platform;

    /**
     * 选项
     */
    public Options options;

    public Map<String, String> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }

    public Audience getAudienceType() {
        return audienceType;
    }

    public void setAudienceType(Audience audienceType) {
        this.audienceType = audienceType;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

}
