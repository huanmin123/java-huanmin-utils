package com.huanmin.utils.common.obj.proxy;

import java.lang.reflect.Method;

public interface ActionProxy {

   void before( Method method, Object[] args);//进入方法后但是还未执行代码前

   void returnBefore( Method method, Object[] args); //方法返回前端处理

   void end( Method method, Object[] args);   //方法返回后后处理

   void exceptionHandling( Method method, Object[] args,Exception e); //异常处理

   //环绕增强  (非必选)
   default Object  round(Object o, Method method, Object[] args){
      return "notUse";
   };


}
