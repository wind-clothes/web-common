package com.web.common.web.common.util.msg.data;

import com.web.common.web.common.util.msg.enums.AsynTaskType;

/**
 * <pre>
 * 异步任务的辅助信息
 * </pre>
 *
 * @author: xiongchengwei
 * @date:2016年2月18日 下午4:44:11
 */
public class AsynTaskContent {
    private AsynTaskType asynTaskType;// 任务类型

    public AsynTaskContent(AsynTaskType asynTaskType) {
        this.asynTaskType = asynTaskType;
    }

    public AsynTaskType getAsynTaskType() {
        return asynTaskType;
    }

    public void setAsynTaskType(AsynTaskType asynTaskType) {
        this.asynTaskType = asynTaskType;
    }

}
