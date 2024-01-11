package com.huanmin.utils.arithmetic.lineardata;
/**
 * 自定义链表栈
 * @author huanmin
 * @param <T>
 */
public class LinkedStack<T> implements Stack<T> {
    int currLength; //栈大小
    StackNode top; //栈顶指针，也是头结点
   private class StackNode{
        T nodeData; //结点值
        StackNode next; //指向下一个结点的引用
    }
    public LinkedStack() {
        currLength = 0;
        top = new StackNode();
    }

    @Override
    public synchronized void push(T e) {
        if (isEmpty()) {
            //说明这是一个空栈
            this.top.nodeData = e;

        } else {
            //将当前节点进行压栈,原来节点放入当前节点的下一个节点
            StackNode insertNode = new StackNode();
            insertNode.nodeData = e;
            insertNode.next = top;
            top = insertNode;
        }
        //列表深度
        currLength++;
    }

    @Override
    public synchronized T pop() {
        if (isEmpty()){
            return null;
        } else{
            if (top!=null) {
                //获取当前节点值
                T returnData = top.nodeData;
                //将下一个节点切换到顶节点
                top = top.next;
                //节点深度减少
                currLength--;
                return returnData;
            }
        }
        return null;
    }

    @Override
    public synchronized T peek() {
        return  this.top.nodeData;
    }

    @Override
    public synchronized int getSize() {
        return this.currLength;
    }

    @Override
    public synchronized boolean isEmpty() {
        return this.currLength==0;
    }

    @Override
    public synchronized boolean isFull() {
        return false;
    }

    @Override
    public synchronized void stackDestroy() {
        this.top.nodeData=null;
        this.top.next=null;
        this.top=null;
        this.currLength=0;
    }
}
