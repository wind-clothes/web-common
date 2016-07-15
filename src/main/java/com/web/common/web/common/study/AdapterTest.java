package com.web.common.web.common.study;

interface Target {
  public void Request();
}


class Adaptee {
  public void SpecificRequest() {
    System.out.println("这是原始标准接口！");
  }
}


// No.1
// 开始写代码，实现适配器模式，并且要求既有类适配器又有对象适配器，请实现AdapterObj和AdapterClass两个类
class AdapterObj extends Adaptee implements Target {

  @Override
  public void Request() {
    super.SpecificRequest();
  }
}


class AdapterClass implements Target {

  private Adaptee adaptee;

  AdapterClass() {
    adaptee = new Adaptee();
  }

  AdapterClass(Adaptee adaptee) {
    this.adaptee = adaptee;
  }

  @Override
  public void Request() {
    adaptee.SpecificRequest();
  }
}


public class AdapterTest {
  public static void main(String[] args) {
    AdapterObj adapterobj = new AdapterObj(); // 类适配器
    AdapterClass adapterclass = new AdapterClass();// 对象适配器
    adapterobj.Request();
    adapterclass.Request();
  }
}
