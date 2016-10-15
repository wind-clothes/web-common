package com.web.common.web.common.algorithm;

/**
 * <pre>
 * 水果
 * </pre>
 *
 * @date: 2016年5月20日 下午9:25:23
 */
class Fruit {
  private String name;
  private int size;
  private int price;

  public Fruit(String name, int size, int price) {
    this.name = name;
    this.size = size;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public int getPrice() {
    return price;
  }

  public int getSize() {
    return size;
  }
}


/**
 * <pre>
 * 动态规划
 * </pre>
 *
 * @author: chengweixiong@uworks.cc
 * @date: 2016年5月21日 上午12:21:41
 */
public class KnapsackProblem {
  public static void main(String[] args) {
    final int MAX = 8;
    final int MIN = 1;
    // item表示最後一個放至背包的水果;[0, 3, 2, 3, 0, 1, 3, 2, 3]
    int[] item = new int[MAX + 1];
    // 当前最优解的存放总价格
    int[] value = new int[MAX + 1];

    Fruit fruits[] = {new Fruit("李子", 4, 4500), new Fruit("苹果", 5, 5700), new Fruit("橘子", 2, 2250),
        new Fruit("草莓", 1, 1100), new Fruit("甜瓜", 6, 6700)};

    // 开始写代码，补充完整代码，用动态规划的方法实现背包问题
    for (int i = 0; i < fruits.length; i++) {
      int size = fruits[i].getSize();
      for (int j = size; j <= MAX; j++) {
        int p = j - size;
        int newValue = value[p] + fruits[i].getPrice();
        if (newValue > value[j]) {
          value[j] = newValue;
          item[j] = i;
        }
      }
    }


    System.out.println("物品\t价格");
    for (int i = MAX; i >= MIN; i = i - fruits[item[i]].getSize()) {
      System.out.println(fruits[item[i]].getName() + "\t" + fruits[item[i]].getPrice());
    }

    /**
     * [0, 1100, 2250, 3350, 4500, 5700, 6800, 7950, 9050]
     */
    System.out.println("合计\t" + value[MAX]);
  }
}
