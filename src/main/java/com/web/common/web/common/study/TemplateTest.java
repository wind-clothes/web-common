package com.web.common.web.common.study;

/**
 * <pre>
 * 抽象模板
 * </pre>
 *
 * @date: 2016年5月20日 下午9:06:46
 */
abstract class AbstractDisplay {
    public abstract void print();

    /**
     * 模板方法，交由子类实现具体的内容
     *
     * @return void
     */
    public final void display() {
        print();
    }
}
// No.1
// 开始写代码，用模板模式实现输出一个字符和输出一个字符串，请自行构造CharDisplay类和StringDisplay类


/**
 * <pre>
 * 字符模板实现类
 * </pre>
 *
 * @date: 2016年5月20日 下午9:10:17
 */
class CharDisplay extends AbstractDisplay {
    private char sayWhat;

    /**
     * 构造方法
     *
     * @param sayWhat
     * @return void
     */
    public CharDisplay(char sayWhat) {
        this.sayWhat = sayWhat;
    }

    /**
     * 子类实现具体的内容
     */
    @Override public void print() {
        System.out.println("CharDisplay output is char:" + sayWhat);
    }

    public char getSayWhat() {
        return sayWhat;
    }

    public void setSayWhat(char sayWhat) {
        this.sayWhat = sayWhat;
    }

}


/**
 * <pre>
 * 字符串模板实现类
 * </pre>
 *
 * @date: 2016年5月20日 下午9:10:17
 */
class StringDisplay extends AbstractDisplay {
    private String sayWhat;

    /**
     * 构造方法
     *
     * @param sayWhat
     * @return void
     */
    public StringDisplay(String sayWhat) {
        this.sayWhat = sayWhat;
    }

    /**
     * 子类实现具体的内容
     */
    @Override public void print() {
        System.out.println("StringDisplay output is string:" + sayWhat);
    }

    public String getSayWhat() {
        return sayWhat;
    }

    public void setSayWhat(String sayWhat) {
        this.sayWhat = sayWhat;
    }

}


// end_code
public class TemplateTest {
    public static void main(String[] args) {
        AbstractDisplay abstractDisplayChar = new CharDisplay('A');
        AbstractDisplay abstractDisplayString = new StringDisplay("Hello World");
        abstractDisplayChar.display();
        abstractDisplayString.display();
    }
}
