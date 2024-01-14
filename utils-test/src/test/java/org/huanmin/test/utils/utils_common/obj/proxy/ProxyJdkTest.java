package org.huanmin.test.utils.utils_common.obj.proxy;

import com.alibaba.fastjson.JSON;
import org.huanmin.utils.common.obj.proxy.jdk.ProxyJdkFactory;
import org.huanmin.utils.common.obj.proxy.jdk.ProxyJdkInstance;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;

public class ProxyJdkTest {
    private static final Logger logger = LoggerFactory.getLogger(ProxyJdkTest.class);
    //代理接口,统一接口实现
    @Test
    public void show1() {
        //创建接口方法,代理处理类   (类似于实现类,但是和实现类不同的是,全部接口都按照统一规则进行处理)
        InvocationHandler handler = new ProxyJdkFactory((method, arg)->{
            String s = JSON.toJSONString(arg);
            System.out.println(s);
            return  s;
        });
        //获取代理的实现类
        TestService object = ProxyJdkInstance.getObject(TestService.class, handler);
        String xxx = object.getList("xxx", "1111");
        System.out.println(xxx);
    }
    //代理对象,进行方法增强
    @Test
    public void show2() {
        //代理对象的方式,此对象必须继承接口,否则无法代理
        TestServiceImpl testService=new TestServiceImpl();
        InvocationHandler handler1 = new ProxyJdkFactory(new ActionProxyTestImpl(),testService);
        TestService object1 = ProxyJdkInstance.getObject(testService, handler1);
        String list = object1.getList("a", "a");
        System.out.println(list);
    }

}
