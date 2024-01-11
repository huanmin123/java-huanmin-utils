package com.utils.null_chain;

import com.google.common.collect.Maps;
import com.utils.common.base.LambdaUtil;
import com.utils.common.base.UniversalException;
import com.utils.common.obj.copy.BeanCopyUtil;
import org.checkerframework.checker.units.qual.K;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public class NullChainDefault<T extends Serializable> extends NullConvertDefault<T> implements NullChain<T> {
    private static final long serialVersionUID = 123456791011L;

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
    @SuppressWarnings("unchecked")
    public <U extends Serializable> NullChain<U> of(NullFun<? super T, ? extends U> function) {
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
    public <U extends Serializable> NullChain<U> of(NullFun<? super T, ? extends U> function, Consumer<U> consumer) {
        NullChain<? extends U> nullChain = of(function);
        if (!nullChain.is()) {
            consumer.accept(nullChain.get());
        }
        return noEmpty(nullChain.get(), linkLog);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U extends Serializable> NullChain<U> no(NullFun<? super T, ? extends U> function) {
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
    public <U extends Serializable> NullChain<U> no(NullFun<? super T, ? extends U> function, Consumer<U> consumer) {
        NullChain<? extends U> nullChain = no(function);
        if (!nullChain.is()) {
            consumer.accept(nullChain.get());
        }
        return noEmpty(nullChain.get(), linkLog);
    }

    @Override
    public <U extends Serializable> NullChain<U> map(NullFun<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isNull) {
            return empty();
        } else {
            return NULL.of(mapper.apply(value));
        }
    }

    @Override
    public NullChain<T> or(Supplier<? extends NullChain<T>> supplier) {
        return !isNull ? this : supplier.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> NullChain<T> pick(List<NullFun<? super T, ? extends U>> mapper) {
        if (isNull) {
            return empty();
        }
        try {
            T object = (T) value.getClass().newInstance();
            for (NullFun<? super T, ? extends U> nullFun : mapper) {
                String methodName = LambdaUtil.methodInvocation(nullFun);
                //反射获取值
                Class<?> aClass = value.getClass();
                Method methodName1 = aClass.getMethod(methodName);
                Object invoke = methodName1.invoke(value);
                if (invoke != null) {
                    String field = LambdaUtil.fieldInvocation(nullFun);
                    //添加set方法
                    String firstLetter = field.substring(0, 1).toUpperCase();    //将属性的首字母转换为大写
                    String setMethodName = "set" + firstLetter + field.substring(1);
                    //获取方法对象,将值设置进去
                    object.getClass().getMethod(setMethodName, invoke.getClass()).invoke(object, invoke);
                }
            }
            return NULL.of(object);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SafeVarargs
    public final <U> NullChain<T> pick(NullFun<? super T, ? extends U>... mapper) {
        return pick(Arrays.asList(mapper));
    }


    @Override
    @SuppressWarnings("unchecked")
    public NullChain<T> copy() {
        return NULL.of(BeanCopyUtil.copy(value));
    }

    @Override
    public NullChain<T> deepCopy() {
        return NULL.of(BeanCopyUtil.deepCopy(value));
    }


    protected static <T extends Serializable> NullChain<T> empty() {
        return new NullChainDefault<>(null, true, new StringBuffer());
    }

    protected static <K, V extends Serializable> Map<K, NullChain<V>> emptyMap() {
        Map<K, V> map = new HashMap<>();
        return (Map<K, NullChain<V>>) map;
    }
    //空list
    protected static <T extends Serializable> List<NullChain<T>> emptyList() {
        List<T> list = new ArrayList<>();
        return (List<NullChain<T>>) list;
    }

    //空array
    protected static <T extends Serializable> NullChain<T>[] emptyArray() {
        return (NullChain<T>[]) new NullChain[0];
    }



    protected static <T extends Serializable> NullChain<T> empty(StringBuffer linkLog) {
        return new NullChainDefault<>(null, true, linkLog);
    }

    protected static <T extends Serializable> NullChain<T> noEmpty(T object) {

        return new NullChainDefault<>(object, false, new StringBuffer());
    }


    protected static <T extends Serializable> NullChain<T> noEmpty(T object, StringBuffer linkLog) {
        return new NullChainDefault<>(object, false, linkLog);
    }

    @Override
    public String toString() {
        return String.valueOf(isNull);
    }
}
