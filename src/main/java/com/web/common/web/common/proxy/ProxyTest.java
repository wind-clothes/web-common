package com.web.common.web.common.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <pre>
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2016年6月22日 下午2:36:45
 */
public class ProxyTest {

  public interface Proxys {

    public void test();
  }
  public class ProxyImpl implements Proxys {

    @Override
    public void test() {
      System.out.println("say ok!");
    }
  }
  public class ProxyHandler implements InvocationHandler {

    private Proxys proxyImpl;

    public ProxyHandler(Proxys proxyImpl) {
      this.proxyImpl = proxyImpl;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      method.invoke(proxyImpl, args);
      return null;
    }
  }

  public static void main(String[] args) {
    ProxyTest proxyTest = new ProxyTest();
    Proxys proxy =  proxyTest.new ProxyImpl();
    Proxys p = (Proxys) Proxy.newProxyInstance(ProxyTest.class.getClassLoader(), new Class[] {Proxys.class},
        proxyTest.new ProxyHandler(proxy));
    p.test();
  }
}
