package com.web.common.web.common.study;


/**
 * <pre>
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2016年7月4日 上午11:09:15
 */
public class A{
  int a = 100;

  public A() {
    System.out.println("aaaa");
    System.out.println(a);
    a = 200;
  }

  public static void main(String[] args) {
    System.out.println(new A().a);
  }
}

class B {
   int b;
  public B() {
    System.out.println("bbbb"+b);
    System.out.println(this.b);
  }
}
