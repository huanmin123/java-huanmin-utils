package com.utils.common.obj.proxy.jdk;




import com.utils.common.obj.proxy.ActionProxy;
import com.utils.common.obj.proxy.MethodImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProxyJdkFactory implements InvocationHandler {

    private  Object target;
    private ActionProxy actionProxy;

    public ProxyJdkFactory(ActionProxy actionProxy, Object target) {
        this.actionProxy = actionProxy;
        this.target=target;
    }

    private MethodImpl methodImpl;

    public ProxyJdkFactory(MethodImpl methodImpl) {
        this.methodImpl = methodImpl;
    }

    /**
     *
     * @param proxy 代理的对象也就是ServiceProxy他自己
     * @param method 被代理的方法
     * @param args 被代理的方法传入的参数
     * @return  返回被代理方法的执行的结果
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        //代理对象,对方法进行增强
        if (actionProxy!=null) {
            Object invoke = null;
            try {
                actionProxy.before(method,args);
                invoke = actionProxy.round( target,method, args); //环绕处理
                if ("notUse".equals(invoke)) {
                    invoke = method.invoke(target, args);
                }
                actionProxy.returnBefore(method,args);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                actionProxy.exceptionHandling(method,args,e);
                e.printStackTrace();
            }finally {
                actionProxy.end(method,args);
            }

            return invoke;
        } else {
            //代理接口,对所有接口进行统一逻辑实现
            return methodImpl.run(method,args);

        }

    }
}
