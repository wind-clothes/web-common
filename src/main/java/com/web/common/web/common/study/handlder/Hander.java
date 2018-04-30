package meituan.zylproxy.handlder;

import java.lang.reflect.Method;

import meituan.zylproxy.handlder.ZYLInvocationHandler;

public class Hander implements ZYLInvocationHandler {

    public Object target;

    public Hander(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        System.out.println("before");
        Object o = method.invoke(target, args);
        System.out.println("after");
        return o;
    }
}
