package com.huanmin.utils.null_chain.base;

import com.huanmin.utils.common.multithreading.executor.ExecutorUtil;
import com.huanmin.utils.common.multithreading.executor.ThreadFactoryUtil;
import com.huanmin.utils.null_chain.NULL;
import com.huanmin.utils.null_chain.NullBuild;
import com.huanmin.utils.null_chain.NullChainException;
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

    /**
     * 如果使用Void.TYPE作为返回值,那么就不会设置值,也不会唤醒下一个任务
     * 如果设置为NULL,那么就会设置值,但是不会唤醒下一个任务,并打印链路警告日志
     * @param consumer
     * @return
     * @param <U>
     */
    @Override
    public <U extends Serializable> NullChainAsync<U> async(NullFunEx<NullChain<? super T>,? extends U> consumer)  {
        NullChainAsync<U> nullChainAsync = new NullChainAsyncBase<>(this.key,linkLog);
        if ( this.key == null) {
            return nullChainAsync;
        }
        ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.NULL, () -> {
            try {
                Queue<Thread> threads = NullBuild.threadMap.get(this.key);
                if (threads == null) {
                    return;
                }
                threads.add(Thread.currentThread()); //保存线程追加到队列尾部
                LockSupport.park();//阻塞线程,等待唤醒

                Boolean aBoolean = NullBuild.stopMap.get(this.key);
                if (aBoolean != null && aBoolean) {//如果已经停止了就不需要再设置值了
                    NullBuild.stopVerifyMap.get(this.key).incrementAndGet();//增加计数器,为了清除资源
                    return;
                }
                Object res = NullBuild.resultMap.get(this.key);
                if (res == null) {//如果结果为空,那么就不需要再设置值了,结束之后的所有任务。
                    linkLog.append("async?");
                    log.warn(linkLog.toString());
                    NullBuild.stop(this.key);
                    return;
                }
                if (res.toString().equals(Void.TYPE.toString())){
                    NullBuild.stop(this.key);
                    return;
                }
                U apply = consumer.apply( NULL.of((T)res));
                if (apply==null){
                    linkLog.append("async?");
                    log.warn(linkLog.toString());
                    NullBuild.stop(this.key);
                    return;
                }
                NullBuild.resultMap.put(this.key, apply);
                //判断是否是最后一个任务,如果是最后一个任务,那么就清理资源
                if (threads.isEmpty()) {
                    NullBuild.clear(this.key);
                }

                //唤醒后一个任务
                Thread poll = threads.poll();
                LockSupport.unpark(poll);

            } catch (Throwable e) {
                NullChainException.logError(e,linkLog.toString());
            }
        });
        return nullChainAsync;
    }




}
