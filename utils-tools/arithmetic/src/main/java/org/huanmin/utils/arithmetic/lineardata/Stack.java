package org.huanmin.utils.arithmetic.lineardata;
/**
 * @author huanmin
 * @param <T>
 */
/**
 * 自定义栈接口定义
 **/
public interface Stack<T> {

    /**
     * 向栈中添加元素
     *
     * @param e
     */
    void push(T e);

    /**
     * 从栈中删除元素
     */
    T pop();

    /**
     * 获取栈顶元素
     *
     * @return
     */
    T peek();

    /**
     * 获取栈中元素个数
     *
     * @return
     */
    int getSize();

    /**
     * 判断栈中是否为空
     *
     * @return
     */
    boolean isEmpty();

    /**
     * 判断栈是否满
     * @return
     */
    boolean isFull();

    /**
     * 销毁栈
     */
    void stackDestroy();
}
