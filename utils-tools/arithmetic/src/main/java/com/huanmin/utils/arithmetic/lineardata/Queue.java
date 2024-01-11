package com.huanmin.utils.arithmetic.lineardata;
/**
 * @author huanmin
 * @param <T>
 */
/**
 * 自定义队列接口定义
 **/
public interface Queue<T> {

    /**
     * 向队列中添加元素
     *
     * @param e
     */
    void push(T e);

    /**
     * 从队列中删除元素
     */
    T pop();

    /**
     * 获取队列顶元素
     *
     * @return
     */
    T peek();

    /**
     * 获取队列中元素个数
     *
     * @return
     */
    int getSize();

    /**
     * 判断队列中是否为空
     *
     * @return
     */
    boolean isEmpty();

    /**
     * 判断队列是否满
     * @return
     */
    boolean isFull();

    /**
     * 销毁队列
     */
    void queueDestroy();
}
