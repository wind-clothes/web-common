package com.web.common.web.common.algorithm;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2016年7月8日 下午3:12:53
 */
public class DataStructure {


  public static long fibonacci(int num) {
    if (num == 0 || num == 1) {
      return num;
    }
    long count = 0L;
    long one = 0L;
    long two = 1L;
    for (int i = 2; i <= num; i++) {
      count = one + two;
      one = two;
      two = count;
    }
    return count;
  }

  public static void main(String[] args) {
    System.out.println(fibonacci(4));
  }
}
