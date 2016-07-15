package com.web.common.web.common.study;

import java.util.LinkedList;

/**
 * @date: 2016年5月25日 下午4:47:16
 */
public class GetNumber {
  private static LinkedList<Integer> numberList = new LinkedList<Integer>();

  /**
   * 分解可能的结果
   * 5+10
   * 6+9
   * 7+8
   * 2+7+6
   * 3+6+5
   * 1+5+4+3
   * 4+6+5
   * 2+5+4+3
   * 1+5+4+3+2
   * findSum(sum, n) = n + findSum(sum - n, n -1) 或者 findSum (sum, n) = findSum (sum, n -1)
   * @param sum
   * @param n
   * @return void
   */
  public static void getResolve(int sum, int n) {
    if (n < 1 || sum < 1)
      return;
    if (sum > n) {
      numberList.add(n);
      getResolve(sum-n, n-1);
      numberList.pop();
      getResolve(sum, n-1);
    } else {
      System.out.print(sum);
      for (int i = 0; i < numberList.size(); i++)
        System.out.print("+" + numberList.get(i));
      System.out.println();
    }
  }

  public static void main(String[] args) {
    int sum = 15;
    int n = 10;
    System.out.println(sum + "可以分解为：");
    getResolve(sum, n);
  }
}
