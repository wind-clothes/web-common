package com.web.common.web.common.study;

/**
 * <pre>
 * 请求的实体
 * </pre>
 */
class Request {
    // 请求者的名称
    private String name;
    // 请求类型
    private String requestType;
    // 请求的天数
    private int number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}


/**
 * <pre>
 * 管理层基类
 * </pre>
 */
abstract class Manager {
    // 名称
    protected String name;
    // 直属上司
    protected Manager superior;

    public Manager(String name) {
        this.name = name;
    }

    public void SetSuperior(Manager superior) {
        this.superior = superior;
    }

    abstract public void requestApplications(Request requset);
}


// No.1
// 开始写代码，使用职责链模式，请构造CommonManager、Majordomo和GeneralManager这三个类


/**
 * <pre>
 * 经理
 * </pre>
 */
class CommonManager extends Manager {
    // 最大批准的天数
    private int maxDay = 0;
    // 最低能批准的天数
    private int minDay = 2;

    public CommonManager(String name) {
        super(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxDay() {
        return maxDay;
    }

    public int getMinDay() {
        return minDay;
    }

    public void setMaxDay(int maxDay) {
        this.maxDay = maxDay;
    }

    public void setMinDay(int minDay) {
        this.minDay = minDay;
    }

    @Override public void requestApplications(Request requset) {
        Manager manager = superior;
        int day = requset.getNumber();
        if (day < maxDay) {
            System.out
                .println(requset.getName() + "  " + requset.getRequestType() + "  " + name + "批准");
        } else {
            if (manager == null) {
                throw new IllegalArgumentException(" CommonManager sup is null");
            } else {
                manager.requestApplications(requset);
            }
        }
    }

}


/**
 * <pre>
 * 总监
 * </pre>
 */
class Majordomo extends Manager {
    // 最大批准的天数
    private int maxDay = 5;
    // 最低能批准的天数
    private int minDay = 2;

    public Majordomo(String name) {
        super(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxDay() {
        return maxDay;
    }

    public int getMinDay() {
        return minDay;
    }

    public void setMaxDay(int maxDay) {
        this.maxDay = maxDay;
    }

    public void setMinDay(int minDay) {
        this.minDay = minDay;
    }

    @Override public void requestApplications(Request requset) {
        Manager manager = superior;
        int day = requset.getNumber();
        if (day < maxDay && day > minDay) {
            System.out
                .println(requset.getName() + "  " + requset.getRequestType() + "  " + name + "批准");
        } else {
            if (manager == null) {
                throw new IllegalArgumentException(" Majordomo sup is null");
            } else {
                manager.requestApplications(requset);
            }
        }
    }
}


/**
 * <pre>
 * 总经理
 * </pre>
 */
class GeneralManager extends Manager {
    // 最大批准的天数
    private int maxDay = 10;
    // 最低能批准的天数
    private int minDay = 5;

    public GeneralManager(String name) {
        super(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxDay() {
        return maxDay;
    }

    public int getMinDay() {
        return minDay;
    }

    public void setMaxDay(int maxDay) {
        this.maxDay = maxDay;
    }

    public void setMinDay(int minDay) {
        this.minDay = minDay;
    }

    @Override public void requestApplications(Request requset) {
        int day = requset.getNumber();
        if (day < maxDay && day > minDay) {
            System.out
                .println(requset.getName() + "  " + requset.getRequestType() + "  " + name + "批准");
        } else {
            System.out
                .println(requset.getName() + requset.getRequestType() + day + "天不批准,总经理," + name);
        }
    }
}


// end_code
public class ChainOfResponsibilityTest {
    public static void main(String[] args) {
        CommonManager commonManager = new CommonManager("宋总");
        Majordomo majordomo = new Majordomo("赵总");
        GeneralManager generalManager = new GeneralManager("郑总");

        commonManager.SetSuperior(majordomo);
        majordomo.SetSuperior(generalManager);

        Request request = new Request();
        request.setName("老王");
        request.setRequestType("请假");
        request.setNumber(12);
        commonManager.requestApplications(request);
    }
}
