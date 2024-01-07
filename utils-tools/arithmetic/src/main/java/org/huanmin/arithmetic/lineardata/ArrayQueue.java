package org.huanmin.arithmetic.lineardata;
/**
 * 自定义数组队列
 * @author huanmin
 * @param <T>
 */
public class ArrayQueue<T> implements Queue<T> {

    private Object[] arrayQueue;//队列数组
    private int end = 0;//队尾下标
    private int begin=0;//队列开头
    private final int CLEARTRASH = 1000; //垃圾个数
    private  int length;

    public ArrayQueue(int size) {
        arrayQueue = new Object[size];
    }

    public ArrayQueue() {
        arrayQueue = new Object[30];
    }

    @Override
    public synchronized void push(T e) {
        if (!this.isFull()) {
            this.arrayQueue[this.end++] = e;
        } else {
            //队列满了进行扩容
            this.length = this.arrayQueue.length + (int) (this.arrayQueue.length * 0.75 + 1);
            Object[] target = new Object[this.length];
            //java最快数组copy方式
            System.arraycopy(this.arrayQueue, 0, target, 0, this.arrayQueue.length);
            this.arrayQueue = target;//将原数组替换

            this.arrayQueue[this.end++] = e;
        }

    }

    @Override
    public synchronized T pop() {
        if (this.isEmpty()) {
            System.out.println("队列为空,请先向队列中添加元素");
            return null;
        } else {
            T t = (T) this.arrayQueue[this.begin];
            if (this.begin == CLEARTRASH) {
                //队列向前移动,清理垃圾
                int i = this.CLEARTRASH + 1;
                int i1 =this.end-i ;
                Object[] target = new Object[this.length];
                System.arraycopy(this.arrayQueue, i, target, 0, i1);
                this.arrayQueue = target;
                this.begin = -1;
                this.end=this.end-i;
            }
            this.begin++;


            return t;
        }

    }


    @Override
    public synchronized T peek() {
        return (T) this.arrayQueue[this.begin];
    }

    @Override
    public synchronized int getSize() {
        return this.end;
    }

    @Override
    public synchronized boolean isEmpty() {
        if (this.arrayQueue == null) {
            return true;
        }
        return this.arrayQueue[this.begin] == null;
    }

    @Override
    public synchronized boolean isFull() {
        return this.arrayQueue.length == this.end;
    }

    @Override
    public synchronized void queueDestroy() {
        this.arrayQueue = null;
        this.end = 0;
    }
}
