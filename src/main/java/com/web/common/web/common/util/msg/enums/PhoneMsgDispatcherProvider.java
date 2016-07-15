package com.web.common.web.common.util.msg.enums;

/**
 * <pre>
 * </pre>
 *
 * @author: xiongchengwei
 * @date: 2016年2月21日 下午11:38:40
 */
public enum PhoneMsgDispatcherProvider {
    LUOSIMAO(1);

    private final Integer code;

    PhoneMsgDispatcherProvider(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }
}
