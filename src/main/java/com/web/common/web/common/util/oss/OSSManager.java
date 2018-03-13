/**
 *
 */
package com.web.common.web.common.util.oss;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.web.common.web.common.util.img.FileTools;
import com.web.common.web.common.util.oss.UploadProgress.STATUS;


/**
 * OSS管理
 *
 * @author: chengweixiong@uworks.cc
 * @date:2015年10月22日 下午4:39:36
 */
public class OSSManager {
    private static final Logger LOG = Logger.getLogger(OSSManager.class);
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);
    private OSSClient ossClient;
    private OSSConfig ossConfig;
    private static final String thumb = "thumb/";
    private static final String PROTOTYPE = "prototype/";

    /**
     * 异步上传图片到OSS.
     *
     * @param name    唯一性标示，不能重复
     * @param dirName 文件目录，只能存在一级，不允许为空，以/开头，以/结尾 {@see File#spearator}
     * @param file    文件 {@value File}
     */
    public UploadProgress upload(final String name, final String dirName, final File file) {
        final UploadProgress progress = new UploadProgress();
        String dirPath = dirName;
        String imagePath = name;
        String thumbPath = thumb + name;
        if (StringUtils.isEmpty(dirName)) {
            if (dirPath.startsWith(File.separator)) {
                dirPath = dirPath.substring(1); // OSS上传不允许使用根目录
            }
            imagePath = dirPath + PROTOTYPE + name;
            thumbPath = dirPath + thumb + name;
        }
        final String imageKey = imagePath;
        final String thumbkey = thumbPath;

        // 定义具体执行的任务.
        final Callable<Void> task = new Callable<Void>() {
            @Override public Void call() throws Exception {
                try {
                    // 保存临时图片
                    String imageFilePath = FileTools.getApplicationPath("data");
                    FileTools.writeTo(file, imageFilePath, name);

                    // 生成临时缩略图
                    String thumbFilePath = FileTools.getApplicationPath("data/thumb");
                    File imageFile = new File(imageFilePath + name);
                    FileTools.resizeTo(imageFile, thumbFilePath, name, 140); // 140px压缩

                    // 上传缩略图
                    File thumbImageFile = new File(thumbFilePath + name);
                    ObjectMetadata meta = new ObjectMetadata();
                    meta.setContentLength(thumbImageFile.length());

                    LOG.info("开始上传文件，文件名：" + thumbkey + ", 文件大小：" + meta.getContentLength());
                    ossClient.putObject(ossConfig.getBucketName(), thumbkey,
                        new FileInputStream(thumbImageFile), meta);
                    LOG.info("文件上传成功，文件名：" + thumbkey);

                    // 上传原图
                    ProgressInputStream content =
                        new ProgressInputStream(new FileInputStream(imageFile), progress);
                    meta = new ObjectMetadata();
                    meta.setContentLength(imageFile.length());

                    LOG.info("开始上传文件，文件名：" + imageKey + ", 文件大小：" + meta.getContentLength());
                    ossClient.putObject(ossConfig.getBucketName(), imageKey, content, meta);
                    LOG.info("文件上传成功，文件名：" + imageKey + ", 文件大小：" + progress.getRecievedBytes());

                    progress.setStatus(STATUS.FINISH);

                    // 删除临时图片
                    FileTools.deleteFile(name, "data");
                    FileTools.deleteFile(name, "data/thumb");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    progress.setStatus(STATUS.FAILED);
                }
                return null;
            }
        };

        // 异步执行这个任务，同时被执行的任务有一个超时时间.
        new Thread(new Runnable() {
            @Override public void run() {
                Future<Void> future = executor.submit(task);
                try {
                    // 超时时间设置为10秒
                    future.get(10000, TimeUnit.SECONDS);
                    future.cancel(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return progress;
    }

    /**
     * 同步删除图片
     *
     * @param dirName 文件目录，必须以/开头，以/结尾
     * @param key     文件名称（唯一）
     */
    public boolean delete(String name, String dirName) {
        try {
            String imageKey = dirName.substring(1) + PROTOTYPE + name;
            ossClient.deleteObject(ossConfig.getBucketName(), imageKey);
            String thumbkey = dirName.substring(1) + thumb + name;
            ossClient.deleteObject(ossConfig.getBucketName(), thumbkey);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获得公网访问原图URL, 使用GET请求访问.
     *
     * @param dirName 文件目录，必须以'/'开头，并且'/'结尾
     */
    public String getPublicImageKeyURL(String name, String dirName) {
        StringBuilder url = new StringBuilder(getPublicKeyURL());
        url.append(dirName);
        url.append(PROTOTYPE);
        url.append(name);
        return url.toString();
    }

    /**
     * 获得公网访问压缩图片URL, 使用GET请求访问.
     *
     * @param dirName 文件目录，必须以'/'开头，并且'/'结尾
     */
    public String getPublicThumbkeyURL(String name, String dirName) {
        StringBuilder url = new StringBuilder(getPublicKeyURL());
        url.append(dirName);
        url.append(thumb);
        url.append(name);
        return url.toString();
    }

    /**
     * 获取默认aliyun的oss地址
     *
     * @return
     */
    private String getPublicKeyURL() {
        String url = ossConfig.getEndpointForDownload();
        return url;
    }
}
