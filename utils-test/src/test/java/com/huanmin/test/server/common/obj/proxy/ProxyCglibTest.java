package com.huanmin.test.server.common.obj.proxy;

import com.alibaba.fastjson.JSON;

import com.utils.common.obj.proxy.cglib.ProxyCglibFactory;
import com.utils.common.obj.proxy.cglib.ProxyCglibInstance;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyCglibTest {
    private static final Logger logger = LoggerFactory.getLogger(ProxyCglibTest.class);
    //代理接口
    @Test
    public void test(){
        //目标对象
        ProxyCglibFactory proxyCglibFactory=new ProxyCglibFactory((method, arg)->{
            String s = JSON.toJSONString(arg);
            System.out.println(s);
            return  s;
        });
        //代理对象
        TestService object = ProxyCglibInstance.getObject(TestService.class, proxyCglibFactory);

        //执行代理对象的方法
        String list = object.getList("aa", "vvv");
        System.out.println(list);

    }

    //代理对象, 这个对象没有继承接口
    @Test
    public void test1(){
        //目标对象
        CglibTestImpl target = new CglibTestImpl();
        ProxyCglibFactory proxyCglibFactory=new ProxyCglibFactory(new ActionProxyTestImpl(),target);
        //代理对象
        CglibTestImpl proxy = ProxyCglibInstance.getObject(target,proxyCglibFactory);
        //执行代理对象的方法
        proxy.show();
    }
}
