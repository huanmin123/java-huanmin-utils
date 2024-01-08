package com.utils.arithmetic.tree.binarytree;

import java.lang.reflect.Field;

/**
 * @author huanmin
 */
public class Node<T> {
    private T data;   //节点数据
    private Linked linked; //链表
    private Node<T> leftChild; //左子节点的引用
    private Node<T> rightChild; //右子节点的引用

    public Node(T data) {
        this.data = data;
    }

    public Node() {
    }

    public Linked getLinked() {
        return linked;
    }

    public void setLinked(Linked linked) {
        this.linked = linked;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node<T> leftChild) {
        this.leftChild = leftChild;
    }

    public Node<T> getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node<T> rightChild) {
        this.rightChild = rightChild;
    }

    //通过反射拿到对象内的id值  ,(不允许字符串类型 ,只能是数值类型,或者对象类型里有id字段)
    public long getClassId() {
        if (this.getData() instanceof Number) {
            Number data = (Number) (this.getData());
            return data.longValue();
        }
        long l = 0L;
        try {
            Class<?> aClass = this.getData().getClass();
            Field field = aClass.getDeclaredField("id");
            field.setAccessible(true);
            l = (long) field.get(this.getData());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return l;
    }



    //当数据发生重复时候转换为链表
    protected class Linked {
        private T data; //当前
        private Linked linked; //下一个

        public Linked(T data, Linked linked) {
            this.data = data;
            this.linked = linked;
        }
        public Linked(T data) {
            this.data = data;

        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Linked getLinked() {
            return linked;
        }

        public void setLinked(Linked linked) {
            this.linked = linked;
        }
    }


    public  void delete(Node<T> node) {
        this.setData(null);
        this.setLinked(null);
        this.setRightChild(null);
        this.setLeftChild(null);
        node=null;

    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                '}';
    }
}
