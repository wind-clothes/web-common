package com.web.common.web.common.util.oss;


/**
 * 与oss相关的配置信息
 *
 * @author caoxudong
 * @since 0.1.0
 */
public class OSSConfig {

    private String accessKeyId;
    private String accessKeySecret;
    private String endpointForDownload;
    private String bucketName;
    private String realPath;
    private long expires;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getEndpointForDownload() {
        return endpointForDownload;
    }

    public void setEndpointForDownload(String endpointForDownload) {
        this.endpointForDownload = endpointForDownload;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

}
