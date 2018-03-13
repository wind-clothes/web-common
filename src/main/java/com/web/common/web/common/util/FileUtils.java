package com.web.common.web.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.codec.CharEncoding;

/**
 * 操作文件
 *
 * @author: chengweixiong@uworks.cc
 * @date: 2015年10月29日 上午11:13:16
 */
public class FileUtils {

    public static String readFileToString(File file) {
        try {
            return readStreamToString(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String readStreamToString(InputStream iStream) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br =
                new BufferedReader(new InputStreamReader(iStream, CharEncoding.UTF_8));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb = sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                iStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
