package com.utils.common.base;

import com.google.common.collect.Maps;
import com.utils.common.obj.reflect.MethodUtil;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 * @author huanmin
 * @date 2024/1/10
 */
public class LambdaUtil {
    public static<T> String fieldInvocation(Function<T,Object> myFun) {
        Map<String, String> stringStringMap = lambdaInvocation(myFun);
        return stringStringMap.get("fieldName");
    }

    public static<T> String methodInvocation(Function<T,Object> myFun) {
        Map<String, String> stringStringMap = lambdaInvocation(myFun);
        return stringStringMap.get("methodName");
    }

    /**
     * 获取lambda的 方法名,属性名,这样就可以使用反射了
     * methodInvocation(ProbationPageSearchParam::getProcessInstanceId);
     * @param myFun
     * @throws Exception
     */
    private static<T> Map<String,String> lambdaInvocation(Function<T,Object> myFun) {
        Map<String,String> map = null;
        try {
            if (myFun == null) {
                throw new UniversalException("methodInvocation获取方法名失败:{}",myFun);
            }
            // 直接调用writeReplace
            Method writeReplace = myFun.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            Object sl = writeReplace.invoke(myFun);
            SerializedLambda serializedLambda = (SerializedLambda) sl;
            map = Maps.newHashMap();
            map.put("methodName",serializedLambda.getImplMethodName());
            map.put("fieldName", MethodUtil.methodToProperty(serializedLambda.getImplMethodName()));

        } catch (Exception e) {
            throw  new UniversalException(e,"methodInvocation获取方法名失败:{}",myFun);
        }
        return map;
    }
}
