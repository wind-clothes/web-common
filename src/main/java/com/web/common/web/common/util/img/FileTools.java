package com.web.common.web.common.util.img;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 图片文件存储，缩放等操作
 *
 * @author: chengweixiong@uworks.cc
 * @date:2015年10月22日 下午5:30:47
 */
public class FileTools {

    /**
     * 获得系统目录的路径, 结尾包含`/`符号.
     *
     * @param dirs
     * @return
     */
    public static String getApplicationPath(String... dirs) {
        StringBuilder builder = new StringBuilder(System.getProperty("user.dir") + File.separator);
        for (String s : dirs) {
            builder.append(s + File.separator);
        }
        return builder.toString();
    }

    /**
     * 获得文件后缀名.
     *
     * @param fileName
     * @return
     */
    public static String getFileExtension(String fileName) {
        String[] segs = fileName.split("\\.");
        return segs[segs.length - 1];
    }

    /**
     * 将文件以新名字移动到某个目录.
     *
     * @param file
     * @param dir
     */
    public static void writeTo(File file, String path, String fileName) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        file.renameTo(new File(path + fileName));
    }

    /**
     * 从指定文件夹中删除某个文件.
     *
     * @param fileName
     * @param dir
     */
    public static void deleteFile(String fileName, String... dirs) {
        if (fileName == null || dirs.length == 0)
            return;
        String dir = FileTools.getApplicationPath(dirs);
        File file = new File(dir + fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 将图片以某个限高保存起来.
     *
     * @param image
     * @param dirs
     * @return 保存后的图片名称
     */
    public static String saveImage(File image, int maxHeight, String... dirs) {
        if (image == null || dirs.length == 0)
            return null;
        String ext = FileTools.getFileExtension(image.getName());
        String dir = FileTools.getApplicationPath(dirs);
        String name = UUID.randomUUID().toString() + "." + ext;

        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        FileTools.resizeTo(image, dir, name, maxHeight);
        return name;
    }

    public static void saveImageThumbnail(File image, String filename, int maxHeight,
        String... dirs) {
        if (image == null || dirs.length == 0)
            return;
        String dir = FileTools.getApplicationPath(dirs);

        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        FileTools.resizeTo(image, dir, filename, maxHeight);
    }

    /**
     * 等比例缩放图片
     *
     * @param file
     * @param path
     * @param fileName
     * @param width    宽度
     */
    public static void resizeTo(File file, String path, String fileName, int height) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 等比例缩放图片
        try {
            ImageSizer.resize(file, new File(path + fileName), height, "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
