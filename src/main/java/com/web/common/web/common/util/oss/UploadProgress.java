package com.web.common.web.common.util.oss;

/**
 * 上传进度
 *
 * @author: chengweixiong@uworks.cc
 * @date:2015年10月22日 下午4:40:21
 */
public class UploadProgress {
    public enum STATUS {
        UPLOADING, FINISH, FAILED
    }


    private volatile int recievedBytes;
    private volatile STATUS status;

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public int getRecievedBytes() {
        return recievedBytes;
    }

    public void setRecievedBytes(int bytes) {
        this.recievedBytes = bytes;
    }
}
