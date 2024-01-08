package com.utils.arithmetic.lineardata;

import java.util.Iterator;

/**
 * 双向循环链表
 * @param <T>
 * @author huanmin
 */

public class BothwayLoopLinked<T> extends LinkedAbs<T> {
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
            end= node1;
        }

        //调整位置
        head.previous=end;

        end.next=head;
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
        while (quickPoint.next != head && quickPoint.next.next != head) {
            slowPoint = slowPoint.next;
            quickPoint = quickPoint.next.next;
        }
        return slowPoint.data;
    }

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
        Node curNode = head.next;
        int i = 1;
        while ( curNode!= head) {
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
        previous.previous = head.previous;
        previous.next = next;
        next.previous = previous;
        head = previous;
        end.next=previous;//修改尾节点的指向
        size++;
    }

    @Override
    public void addlast(T e) {
        Node newNode = new Node(e);
        if (head == null) {
            head = newNode;
            head.previous=head;//环型
            head.next=head; //环型
            end=head;//添加尾节点
            size++;
            return;
        }
        Node temp = end;
        temp.next = newNode;
        newNode.previous = temp;
        newNode.next = head;//给为节点添加头节点(环型)
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
        //头部删除需要特殊处理
        if (obj == head.data) {
            Node previous = head.previous;
            head = head.next;
            head.previous = previous;
            end.next=head;
            size--;
            return true;
        }
        Node curNode = head.next;
        while (curNode != head) {
            //寻找到待删除结点
            if (curNode.data.equals(obj)) {
                //将删除的节点后节点,覆盖删除的节点,然后将父节点指向被删除元素的父节点
                Node previous = curNode.previous;
                Node next = curNode.next;
                if (next == null) {
                    //删除的是最后节点,那么就把他上一个节点的下一个节点删除
                    previous.next = null;
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
        if (removeHead()) {
            return true;
        }
        if (size < 0 || index >= size) {//待删除结点不存在
            return false;
        }
        //头部删除需要特殊处理
        if (index==0) {
            Node previous = head.previous;
            head = head.next;
            head.previous = previous;
            size--;
            return true;
        }
        Node curNode = head.next;
        int i = 1;
        while (curNode != null) {
            if (i == index) {//寻找到待删除结点
                //将删除的节点后节点,覆盖删除的节点,然后将父节点指向被删除元素的父节点
                Node previous = curNode.previous;
                Node next = curNode.next;
                if (next == null) {
                    //删除的是最后节点,那么就把他上一个节点的下一个节点给替换成头节点
                    previous.next = head;
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
        head = head.next;
        head.previous = end; //环绕
        end.next=head; //环绕
        size--;
        return false;
    }

    @Override
    public boolean removeLast() {
        //将删除结尾节点
        end.previous.next=head;
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
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            Node cursor = head;
            T data;

            @Override
            public boolean hasNext() {
                if (cursor != null&&cursor.next != head) {
                    data = cursor.data;
                    cursor = cursor.next;
                    return true;
                }

                if (cursor != null) {
                    data = cursor.data;
                    cursor = null;
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
                BothwayLoopLinked.this.remove(data);
            }

        };
    }
}