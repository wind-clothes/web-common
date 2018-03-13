package com.web.common.web.common.redis;


//public class Test {
//  public static void main(String[] args) {
//    System.out.println((Long)10000000000L == 10000000000L);
//    staticFunction();
//    // 1
//    // 2
//    // 3
//    // a=110,b=112   b=0
//    // 4
//  }
//  static {
//    System.out.println("1");
//  }
//
//  static Test st = new Test();
//
//  {
//    System.out.println("2");
//  }
//
//  Test() {
//    System.out.println("3");
//    System.out.println("a=" + a + ",b=" + b);
//  }
//
//  public static void staticFunction() {
//    System.out.println("4");
//  }
//
//  int a = 110;
//  static int b = 112;
//}
 class SSClass
{
   static final int a = 2^32;
    static
    {
        System.out.println("SSClass");
    }
}    
 class SuperClass extends SSClass
{
    static
    {
        System.out.println("SuperClass init!");
    }
 
    public static int value = 123;
 
    public SuperClass()
    {
        System.out.println("init SuperClass");
    }
}
 class SubClass extends SuperClass
{
    static
    {
        System.out.println("SubClass init");
    }
 
    static int a;
 
    public SubClass()
    {
        System.out.println("init SubClass");
    }
}
public class Test
{
    public static void main(String[] args)
    {
      SubClass[] sca = new SubClass[10];
        System.out.println(SubClass.value);
    }
}
