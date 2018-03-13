package com.web.common.web.common.algorithm;

/**
 * <pre>
 * 8皇后问题与回溯算法
 * </pre>
 *
 * @author: chengweixiong@uworks.cc
 * @date: 2016年5月21日 上午12:26:16
 */
public class EightQueens {

  private int n; // 皇后个数
  private int[] x; // 当前解
  private long sum; // 当前已找到的可行方案数
  // private static int h; // 记录遍历方案序数

  public EightQueens() {
    this.sum = 0; // 初始化方案数为1，当回溯到最佳方案的时候，就自增1
    this.n = 8; // 求n皇后问题，由自己定义
    this.x = new int[n + 1]; // x[i]表示皇后i放在棋盘的第i行的第x[i]列
    // h = 1; // 这个是我额外定义的变量，用于遍历方案的个数，请看backTrace()中h变量的作用，这里将它定义为static静态变量
  }

  public boolean place(int k) {
    for (int j = 1; j < k; j++) {
      // 这个主要是刷选符合皇后条件的解，因为皇后可以攻击与之同一行同一列的或同一斜线上的棋子
      if ((Math.abs(k - j)) == (Math.abs(x[j] - x[k])) || (x[j] == x[k])) {
        return false; // 如果是与之同一行同一列的或同一斜线上的棋子，返回false;
      }
    }
    return true;// 如果不是与之同一行同一列的或同一斜线上的棋子，返回true;
  }

  public void backTrace(int t) {
    if (t > n) { // 当t>n时,算法搜索到叶节点，得到一个新的n皇后互不攻击放置方案，方案数加1
      sum++; // 方案数自增1
      print(x);
      // 华丽的分割线
    } else { // 当t<=n时，当前扩展的结点Z是解空间中的内部结点，该节点有x[i]=1,2，…,n共n个子结点，
             // 对于当前扩展结点Z的每一个儿子结点，由place()方法检测其可行性，
             // 并以深度优先的方式递归地对可行子树搜索，或剪去不可行子数
      for (int i = 1; i <= n; i++) {
        x[t] = i;
        if (place(t)) { // 检查结点是否符合条件
          backTrace(t + 1); // 递归调用
        }
      }
    }
  }

  public void print(int[] a) {
    System.out.println("第" + sum + "种走法");

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (i == x[j]) {
          System.out.print("0 ");
        } else
          System.out.print("+ ");
      }
      System.out.println("");
    }
  }

  public static void main(String[] args) {
    EightQueens em = new EightQueens();
    em.backTrace(1); // 从1开始回溯
    System.out.println("详细方案如上所示，" + "可行个数为:" + em.sum);
  }
}
