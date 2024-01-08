package com.utils.arithmetic.lineardata;
/**
 * 自定义数组栈
 * @author huanmin
 * @param <T>
 */
public class ArrayStack<T> implements Stack<T> {

    private int stackSize; //栈最大容量,自动扩容
    private int base = -1; //栈底指针
    private int top; //栈顶指针
    private int popNum;//弹栈次数
    private final int CLEARTRASH = 1000; //垃圾个数

    //构建一个栈
    private Object[] arrayStack;

    //初始化栈
    public ArrayStack(Object... objects) {
        this.arrayStack = objects;
        this.stackSize = objects.length;

        this.top = objects.length - 1;
    }
    public ArrayStack(int stackSize) {
        this.stackSize = stackSize;
        this.top = -1;
        this.arrayStack = new Object[stackSize];
    }

    @Override
    public synchronized void push(T t) {
        if (!isFull()) {
            this.arrayStack[++top] = t;
        } else {
            //栈已满-进行扩容
            this.stackSize = this.stackSize + (int) (this.stackSize * 0.75 + 1);
            Object[] target = new Object[this.stackSize];
            //java最快数组copy方式
            System.arraycopy(this.arrayStack, 0, target, 0, this.arrayStack.length);
            this.arrayStack = target;//将原数组替换
            this.arrayStack[++top] = t;
        }
    }

    @Override
    public synchronized T pop() {
        if (!isEmpty()) {
            T t = (T) this.arrayStack[top--];
            if (popNum == CLEARTRASH) {
                //清除多余空间的内容
                this.stackSize = this.top + (int) (this.top * 0.75 + 1);
                if (stackSize != -1) {
                    Object[] target = new Object[stackSize];
                    System.arraycopy(this.arrayStack, 0, target, 0, top + 1);
                    this.arrayStack = target;
                }
                popNum = 0;
            }
            popNum++;
            return t;
        }
       System.out.println("栈为空，返回null");
        return null;
    }

    @Override
    public synchronized T peek() {
        if (!isEmpty()) {
            return (T) this.arrayStack[top];
        } else {
           System.out.println("栈为空，返回null");
            return null;
        }
    }

    @Override
    public synchronized int getSize() {
        return top - base;
    }

    //判断栈是否为空
    @Override
    public synchronized boolean isEmpty() {
        return base == top;
    }

    //判断栈是否满了
    @Override
    public synchronized boolean isFull() {
        return (top - base) >= stackSize;
    }


    @Override
    public synchronized void stackDestroy() {
        this.arrayStack = null;
        top = -1;
    }
}
