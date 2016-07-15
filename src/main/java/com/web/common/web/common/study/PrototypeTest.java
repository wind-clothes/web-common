package com.web.common.web.common.study;

/**
 * <pre>
 * </pre>
 *
 * @date: 2016年5月20日 下午9:15:52
 */
class Prototype implements Cloneable {
    //No.1
    //开始写代码，实现原型模式。Prototype类中的clone方法是不完整的，请将clone方法和main函数补充完整
    public Prototype clone() {
        Prototype prototype = null;
        try {
            prototype = (Prototype) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return prototype;
    }
    //end_code
}


/**
 * <pre>
 *  原型
 * </pre>
 *
 * @date: 2016年5月20日 下午9:21:25
 */
class ConcretePrototype extends Prototype {
    public void show() {
        System.out.println("原型模式");
    }
}


public class PrototypeTest {
    public static void main(String[] args) {
        ConcretePrototype concretePrototype = new ConcretePrototype();
        for (int i = 0; i < 5; i++) {
            ConcretePrototype cloneConcretePrototype =
                (ConcretePrototype) concretePrototype.clone();
            cloneConcretePrototype.show();
        }
    }
}
