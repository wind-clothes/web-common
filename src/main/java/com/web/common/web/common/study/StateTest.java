package com.web.common.web.common.study;

/**
 * <pre>
 * </pre>
 *
 * @date: 2016年5月20日 下午9:47:30
 */
interface State {
    public void EatMo();
}


class Someone {
    private State state;

    public Someone(State state) {
        this.state = state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void EatMo() {
        state.EatMo();
    }
}
//No.1
//开始写代码，使用状态模式，构造NormalState、HungryState和FullState类


class NormalState implements State {
    private int num = 1;

    @Override public void EatMo() {
        System.out.println("I can eat 肉夹馍(个):" + num);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}


class HungryState implements State {

    private int num = 3;

    @Override public void EatMo() {
        System.out.println("I can eat 肉夹馍(个):" + num);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}


class FullState implements State {
    private int num = 0;

    @Override public void EatMo() {
        System.out.println("I can eat 肉夹馍(个):" + num);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}


//end_code
public class StateTest {
    public static void main(String[] args) {
        Someone someone = new Someone(new NormalState());
        someone.EatMo();
        someone.setState(new HungryState());
        someone.EatMo();
        someone.setState(new FullState());
        someone.EatMo();
    }
}
