package meituan.zylproxy.test;

import meituan.zylproxy.handlder.Hander;
import meituan.zylproxy.test.i.Idto;
import meituan.zylproxy.test.i.impl.DtoImpl;
import meituan.zylproxy.util.PorxyFactory;

public class ZylPorxyTest {

    public static void main(String[] args) throws Exception {
        Idto d = (Idto) PorxyFactory.newProxyInstancewWithMultiClass(DtoImpl.class.getInterfaces(),
                new Hander(new DtoImpl()));
        d.add();
        System.out.println(d.get());
    }
}
