package com.huanmin.utils.arithmetic.lineardata;
/**
 * @author huanmin
 * @param <T>
 */

/**
 * 自定义链表接口定义
 **/
public abstract class LinkedAbs<T> implements Iterable<T> {
    //列表长度
    public int size = 0;

    //当前节点
    public Node head;
    //尾节点
    public Node end;

    //节点
    protected class Node {
        Node previous = null;//上一个结点
        Node next = null;//下一个结点
        T data;//结点数据

        public Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }

        public Node(Node next, Node previous) {
            this.next = next;
            this.previous = previous;
        }

        public Node(T data, Node next, Node previous) {
            this.next = next;
            this.previous = previous;
        }

        public Node(T data) {
            this.data = data;
        }

    }






    /**
     * 判断链表是否有环：
     * 有环返回环的入口节点,没有返回null
     * 设置快指针和慢指针，慢指针每次走一步，快指针每次走两步
     * 当快指针与慢指针相等时，就说明该链表有环,并且这个节点就是环的入口
     */
    public Node isRinged(){
        if(head == null){
            return null;
        }
          Node slow = head;
        Node fast = head;
        while(fast.next != null && fast.next.next != null){
            slow = slow.next;
            fast = fast.next.next;
            if(fast == slow){
                return fast;
            }
        }
        return null;
    }


    // 获取链表头元素
    public T getFrom() {
        return head.data;
    }

    //获取链表结尾元素
    public T getEnd() {

        return end.data;
    }

    //获取链表中元素个数
    public int getSize() {
        return size;
    }


    /**
     * 判断链表中是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }


    /**
     * 销毁链表
     */

    public void stackDestroy() {
        head = null;
        size = 0;
        end=null;
    }



    //寻找单链表的中间结点：
    public  abstract T findMiddle();


    /**
     * 元素反转
     */
    public abstract void reserveLink();

    /**
     * 获取指定元素
     *
     * @param index
     */
    public abstract T get(int index);


    /**
     * 向链表中添加元素
     *
     * @param e
     */
    public abstract void addFirst(T e);

    public abstract void addlast(T e);

    public abstract void add(T e);

    /**
     * 从链表中删除元素
     */
    public abstract boolean remove(T obj);

    public abstract boolean remove(int index);

    public abstract boolean removeFirst();

    public abstract boolean removeLast();


}
