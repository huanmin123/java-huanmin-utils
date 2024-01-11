package com.huanmin.utils.arithmetic.lineardata;

/**
 * 自定义链表队列
 * @author huanmin
 * @param <T>
 */
public class LinkedQueue<T> implements  Queue<T>{
    /**
     * 指向队头结点的引用
     */
    private Node<T> first;


    /**
     * 指向队尾结点的引用
     */
    private Node<T> end;
    /**
     * 元素个数
     */
    private int size;
    private static class Node<T> {
        //下一个结点的引用
        Node<T> next;
        //结点数据
        T data;

        //节点构造器
        public Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }
    }


    @Override
    public void push(T e) {
        //创建新节点
        Node<T> newNode = new Node<>(e, null);
        if (this.end == null) {
            this.end = newNode;
            this.first = newNode;
            this.size++;
            return;

        }
        //改变头节点,和尾节点 ,A-b-c-d
        this.end.next=newNode;// 这个动作其实是添加this.first的下一个节点 ,1,2,3,4
        this.end=newNode;    //存放最后节点元素
        this.size++;

    }

    @Override
    public T pop() {
        if (this.isEmpty()) {
           return  null;
        }
        T e = this.first.data;
        //改变队头节点引用,将下一个节点引用替换当前引用
        this.first = this.first.next;
        //如果元素为0,则将队尾节点引用置空
        if (--this.size == 0) {
            this.end = null;
        }
        return e;

    }

    @Override
    public T peek() {
        return this.first.data;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.first == null;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public void queueDestroy() {
        this.first=null;
        this.end=null;
        this.size=0;
    }
}
