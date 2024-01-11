package com.utils.null_chain;

import com.utils.common.base.LambdaUtil;
import com.utils.common.base.UniversalException;
import com.utils.common.obj.copy.BeanCopierUtil;
import com.utils.common.obj.copy.BeanCopyUtil;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public  class NullChainDefault<T> extends  NullConvertDefault<T> implements NullChain<T>  {


    private NullChainDefault(T object, boolean isNull, StringBuffer linkLog) {
        this.isNull = isNull;
        this.value = object;
        if (object != null) {
            this.linkLog.append(object.getClass().getName()).append("->");
        }
        if (linkLog.length() > 0) {
            this.linkLog.append(linkLog);
        }
    }

    @Override
    public <U> NullChain<U> of(NullFun<? super T, ? extends U> function) {
        try {
            String methodName = LambdaUtil.methodInvocation(function);
            if (isNull) {
                return empty(linkLog);
            }
            //反射获取值
            Class<?> aClass = value.getClass();
            Method methodName1 = aClass.getMethod(methodName);
            Object invoke = methodName1.invoke(value);
            if (invoke == null) {
                linkLog.append(methodName).append("?");
                return empty(linkLog);
            } else {
                linkLog.append(methodName).append("->");
                return noEmpty((U) invoke, linkLog);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new UniversalException(e, "请检测类是否实现了get和set方法,建议在类上加上lombok的@Data注解");
        }
    }

    @Override
    public <U> NullChain<U> of(NullFun<? super T, ? extends U> function, Consumer<U> consumer) {
        NullChain<? extends U> nullChain = of(function);
        if(!nullChain.is()){
            consumer.accept(nullChain.get());
        }
        return noEmpty(nullChain.get(),linkLog);
    }

    @Override
    public <U> NullChain<U> no(NullFun<? super T, ? extends U> function) {
        try {
            String methodName = LambdaUtil.methodInvocation(function);
            if (isNull) {
                return empty(linkLog);
            }
            //反射获取值
            Class<?> aClass = value.getClass();
            Method methodName1 = aClass.getMethod(methodName);
            Object invoke = methodName1.invoke(value);
            if (invoke == null) {
                linkLog.append(methodName).append("?");
                throw new NullPointerException(linkLog.toString());
            } else {
                linkLog.append(methodName).append("->");
            }
            return noEmpty((U) invoke, linkLog);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new UniversalException(e, "请检测类是否实现了get和set方法,建议在类上加上lombok的@Data注解");
        }
    }

    @Override
    public <U> NullChain<U> no(NullFun<? super T, ? extends U> function, Consumer<U> consumer) {
        NullChain<? extends U> nullChain = no(function);
        if(!nullChain.is()){
            consumer.accept(nullChain.get());
        }
        return noEmpty(nullChain.get(),linkLog);
    }

    @Override
    public <U> NullChain<U> map(NullFun<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isNull) {
            return empty();
        } else {
            return Null.of(mapper.apply(value));
        }
    }

    @Override
    public NullChain<T> or(Supplier<? extends NullChain<T>> supplier) {
        return !isNull ? this : supplier.get();
    }



    @Override
    public <U> NullChain<U> pick(NullFun<? super T, ? extends U>... mapper) {
        return null;
    }

    @Override
    public NullChain<T> copy() {
        return null;
    }

    @Override
    public NullChain<T> deepCopy() {
        return null;
    }


    protected static <T> NullChain<T> empty() {
        return new NullChainDefault<>(null, true, new StringBuffer());
    }

    protected static <T> NullChain<T> empty(StringBuffer linkLog) {
        return new NullChainDefault<>(null, true, linkLog);
    }

    protected static <T> NullChain<T> noEmpty(T object) {

        return new NullChainDefault<>(object, false, new StringBuffer());
    }

    protected static <T> NullChain<T> noEmpty(T object, StringBuffer linkLog) {
        return new NullChainDefault<>(object, false, linkLog);
    }

    @Override
    public String toString() {
        return String.valueOf(isNull);
    }
}
