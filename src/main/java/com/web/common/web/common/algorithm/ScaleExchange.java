package com.web.common.web.common.algorithm;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2016年7月9日 上午11:17:18
 */
public class ScaleExchange {

  public static String toHexString(Float num) {
    return Float.toHexString(num);
  }
  public static String toHexString(Integer num) {
    return Integer.toHexString(num);
  }

  public static void main(String[] args) {
    System.out.println(toHexString(3510593.0f));
    System.out.println(toHexString(3510593));
  }
}
