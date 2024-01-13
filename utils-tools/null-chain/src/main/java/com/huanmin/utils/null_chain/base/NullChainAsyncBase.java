package com.huanmin.utils.null_chain.base;

import com.huanmin.utils.common.base.UniversalException;
import com.huanmin.utils.common.multithreading.executor.ExecutorUtil;
import com.huanmin.utils.common.multithreading.executor.ThreadFactoryUtil;
import com.huanmin.utils.null_chain.NULL;
import com.huanmin.utils.null_chain.NullBuild;
import com.huanmin.utils.null_chain.NullFun;
import com.huanmin.utils.null_chain.NullFunEx;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.locks.LockSupport;
@Slf4j
public class NullChainAsyncBase<T extends Serializable> implements NullChainAsync<T> {

    private final String key;
    protected StringBuffer linkLog;
    public NullChainAsyncBase(String key,StringBuffer linkLog) {
        this.key = key;
        this.linkLog = linkLog;
    }
    public NullChainAsyncBase(StringBuffer linkLog) {
        this.key = null;
        this.linkLog = linkLog;
    }
    @Override
    public <U extends Serializable> NullChainAsync<U> async(NullFunEx<NullChain<T>, U> consumer)  {
        NullChainAsync<U> nullChainAsync = new NullChainAsyncBase<>(this.key,linkLog);
        if ( this.key == null) {
            linkLog.append("async?");
            log.warn(linkLog.toString());
            return nullChainAsync;
        }
        ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.NULL, () -> {
            try {
                Queue<Thread> threads = NullBuild.threadMap.get(this.key);
                threads.add(Thread.currentThread()); //保存线程追加到队列尾部
                LockSupport.park();//阻塞线程,等待唤醒
                Boolean aBoolean = NullBuild.stopMap.get(this.key);
                if (aBoolean != null && aBoolean) {//如果已经停止了就不需要再设置值了
                    return;
                }
                Object res = NullBuild.resultMap.get(this.key);
                if (res == null||res.toString().equals(Void.TYPE.toString())) {//如果结果为空,那么就不需要再设置值了,结束之后的所有任务。
                    stop();
                    return;
                }
                U apply = consumer.apply(NULL.of((T) res));
                NullBuild.resultMap.put(this.key, apply);
                //唤醒后一个任务
                Thread poll = threads.poll();
                LockSupport.unpark(poll);
                //判断是否是最后一个任务,如果是最后一个任务,那么就清理资源
                if (threads.isEmpty()) {
                    clear();
                }
            } catch (Throwable e) {
                UniversalException.logError(e,linkLog.toString());
            }
        });
        return nullChainAsync;
    }


    private void stop() {
        ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.NULL, () -> {
            NullBuild.stopMap.put(this.key, true);
            NullBuild.resultMap.remove(this.key);
            Queue<Thread> threads = NullBuild.threadMap.get(this.key);
            if (threads != null) {//唤醒所有线程
                for (Thread thread : threads) {
                    LockSupport.unpark(thread);
                }
            }
            //等待1秒,让线程执行完毕
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            NullBuild.threadMap.remove(this.key);
            NullBuild.stopMap.remove(this.key);
        });
    }
    private void clear() {
        NullBuild.threadMap.remove(this.key);
        NullBuild.stopMap.remove(this.key);
        NullBuild.resultMap.remove(this.key);
    }

}
