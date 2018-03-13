package com.web.common.web.common.util.msg.enums;

/**
 * <pre>
 * 短信消息的枚举信息
 * </pre>
 *
 * @author: xiongchengwei
 * @date: 2016年2月19日 下午3:59:05
 */
public enum PhoneMsgStatus {
    INIT(1), OK(2), ERROR(3), TEST(4);
    private final Integer code;

    PhoneMsgStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }
}
