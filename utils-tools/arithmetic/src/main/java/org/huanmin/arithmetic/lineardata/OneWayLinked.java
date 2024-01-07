package org.huanmin.arithmetic.lineardata;

import java.util.Iterator;

/**
 * 单向链表
 * @author huanmin
 * @param <T>
 */

public class OneWayLinked<T> extends LinkedAbs<T> {


    @Override
    public void reserveLink() {
        Node curNode = head;//头结点
        Node preNode = null;//前一个结点
        while(curNode != null){
            Node nextNode = curNode.next;//保留下一个结点
            curNode.next = preNode;//指针反转
            preNode = curNode;//前结点后移
            curNode = nextNode;//当前结点后移
        }
        head = preNode;
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

    /**
     * 查询指定下标数据
     * @param index
     * @return
     */

    @Override
    public T get(int index) {

        if(size<0 || index>size){//待查询结点不存在
            return null;
        }
        if(index == 0){//查询头结点
            return head.data;
        }

        Node curNode =head;
        int i = 0;
        while (curNode != null) {
            if(i==index){//寻找到待查询结点

                return  curNode.data;
            }
            //当先结点和前结点同时向后移
            curNode = curNode.next;
            i++;
        }
        return null;
    }

    @Override
    public void addFirst(T e) {

    }

    @Override
    public void addlast(T e) {

    }

    /**
     * 链表添加结点:
     * 找到链表的末尾结点，把新添加的数据作为末尾结点的后续结点
     *
     * @param data
     */
    @Override
    public void add(T data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            end=head;//添加尾节点
            size++;
            return;
        }
        Node temp = end;
        temp.next = newNode;
        end=newNode;//修改尾节点
        size++;
    }
    /**
     * 链表删除结点:
     * 把要删除结点的前结点指向要删除结点的后结点，即直接跳过待删除结点
     * @param obj
     * @return
     */
    @Override
    public boolean remove(T obj) {

        if (head.data.equals(obj)) {//删除头结点
            head = head.next;
            size=0;
            return true;
        }
        Node preNode = head;
        Node curNode = preNode.next;

        while (curNode != null) {
            if (curNode.data.equals(obj)) {//寻找到待删除结点
                preNode.next = curNode.next;//待删除结点的前结点指向待删除结点的后结点
                size--;
                return true;
            }
            //当先结点和前结点同时向后移
            preNode = preNode.next;
            curNode = curNode.next;
        }
        return  false;
    }

    @Override
    public boolean remove(int index) {
        if(size<0 || index>size){//待删除结点不存在
            return false;
        }
        if(index == 0){//删除头结点
            head = head.next;
            return true;
        }
        Node preNode = head;
        Node curNode =head.next;
        int i =1; //从第2个值开始
        while(preNode.next != null){
            if(i==index){//寻找到待删除结点
                preNode.next= curNode.next;//待删除结点的前结点指向待删除结点的后结点
                return true;
            }
            //当先结点和前结点同时向后移
            preNode=curNode;
            curNode = curNode.next;
            i++;
        }
        return false;
    }

    @Override
    public boolean removeFirst() {
        return false;
    }

    @Override
    public boolean removeLast() {
        return false;
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
                OneWayLinked.this.remove(data);
            }

        };
    }
}
