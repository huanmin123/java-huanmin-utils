package org.huanmin.test.utils.utils_common.obj.proxy;

import org.huanmin.utils.common.base.UniversalException;
import org.huanmin.utils.common.obj.proxy.ActionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ActionProxyTestImpl implements ActionProxy {
    private static final Logger logger = LoggerFactory.getLogger(ActionProxyTestImpl.class);
    @Override
    public void before( Method method, Object[] args) {
        System.out.println("我是前置处理");
    }

    @Override
    public void returnBefore( Method method, Object[] args) {
        System.out.println("我是返回前处理");
    }

    @Override
    public void end( Method method, Object[] args) {
        System.out.println("我是返回后处理");
    }

    @Override
    public void exceptionHandling( Method method, Object[] args, Exception e) {
        System.out.println("我是异常处理");
    }


    @Override
    public Object round(Object o, Method method, Object[] args) {
        System.out.println("我是环绕处理");
        Object invoke=null;
        try {
            invoke= method.invoke(o,args);
        } catch (InvocationTargetException | IllegalAccessException e) {
             UniversalException.logError(e);
        }

        return invoke;
    }
}
