package meituan.zylproxy;

/**
 * @author: xiongchengwei
 * @date:2018年4月20日 下午12:28:48
 */
import java.lang.reflect.Method;
import meituan.zylproxy.handlder.ZylProxy;
import meituan.zylproxy.handlder.ZYLInvocationHandler;
import meituan.zylproxy.test.i.Idto;
import meituan.zylproxy.test.i.Idto2;

public class IdtoPorxy extends ZylProxy implements Idto, Idto2 {
    public ZYLInvocationHandler zYLInvocationHandler;
    public static Method add1;
    public static Method get2;
    public static Method adda3;
    public static Method geta4;
    static {
        try {
            add1 = Class.forName("meituan.zylproxy.test.i.Idto").getMethod("add", new Class[0]);
            get2 = Class.forName("meituan.zylproxy.test.i.Idto").getMethod("get", new Class[0]);
            adda3 = Class.forName("meituan.zylproxy.test.i.Idto2").getMethod("adda", new Class[0]);
            geta4 = Class.forName("meituan.zylproxy.test.i.Idto2").getMethod("geta", new Class[0]);
        } catch (Exception e) {
        }
    }

    public IdtoPorxy(ZYLInvocationHandler zYLInvocationHandler) {
        this.zYLInvocationHandler = zYLInvocationHandler;
    }

    public void add() {
        Object[] o = {};
        try {
            this.zYLInvocationHandler.invoke(this, add1, o);
            return;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public java.lang.String get() {
        Object[] o = {};
        try {
            return (java.lang.String) this.zYLInvocationHandler.invoke(this, get2, o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void adda() {
        Object[] o = {};
        try {
            this.zYLInvocationHandler.invoke(this, adda3, o);
            return;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public java.lang.String geta() {
        Object[] o = {};
        try {
            return (java.lang.String) this.zYLInvocationHandler.invoke(this, geta4, o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
