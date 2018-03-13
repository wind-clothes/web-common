package com.web.common.web.common.study;

/**
 * <pre>
 * </pre>
 *
 * @author: xiongchengwei
 * @date: 2016年4月1日 下午4:50:51
 */
public class JniStudy {

    static {
        //System.loadLibrary("hello");
    }

    public JniStudy() {
        super();
    }

    public native void SayHello(String strName);

    public static void main(String[] args) {
      System.out.println("a".hashCode());
    }
}
