package com.utils.arithmetic.lineardata;

import java.util.Iterator;

/**
 * 双向链表
 * @author huanmin
 * @param <T>
 */

public class BothwayLinked<T> extends LinkedAbs<T> {
    /**
     *  查询指定下标数据
     * @param index
     * @return
     */
    @Override
    public T get(int index) {
        if (size < 0 || index > size) {//待查询结点不存在
            return null;
        }
        if (index == 0) {//查询头结点
            return head.data;
        }
        Node curNode = head;
        int i = 0;
        while (curNode != null) {
            if (i == index) {//寻找到待查询结点
                return curNode.data;
            }
            //当先结点和前结点同时向后移
            curNode = curNode.next;
            i++;
        }
        return null;
    }

    @Override
    public void addFirst(T e) {
        Node next = head;
        Node previous = new Node(e);
        previous.next = next;
        next.previous = previous;
        head=previous;
        size++;
    }

    @Override
    public void addlast(T e) {
        Node newNode = new Node(e);
        if (head == null) {
            head = newNode;
            size++;
            end=head;//添加尾节点
            return;
        }
        Node temp = end;
        temp.next = newNode;
        newNode.previous = temp;
        end=newNode;//修改尾节点
        size++;

    }

    @Override
    public void add(T e) {
        addlast(e);
    }

    @Override
    public boolean remove(T obj) {
        if (removeHead()) {
            return true;
        }
        Node curNode = head;
        while (curNode != null) {
            //寻找到待删除结点
            if (curNode.data.equals(obj)) {
                //将删除的节点后节点,覆盖删除的节点,然后将父节点指向被删除元素的父节点
                Node previous = curNode.previous;
                Node next = curNode.next;
                if (next == null) {
                    //删除的是最后节点,那么就把他上一个节点的下一个节点删除
                    previous.next=null;
                } else if (previous==null) {
                    //删除的是头节点的话,那么就不管父节点了
                    head=head.next;
                    head.previous=null;
                } else {
                    next.previous = previous;
                    previous.next = next;
                }
                size--;
                return true;
            }
            //当先结点向后移
            curNode = curNode.next;
        }
        return false;
    }

    @Override
    public boolean remove(int index) {

        if (index<0 ||index >= size) {//待删除结点不存在
            return false;
        }
        if (removeHead()) {
            return true;
        }
        Node curNode = head;
        int i = 0;
        while (curNode != null) {
            if (i == index) {//寻找到待删除结点
                //将删除的节点后节点,覆盖删除的节点,然后将父节点指向被删除元素的父节点
                Node previous = curNode.previous;
                Node next = curNode.next;
                if (next == null) {
                    //删除的是最后节点,那么就把他上一个节点的下一个节点删除
                    previous.next=null;
                } else if (previous==null) {
                    //删除的是头节点的话,那么就不管父节点了
                    head=head.next;
                    head.previous=null;
                } else {
                    next.previous = previous;
                    previous.next = next;
                }

                size--;
                return true;
            }
            //当先结点向后移
            curNode = curNode.next;
            i++;
        }
        return false;
    }

    @Override
    public boolean removeFirst() {
        if (removeHead()) {
            return true;
        }
        Node node = head.next;
        node.previous = null;
        head = node;
        size--;
        return false;
    }

    @Override
    public boolean removeLast() {
        if (removeHead()) {
            return true;
        }
        //删除尾节点
        end.previous.next=null;
        size--;
        return true;

    }
    //如果只有一个元素那么就将头删除
    public boolean removeHead() {
        if (head.next==null) {
            head=null;
            return true ;
        }
        return  false;
    }






    @Override
    public void reserveLink() {
        Object[] ts = new Object[size];
        int i = size - 1;
        for (T t : this) {
            ts[i] = t;
            i--;
        }

        Node node = head;
        node.data = (T) ts[0];
        for (int i1 = 1; i1 < ts.length; i1++) {
            Node node1 = new Node((T) ts[i1]);
            node.next = node1;
            node1.previous = node;
            node = node1;
        }

    }

    /**
     * 寻找单链表的中间结点：
     * 方法一、先求出链表的长度，再遍历1/2链表长度，寻找出链表的中间结点
     * 方法二、：
     * 用两个指针遍历链表，一个快指针、一个慢指针，
     * 快指针每次向前移动2个结点，慢指针一次向前移动一个结点，
     * 当快指针移动到链表的末尾，慢指针所在的位置即为中间结点所在的位置
     */
    @Override
    public T findMiddle() {
        Node slowPoint = head;
        Node quickPoint = head;
        //quickPoint.next == null是链表结点个数为奇数时，快指针已经走到最后了
        //quickPoint.next.next == null是链表结点数为偶数时，快指针已经走到倒数第二个结点了
        //链表结点个数为奇数时,返回的是中间结点；链表结点个数为偶数时，返回的是中间两个结点中的前一个
        while (quickPoint.next != null && quickPoint.next.next != null) {
            slowPoint = slowPoint.next;
            quickPoint = quickPoint.next.next;
        }
        return slowPoint.data;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            Node cursor = head;
            T data;

            @Override
            public boolean hasNext() {
                if (cursor != null) {
                    data = cursor.data;
                    cursor = cursor.next;
                    return true;
                }

                return false;
            }

            @Override
            public T next() {
                return data;
            }

            @Override
            public void remove() {
                BothwayLinked.this.remove(data);
            }

        };
    }
}
