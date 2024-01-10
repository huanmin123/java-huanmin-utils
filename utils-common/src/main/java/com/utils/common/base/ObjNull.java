package com.utils.common.base;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.ognl.ObjectNullHandler;
import org.apache.logging.log4j.util.Strings;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 高级链路对象判空处理 , 对象必须实现了get和set方法
 *
 * @author huanmin
 * @date 2024/1/10
 */
public class ObjNull {

    public static <T> ObjectNullHandler<T> isNull(T o) {
        if (o == null) {
            return ObjectNullHandler.empty();
        }
        return ObjectNullHandler.noEmpty(o);
    }
    //遇到空直接抛异常
    public static <T> ObjectNullHandler<T>   noNull(T object) {
        return ObjectNullHandler.noEmpty(Objects.requireNonNull(object));
    }


    public static class ObjectNullHandler<T> {
        private boolean isNull; //true 为null ,false 不为null
        private T value;

        private ObjectNullHandler(T object, boolean isNull) {
            this.isNull = isNull;
            this.value = object;
        }

        private static <T> ObjectNullHandler<T> empty() {
            @SuppressWarnings("unchecked")
            ObjectNullHandler<T> t = new ObjectNullHandler<>(null, true);
            return t;
        }

        private static <T> ObjectNullHandler<T> noEmpty(T object) {
            return new ObjectNullHandler<>(object, false);
        }


        public ObjectNullHandler<T> isNull(MyFun<T, Object> function) {
            try {
                String methodName = LambdaUtil.methodInvocation(function);
                //反射获取值
                Class<?> aClass = value.getClass();
                Method methodName1 = aClass.getMethod(methodName);
                Object invoke = methodName1.invoke(value);
                if (invoke == null) {
                    isNull = true;
                }
                return this;
            } catch (Exception e) { //如果反射失败,表示获取不到方法
                isNull = true;
                return this;
            }
        }
        //如果是空直接抛异常
        public ObjectNullHandler<T> noNull(MyFun<T, Object> function) {
            try {
                String methodName = LambdaUtil.methodInvocation(function);
                //反射获取值
                Class<?> aClass = value.getClass();
                Method methodName1 = aClass.getMethod(methodName);
                Object invoke = methodName1.invoke(value);
                Objects.requireNonNull(invoke);
                return this;
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                throw new NullPointerException();
            }
        }


        //传入的是T返回的是U
        public <U> ObjectNullHandler<U> map(Function<? super T, ? extends U> mapper) {
            Objects.requireNonNull(mapper);
            if (isNull) {
                return empty();
            } else {
                return ObjNull.isNull(mapper.apply(value));
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
                throw new NullPointerException("值是空的不能获取");
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


        public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
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



        //将T转换成Stream<T> ,如果是null,则返回空的Stream<T>
        public Stream<T> stream() {
            return!isNull ? Stream.of(value) : Stream.empty();
        }

        //并发流
        public Stream<T> parallelStream() {
            return!isNull ? Stream.of(value).parallel() : Stream.empty();
        }

        //为什么会有isEmpty, 因为这个用的太频繁了, 很多时候都是拿到对象后直接获取对象内某个属性的字符串
        // String empty = ObjNull.isNull(userData).map(UserData::getName).isEmpty();  直接一条龙服务到位
        public String isEmpty() {
            if (value instanceof String) {
                if (((String) value).isEmpty()){
                    throw new NoSuchElementException("值是字符串类型的,但是值是空的不能进行获取");
                }
                return (String) value;
            }
            throw new NoSuchElementException("值不是字符串类型的不能获取");
        }

        //这个和map的区别就是,这个是直接获取值,而map是获取值后还能继续判断
        public <U> U convert(Function<? super T, ? extends U> mapper) {
            if (isNull){
                throw new NullPointerException("值是空的不能进行转换");
            }
            return mapper.apply(value);
        }
    }




    @FunctionalInterface
    private interface MyFun<T, R> extends java.util.function.Function<T, R>, Serializable {

    }

    public static void main(String[] args) {
        UserData userData = new UserData();
        userData.setName("111");
       ObjNull.isNull(userData).isNull(UserData::getAge).map(UserData::getAge);
       ObjNull.isNull(userData).isNull(UserData::getAge).map(UserData::getAge);

//        Integer convert = ObjNull.isNull(userData).map(UserData::getName).convert(Integer::parseInt);
//        System.out.println(convert);

        System.out.println();
    }

}
