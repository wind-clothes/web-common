package com.web.common.web.common.study;

public class Test {
  // 32位下对象头8字节，byte占1字节，对其填充后，总占16字节
  // 64位开启指针压缩下对象头12字节，byte1字节，对齐后占16字节
  class A {
    byte b1;
  }
  // 32位下对象头8字节，8个byte8字节，总16字节
  // 64位开启指针压缩下对象头12字节，8个byte8字节，对齐后占24字节
  class B {
    byte b1, b2, b3, b4, b5, b6, b7, b8;
  }
  // 32位下对象头8字节，9个byte9字节，对其填充后，总24字节
  // 64位开启指针压缩下对象头12字节，9个byte9字节，对齐后占24字节
  class C {
    byte b1, b2, b3, b4, b5, b6, b7, b8, b9;
  }
  // 32位下对象头8字节，int占4字节，引用占4字节，共16字节
  // 64位开启指针压缩下对象头12字节，int占4字节，引用占4字节，对齐后占24字节
  class D {
    int i;
    String str;
  }
  // 32位下对象头8字节，int4字节，byte占1字节，引用占4字节，对其后，共24字节
  // 64位开启指针压缩下对象头12字节，int占4字节，引用占4字节，byte占1字节，对齐后占24字节
  class E {
    int i;
    byte b;
    String str;
  }
  /**
   * 对齐有两种 1、整个对象8字节对齐 2、属性4字节对齐 ****
   *
   * 对象集成属性的排布 markword 4 8 class指针 4 4 父类的父类属性 1 1 属性对齐 3 3 父类的属性 1 1 属性对齐 3 3 当前类的属性 1 1 属性对齐填充 3
   * 3 整个对象对齐 8+12 =》 24 12+12=》24
   */
  class O {
    byte b;
  }
  class P extends O {
    byte b;
  }
  class Q extends P {
    byte b;
  }
}
