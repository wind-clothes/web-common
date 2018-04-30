package meituan.zylproxy.util;

import java.util.Map;

import meituan.zylproxy.DynamicLoader;
import meituan.zylproxy.handlder.ZYLInvocationHandler;

public class PorxyFactory {

    // 单interface的时候用
    public static Object newProxyInstance(Class<?> c, ZYLInvocationHandler h) throws Exception {

        String classStr = ClassUtil.mackProxyClass(c);
        Map<String, byte[]> m = DynamicLoader.compile(classStr);
        DynamicLoader.MemoryClassLoader classLoader = new DynamicLoader.MemoryClassLoader(m);
        Class<?> proxy = classLoader.loadClass(m.keySet().toArray(new String[0])[0]);
        return proxy.getConstructor(ZYLInvocationHandler.class).newInstance(h);
    }

    // 多interface的时候用
    public static Object newProxyInstancewWithMultiClass(Class<?>[] c, ZYLInvocationHandler h) throws Exception {

        // 根据代理的接口类型生成代理类的声明和定义，该代理类继承了所有接口，并实现其所有的方法，不过真实实现是ZYLInvocationHandler完成的
        String classStr = ClassUtil.mackMultiProxyClass(c);
        // System.out.println(classStr);
        // 根据类声明字符串获得类对应的字节码，并进行类本身的实例化
        Map<String, byte[]> m = DynamicLoader.compile(classStr);
        System.out.println("===========" + m.toString());
        // 通过类加载器加载对应的代理类字节码
        DynamicLoader.MemoryClassLoader classLoader = new DynamicLoader.MemoryClassLoader(m);
        // 类加载器之后获得对应的类本身的实例化对象
        Class<?> proxy = classLoader.loadClass(m.keySet().toArray(new String[0])[0]);
        // 实例化代理类
        return proxy.getConstructor(ZYLInvocationHandler.class).newInstance(h);
    }
}
