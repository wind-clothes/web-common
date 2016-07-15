package com.web.common.web.common.util.oss;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.web.common.web.common.util.oss.UploadProgress.STATUS;


/**
 * 上传结束状态通知
 *
 * @author: chengweixiong@uworks.cc
 * @date:2015年10月22日 下午8:32:09
 */
public class ProgressInputStream extends FilterInputStream {
    private static final int NOTIFICATION_THRESHOLD = 8 * 1024;
    private int receivedBytes = 0;
    private UploadProgress progress;

    protected ProgressInputStream(InputStream in, UploadProgress progress) {
        super(in);
        this.progress = progress;
    }


    @Override public int read(byte[] b, int off, int len) throws IOException {
        int byteCount = super.read(b, off, len);
        if (byteCount != -1) {
            receivedBytes += byteCount;
        }

        if (receivedBytes % NOTIFICATION_THRESHOLD == 0 || byteCount == -1) {
            notify(receivedBytes, byteCount);
        }
        return byteCount;

    }

    @Override public void close() throws IOException {
        super.close();
    }

    private void notify(int bytesRead, int lastByte) {
        progress.setRecievedBytes(bytesRead);
        if (lastByte == -1) {
            progress.setStatus(STATUS.FINISH);
        } else {
            progress.setStatus(STATUS.UPLOADING);
        }
    }
}
