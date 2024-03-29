package org.huanmin.utils.null_chain.base;

import org.huanmin.utils.common.base.LambdaUtil;
import org.huanmin.utils.common.obj.copy.BeanCopyUtil;
import org.huanmin.utils.null_chain.NULL;
import org.huanmin.utils.null_chain.common.NullBuild;
import org.huanmin.utils.null_chain.common.NullChainException;
import org.huanmin.utils.null_chain.common.NullExecutor;
import org.huanmin.utils.null_chain.common.NullFunEx;
import org.huanmin.utils.null_chain.common.NullFun;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author huanmin
 * @date 2024/1/11
 */
@Slf4j
public class NullChainBase<T extends Serializable> extends NullConvertBase<T> implements NullChain<T> {
    private static final long serialVersionUID = 123456791011L;

    public NullChainBase(T object, boolean isNull, StringBuffer linkLog) {
        this.isNull = isNull;
        this.value = object;
        if (linkLog.length() > 0) {
            this.linkLog.append(linkLog);
        }
    }



    @Override
    @SuppressWarnings("unchecked")
    public <U extends Serializable> NullChain<U> of(NullFun<? super T, ? extends U> function) {
        try {
            if (isNull) {
                return NullBuild.empty(linkLog);
            }
            String methodName = LambdaUtil.methodInvocation(function);
            if (methodName == null) {
                NULL.addHeadLog(linkLog);
                linkLog.append("of? 请检查是否使用了lambda表达式  [类::方法]  的形式");
                throw new NullChainException(linkLog.toString());
            }
            //反射获取值
            Class<?> aClass = value.getClass();
            Method methodName1 = aClass.getMethod(methodName);
            Object invoke = methodName1.invoke(value);
            if (invoke == null) {
                linkLog.append("of?");
                return NullBuild.empty(linkLog);
            } else {
                linkLog.append("of->");
                return NullBuild.noEmpty((U) invoke, linkLog);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new NullChainException(e, "请检测类是否实现了get和set方法,建议在类上加上lombok的@Data注解");
        }
    }

    @Override
    public <U extends Serializable> NullChain<U> of(NullFun<? super T, ? extends U> function, Consumer<U> consumer) {
        NullChain<? extends U> nullChain = of(function);
        if (!nullChain.is()) {
            consumer.accept(nullChain.get());
        }
        return NullBuild.noEmpty(nullChain.get(), linkLog);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U extends Serializable> NullChain<U> no(NullFun<? super T, ? extends U> function) {
        try {
            if (isNull) {
                NULL.addHeadLog(linkLog);
                throw new NullChainException(linkLog.toString());
            }
            String methodName = LambdaUtil.methodInvocation(function);
            if (methodName == null) {
                NULL.addHeadLog(linkLog);
                linkLog.append("no? 请检查是否使用了lambda表达式  [类::方法]  的形式");
                throw new NullChainException(linkLog.toString());
            }
            //反射获取值
            Class<?> aClass = value.getClass();
            Method methodName1 = aClass.getMethod(methodName);
            Object invoke = methodName1.invoke(value);
            if (invoke == null) {
                NULL.addHeadLog(linkLog);
                linkLog.append("no?");
                throw new NullChainException(linkLog.toString());
            } else {
                linkLog.append("no->");
            }
            return NullBuild.noEmpty((U) invoke, linkLog);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new NullChainException(e, "请检测类是否实现了get和set方法,建议在类上加上lombok的@Data注解");
        }
    }

    @Override
    public <U extends Serializable> NullChain<U> no(NullFun<? super T, ? extends U> function, Consumer<U> consumer) {
        NullChain<? extends U> nullChain = no(function);
        if (!nullChain.is()) {
            consumer.accept(nullChain.get());
        }
        return NullBuild.noEmpty(nullChain.get(), linkLog);
    }

    @Override
    public <U extends Serializable> NullChain<U> map(NullFun<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isNull) {
            return NullBuild.empty(linkLog);
        } else {
            linkLog.append("map->");
            return NULL.of(mapper.apply(value));
        }
    }

    @Override
    public NullChain<T> or(Supplier<? extends NullChain<T>> supplier) {
        return !isNull ? this : supplier.get();
    }

    @Override
    public <U  extends Serializable> NullChain<U> convert(NullFun<? super T, ? extends U> mapper) {
        if (isNull) {
            return NullBuild.empty(linkLog);
        }
        linkLog.append("convert->");
        U apply = mapper.apply(value);
        return NULL.of(apply);
    }


    @Override
    @SuppressWarnings("unchecked")
    public <U> NullChain<T> pick(List<NullFun<? super T, ? extends U>> mapper) {
        if (isNull) {
            return NullBuild.empty(linkLog);
        }
        linkLog.append("pick");
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
                }else{
                    linkLog.append(methodName1.getName()).append("?");
                }
            }
            linkLog.append("->");
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
        if (isNull) {
            return NullBuild.empty(linkLog);
        }
        linkLog.append("copy->");
        return NULL.of(BeanCopyUtil.copy(value));
    }

    @Override
    public NullChain<T> deepCopy() {
        if (isNull) {
            return NullBuild.empty(linkLog);
        }
        linkLog.append("deepCopy->");
        return NULL.of(BeanCopyUtil.deepCopy(value));
    }

    @Override
    public  <U extends Serializable> NullChainAsync<U> async(NullFunEx<NullChain<? super T>,? extends U> consumer) throws NullChainException  {
        if (isNull) {
            log.warn(linkLog.toString());
            return new NullChainAsyncBase<>(linkLog);
        }
        String key = UUID.randomUUID().toString();
        NullChainAsync<U> nullChainAsync = new NullChainAsyncBase<>(key,linkLog);
        NullExecutor.create().execute(()->{
            try {
                NullBuild.threadMap.put(key, new ConcurrentLinkedQueue<>());
                NullBuild.stopMap.put(key, false);
                NullBuild.stopVerifyMap.put(key, new AtomicInteger(0));
                T t = this.get();//获取值
                U apply = consumer.apply(NULL.of(t));
                if (apply==null){
                    linkLog.append("async?");
                    throw  new  NullChainException(linkLog.toString());
                }
                linkLog.append("async->");
                NullBuild.resultMap.put(key,apply );
                //唤醒后一个任务
                Queue<Thread> threads = NullBuild.threadMap.get(key);
                Thread poll = threads.poll();
                LockSupport.unpark(poll);

            } catch (Throwable e) {
                NullBuild.stop(key);
                NullChainException.logError(e);
            }
        });
        return nullChainAsync;
    }


}
