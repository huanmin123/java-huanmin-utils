package com.huanmin.utils.common.obj.proxy;

import java.lang.reflect.Method;

@FunctionalInterface
public interface MethodImpl {
    Object run( Method method, Object[] args);//进入方法后但是还未执行代码前
}
