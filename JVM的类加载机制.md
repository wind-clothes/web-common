JVM的类加载机制
==========================

## 1、ClassLoader抽象类
    类加载器的任务就是根据一个类的全限定名来读取此类的二进制字节流到JVM内部，然后转换为一个与目标类对应的java.lang.Class对象实例。

    如果需要支持类的动态加载或需要对编译后的字节码文件进行解密操作等，就需要与类加载器打交道了。

        - BootstrapClassLoader，由C++编写嵌套在JVM内部，负责加载“JAVA_HOME/lib”目录中的所有类型，或者由“-Xbootclasspath”指定路径中的所有类型。
        - ExtClassLoader和AppClassLoader都继承至ClassLoader抽象类，由Java编写。
        - ExtClassLoader负责加载“JAVA_HOME/lib/ext”目录下的所有类型。
        - AppClassLoader负责加载ClassPath目录中的所有类型。
    defineClass方法将字节码的byte数组转换为一个类的Class对象实例，如果希望在类被加载到JVM内部时就被链接，那么可以调用resolveClass方法。

## 2、双亲委派模型