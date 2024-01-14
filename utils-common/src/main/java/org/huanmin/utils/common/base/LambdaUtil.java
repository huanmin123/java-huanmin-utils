package org.huanmin.utils.common.base;

import com.google.common.collect.Maps;
import org.huanmin.utils.common.obj.reflect.MethodUtil;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

/**
 * @author huanmin
 * @date 2024/1/10
 */
public class LambdaUtil {
    public static<T,U> String fieldInvocation(Function<T,U> myFun) {
        Map<String, String> stringStringMap = lambdaInvocation(myFun);
        if (stringStringMap == null) {
            return null;
        }
        String fieldName = stringStringMap.get("fieldName");
        if (fieldName == null) {
            return null;
        }
        return fieldName;
    }

    public static<T,U> String methodInvocation(Function<T,U> myFun) {
        Map<String, String> stringStringMap = lambdaInvocation(myFun);
        if (stringStringMap == null) {
            return null;
        }
        String methodName = stringStringMap.get("methodName");
        if (methodName == null||methodName.startsWith("lambda$")) {
            return null;
        }
        return  methodName;
    }

    /**
     * 获取lambda的 方法名,属性名,这样就可以使用反射了 , 这个性能上没啥问题10万次调用耗时 10毫秒
     * methodInvocation(ProbationPageSearchParam::getProcessInstanceId);
     * @param myFun
     * @throws Exception
     */
    private static<T,U> Map<String,String> lambdaInvocation(Function<T,U> myFun) {
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
            return null;
//            throw  new UniversalException(e,"methodInvocation获取方法名失败:{}",myFun);
        }
        return map;
    }


}
