package com.web.common.web.common.algorithm;

import java.util.*;

/**
 * 使用两个堆栈模拟一个队列的先进先出效果
 * 
 * @date: 2016年5月24日 下午2:48:34
 */
public class TwoStackRealizeQueue {
  private Stack<Integer> stack1;
  private Stack<Integer> stack2;

  TwoStackRealizeQueue() {
    stack1 = new Stack<Integer>();
    stack2 = new Stack<Integer>();
  }

  // No.1
  // 开始写代码，用两个栈实现一个队列
  /**
   * 入队列
   * 
   * @param o
   * @return int
   */
  public int offer(int o) {
    return stack1.push(o);
  }

  /**
   * 先进先出队列
   * 
   * @param
   * @return int
   */
  public int poll() {
    if (stack2.isEmpty()) {
      while (!stack1.isEmpty()) {
        stack2.push(stack1.pop());
      }
    }
    if (stack2.isEmpty()) {
      throw new IllegalArgumentException("队列为空，不能删除");
    }
    return stack2.pop();
  }

  public static void main(String[] args) {
    TwoStackRealizeQueue queue = new TwoStackRealizeQueue();
    List<Integer> number = new ArrayList<Integer>();
    queue.offer(1);
    queue.offer(2);
    queue.offer(3);
    queue.offer(4);
    queue.offer(5);
    number.add(queue.poll());
    number.add(queue.poll());
    number.add(queue.poll());
    number.add(queue.poll());
    number.add(queue.poll());
    System.out.println(number);
  }
}
