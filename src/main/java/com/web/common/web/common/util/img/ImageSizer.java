package com.web.common.web.common.util.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageSizer {

    /**
     * @param originalFile 原图像
     * @param resizedFile  压缩后的图像
     * @param width        图像宽
     * @param format       图片格式 jpg, png, gif(非动画)
     * @throws IOException
     */
    public static void resize(final File originalFile, final File resizedFile, final int width,
        final String format) throws IOException {
        final BufferedImage src = ImageIO.read(originalFile);

        final int imageWidth = src.getWidth();
        if (imageWidth < 1) {
            throw new IllegalArgumentException("image width " + imageWidth + " is out of range");
        }
        final int imageHeight = src.getHeight();
        if (imageHeight < 1) {
            throw new IllegalArgumentException("image height " + imageHeight + " is out of range");
        }

        final BufferedImage dest = ImageScaler.scaleW(src, width);
        ImageIO.write(dest, format, resizedFile);
    }
}
