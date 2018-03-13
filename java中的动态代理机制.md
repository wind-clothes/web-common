### java中的动态代理机制

#### JDK
Java动态代理是利用反射机制生成一个实现代理接口的匿名类，在调用具体方法前调用InvokeHandler来处理。

```java
import java.lang.reflect.InvocationHandler;    
import java.lang.reflect.Method;    
import java.lang.reflect.Proxy;    
/**   
 *    
 * JDK动态代理类   
 */    
public class JDKProxy implements InvocationHandler {    
    
    private Object targetObject;//需要代理的目标对象    
    
    public Object newProxy(Object targetObject) {//将目标对象传入进行代理    
        this.targetObject = targetObject;     
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),    
                targetObject.getClass().getInterfaces(), this);//返回代理对象    
    }    
    
    @Overide
    public Object invoke(Object proxy, Method method, Object[] args)//invoke方法    
            throws Throwable {    
        checkPopedom();    //一般我们进行逻辑处理的函数比如这个地方是模拟检查权限    
        Object ret = null;      // 设置方法的返回值    
        ret  = method.invoke(targetObject, args);       //调用invoke方法，ret存储该方法的返回值    
        return ret;    
    }    
    
    private void checkPopedom() {//模拟检查权限的例子    
        System.out.println(".:检查权限  checkPopedom()!");    
    }    
}    
```

### Cglib
cglib动态代理是利用asm开源包，对代理对象类的class文件加载进来，通过修改其字节码生成子类来处理。

1、如果目标对象实现了接口，默认情况下会采用JDK的动态代理实现AOP 
2、如果目标对象实现了接口，可以强制使用CGLIB实现AOP 
3、如果目标对象没有实现了接口，必须采用CGLIB库，spring会自动在JDK动态代理和CGLIB之间转换

JDK中提供的生成动态代理类的机制有个鲜明的特点是： 某个类必须有实现的接口，而生成的代理类也只能代理某个类接口定义的方法，比如：如果上面例子的ElectricCar实现了继承自两个接口的方法外，另外实现了方法bee() ,则在产生的动态代理类中不会有这个方法了！更极端的情况是：如果某个类没有实现接口，那么这个类就不能用JDK产生动态代理了！

幸好我们有cglib，“CGLIB（Code Generation Library），是一个强大的，高性能，高质量的Code生成类库，它可以在运行期扩展Java类与实现Java接口。”
　　cglib 创建某个类A的动态代理类的模式是：
　　1.查找A上的所有非final 的public类型的方法定义；
　　2.将这些方法的定义转换成字节码；
　　3.将组成的字节码转换成相应的代理的class对象；
　　4.实现 MethodInterceptor接口，用来处理 对代理类上所有方法的请求（这个接口和JDK动态代理InvocationHandler的功能和角色是一样的）

如何强制使用CGLIB实现AOP？
 （1）添加CGLIB库，SPRING_HOME/cglib/*.jar
 （2）在spring配置文件中加入<aop:aspectj-autoproxy proxy-target-class="true"/>

```java
import java.lang.reflect.Method;    
    
import net.sf.cglib.proxy.Enhancer;    
import net.sf.cglib.proxy.MethodInterceptor;    
import net.sf.cglib.proxy.MethodProxy;    
    
/**   
 * CGLibProxy动态代理类的实例     
 */    
public class CGLibProxy implements MethodInterceptor {    
    
    private Object targetObject;// CGLib需要代理的目标对象    
    
    public Object createProxyObject(Object obj) {    
        this.targetObject = obj;    
        Enhancer enhancer = new Enhancer();    
        enhancer.setSuperclass(obj.getClass());    
        enhancer.setCallback(this);    
        Object proxyObj = enhancer.create();    
        return proxyObj;// 返回代理对象    
    }    
    
    public Object intercept(Object proxy, Method method, Object[] args,    
            MethodProxy methodProxy) throws Throwable {    
        Object obj = null;    
        if ("addUser".equals(method.getName())) {// 过滤方法    
            checkPopedom();// 检查权限    
        }    
        obj = method.invoke(targetObject, args);    
        return obj;    
    }    
    
    private void checkPopedom() {    
        System.out.println(".:检查权限  checkPopedom()!");    
    }    
}    
```

 ### 区别
JDK动态代理和CGLIB字节码生成的区别？
 （1）JDK动态代理只能对实现了接口的类生成代理，而不能针对类
 （2）CGLIB是针对类实现代理，主要是对指定的类生成一个子类，覆盖其中的方法
   因为是继承，所以该类或方法最好不要声明成final 
