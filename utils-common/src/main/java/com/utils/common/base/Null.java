package com.utils.common.base;

import org.checkerframework.checker.units.qual.C;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * 高级链路对象判空处理 , 对象必须实现了get和set方法
 * @DTDO: 连级中断处理
 * @author huanmin
 * @date 2024/1/10
 */
//@SuppressWarnings("all")
public class Null {

    public static <T> ObjectNullHandler<T> of(T o) {
        if (o == null) {
            return ObjectNullHandler.empty();
        }
        return ObjectNullHandler.noEmpty(o);
    }

    //遇到空直接抛异常
    public static <T> ObjectNullHandler<T> no(T object) {
        return ObjectNullHandler.noEmpty(Objects.requireNonNull(object));
    }


    public static class ObjectNullHandler<T> {
        private boolean isNull; //true 为null ,false 不为null
        private T value;
        private StringBuffer linkLog = new StringBuffer();

        private ObjectNullHandler(T object, boolean isNull, StringBuffer linkLog) {
            this.isNull = isNull;
            this.value = object;
            if (object != null) {
                this.linkLog.append(object.getClass().getName()).append("->");
            }
            if (linkLog.length() > 0) {
                this.linkLog.append(linkLog);
            }
        }

        private static <T> ObjectNullHandler<T> empty() {
            return new ObjectNullHandler<>(null, true, new StringBuffer());
        }

        private static <T> ObjectNullHandler<T> empty(StringBuffer linkLog) {
            return new ObjectNullHandler<>(null, true, linkLog);
        }

        private static <T> ObjectNullHandler<T> noEmpty(T object) {

            return new ObjectNullHandler<>(object, false, new StringBuffer());
        }

        private static <T> ObjectNullHandler<T> noEmpty(T object, StringBuffer linkLog) {
            return new ObjectNullHandler<>(object, false, linkLog);
        }


        public <U> ObjectNullHandler<U> of(MyFun<? super T, ? extends U > function) {
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

        //如果是空直接抛异常
        public <U> ObjectNullHandler<U> no(MyFun<? super T, ? extends U> function) {
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


        //传入的是T返回的是U
        public <U> ObjectNullHandler<U> map(MyFun<? super T, ? extends U> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            if (isNull) {
                return empty();
            } else {
                return Null.of(mapper.apply(value));
            }
        }


        //如果value不为null,则返回value,否则返回新的ObjectNullHandler对象可以继续判断
        //这个好处就是a不行可以继续判断b,如果b不行可以继续判断c,如果c不行可以继续判断d
        public ObjectNullHandler<T> or(Supplier<? extends ObjectNullHandler<T>> supplier) {
            return !isNull ? this : supplier.get();
        }


        //  ==============================下面的是终结方法==============================================================


        //is,判断结果
        public boolean is() {
            return isNull;
        }

        //获取值,如果是空直接报错
        public T get() {
            if (isNull) {
                throw new NullPointerException(linkLog.toString());
            }
            return value;
        }

        //is,如果是true,则执行consumer,否则不执行
        public void is(Consumer<? super T> consumer) {
            if (!isNull) {
                consumer.accept(value);
            }
        }

        public void isOr(Consumer<? super T> action, Runnable emptyAction) {
            if (!isNull) {
                action.accept(value);
            } else {
                emptyAction.run();
            }
        }


        public <X extends Throwable> T orThrow(Supplier<? extends X> exceptionSupplier) throws X {
            if (!isNull) {
                return value;
            } else {
                throw exceptionSupplier.get();
            }
        }

        //如果value不为null,则返回value,否则返回defaultValue默认值
        public T orElse(T defaultValue) {
            return !isNull ? value : defaultValue;
        }

        //为什么会有toStr, 因为这个用的太频繁了, 很多时候都是拿到对象后直接获取对象内某个属性的字符串
        // String empty = ObjNull.isNull(userData).map(UserData::getName).toStr();  直接一条龙服务到位
        public String toStr() {
            if (isNull) {
                throw new NullPointerException(linkLog.toString());
            }
            if (value instanceof String) {
                if (((String) value).isEmpty()) {
                    throw new NullPointerException(linkLog.toString()+" ''空的字符串长度为0(这个不是null)");
                }
                return (String) value;
            }
            throw new NullPointerException(linkLog.toString()+" 注意上级调用不是字符串无法使用isEmpty方法");
        }
        //转换成Integer
        public Integer toInt() {
            if (isNull) {
                throw new NullPointerException(linkLog.toString());
            }
            if (value instanceof Number||value instanceof String) {
                try {
                    return Integer.parseInt(value.toString());
                } catch (NumberFormatException e) {
                    throw new NumberFormatException(linkLog.toString()+" ["+e.getMessage()+"] "+"请检查字符串中是纯数字吗?,一定不能包含其他字符的。");
                }
            }
            throw new NullPointerException(linkLog.toString()+" 注意上级调用不是Number或String无法使用intValue方法");
        }

        //这个和map的区别就是,这个是直接获取值,而map是获取值后还能继续判断
        public <U> U convert(MyFun<? super T, ? extends U> mapper) {
            if (isNull) {
                throw new NullPointerException(linkLog.toString());
            }
            return mapper.apply(value);
        }

        //将T转换成Stream<T> ,如果是null,则返回空的Stream<T>
        //并发流自己通过.parallel()转换
        public Stream<T> stream() {
            return !isNull ? Stream.of(value) : Stream.empty();
        }
        //转Optional,这是是为了兼容有些方法需要Optional对象的情况
        public Optional<T> optional() {
            return !isNull ? Optional.of(value) : Optional.empty();
        }

    }


    @FunctionalInterface
    public interface MyFun<T, R> extends java.util.function.Function<T, R>, Serializable {

    }

}
