package com.web.common.web.common.util.msg.enums;

public enum AsynTaskType {
    PHONE_MSG(1);

    private final Integer code;

    AsynTaskType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }
}
