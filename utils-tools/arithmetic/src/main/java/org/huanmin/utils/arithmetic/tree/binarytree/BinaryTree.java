package org.huanmin.utils.arithmetic.tree.binarytree;


import org.huanmin.utils.common.obj.copy.BeanCopyUtil;

import java.util.*;
/**
 * @author huanmin
 */
public class BinaryTree<T> implements Tree<T> {
    //根节点
    private Node<T> root;

    public BinaryTree(List<T> list) {
        for (T t : list) {
            this.insert(t);
        }
    }

    public BinaryTree() {
    }

    @Override
    public T find(T key) {
        Node<T> node1 = new Node<>(key);
        return find(root,node1);
    }
    public T find( Node<T> node,  Node<T> node1) {
        //0. 当前节点等于查找的值
        if (node.getClassId()==node1.getClassId()) {
            return node.getData();
        }
        //1 .如果查找值的小于当前节点那么就往左找
        if (node.getClassId()>node1.getClassId()) {
            if (node.getLeftChild()!=null) {
               return find(node.getLeftChild(),node1);
            }
        }
        //2. 如果查找的值大于当前节点那么就往右找
        if (node.getClassId()<node1.getClassId()) {
            if (node.getRightChild() != null) {
                return find(node.getRightChild(),node1);
            }
        }
        //没有找到
        return null;
    }

    //(深度优先遍历) type =   1前序遍历   2中序遍历(有序列表(小到大))    3 后续遍历 ,4中序遍历(有序列表(大到小))
    @Override
    public List<T> findAll(int type) {
        //1. 查找全部节点,遍历全部左树,和右树 ,以及链表(需要递归)
        List<T> nodes = new ArrayList<>();
        findAll(nodes, root,type);
        return nodes;
    }

    // 用递归的方法
    private void findAll(List<T> nodes, Node<T> node,int type) {

        if (type==4) { //中序遍历(大到小)
            if (node.getRightChild() != null) {
                findAll(nodes, node.getRightChild(),type);
            }
            findAllAndLinked(nodes,node);
            if (node.getLeftChild() != null) {
                findAll(nodes, node.getLeftChild(),type);
            }
            return;
        }

        //保存当前节点值
        if (type==1) { //前序遍历
            findAllAndLinked(nodes,node);
        }
        //如果当前节点的子左节点不为空那么就往下递归
        if (node.getLeftChild() != null) {
            findAll(nodes, node.getLeftChild(),type);
        }
        if (type==2) { //中序遍历(小到大)
            findAllAndLinked(nodes,node);
        }
        //如果当前节点的子右节点不为空那么就往下递归
        if (node.getRightChild() != null) {
            findAll(nodes, node.getRightChild(),type);
        }
        if (type==3) {//后序遍历
            findAllAndLinked(nodes,node);
        }

    }

    //广度优先遍历
    @Override
    public ArrayList<T>breadthFindAll() {
        ArrayList<T> lists=new ArrayList<T>();
        if(root==null) {
            return lists;
        }
        Queue<Node<T>> queue=new LinkedList<Node<T>>();
        queue.offer(root);
        while(!queue.isEmpty()){
            Node<T> tree=queue.poll();
            if (tree.getLeftChild() != null) {

                queue.offer(tree.getLeftChild());
            }
            if (tree.getRightChild() != null) {

                queue.offer(tree.getRightChild());
            }
            this.findAllAndLinked(lists,tree);
        }
        return lists;
    }


    private void findAllAndLinked(List<T> nodes,Node<T> node) {
        //如果节点被删除那么跳过
        if (node.getData()==null) {
            return;
        }
        //保存当前节点值
        nodes.add(node.getData());
        //拿到链表的子链表
        Node<T>.Linked linked = node.getLinked();
        if (linked!=null) {
            //因为当前链表值就是当前节点的值,所以不需要添加
            linked=linked.getLinked();
        }
        //如果发现有链表那么,开启遍历链表
        while (linked != null) {
            //将子链表的值添加
            nodes.add(linked.getData());
            //获取子链表是否为空
            Node<T>.Linked linked1 = linked.getLinked();
            if (linked1 == null) {
                break;
            }
            //将链表引用保存
            linked = linked1;
        }
    }





    @Override
    public boolean insert(T data) {
        //1.先判断是否是空树,如果是那么当前插入的值作为树根
        if (root == null) {
            root = new Node<>(data);
            return true;
        }
        // 将值放入node节点中
        Node<T> node = new Node<>(data);
        //2.拿当前节点和值进行比较(小的找左,大的找右),直到找到最后节点为null结束 ,然后插入

        //记录当前查找的节点,默认都是从根节点开始找
        Node<T> current = root;
        while (true) {
            //1.1左树(比父节点小)
            if (current.getClassId() > node.getClassId()) {
                //获取左节点的值
                Node<T> leftChild = current.getLeftChild();
                //如果为空那么就找到最深处了,那么就可以添加值到左节点
                if (leftChild == null) {
                    current.setLeftChild(node);
                    return true;
                } else {
                    current = leftChild;
                }

            }
            //1.2右树(比父节点大)
            if (current.getClassId() < node.getClassId()) {
                //获取右节点的值
                Node<T> rightChild = current.getRightChild();
                //如果为空那么就找到最深处了,那么就可以添加值到右节点
                if (rightChild == null) {
                    current.setRightChild(node);
                    return true;
                } else {
                    current = rightChild;
                }
            }
            //1.3 节点相同 (转换链表)
            if (current.getClassId() == node.getClassId()) {
                if (current.getLinked() == null) {
                    //将链表初始化,把当前节点的值和要添加的值都放入链表中
                    Node<T>.Linked linked1 = new Node<T>().new Linked(node.getData());
                    Node<T>.Linked linked2 = new Node<T>().new Linked(current.getData(), linked1);
                    current.setLinked(linked2);
                    return true;
                } else {
                    //获取当节点的链表
                    Node<T>.Linked linked = current.getLinked();
                    while (true) {
                        //拿到连表的子链
                        Node<T>.Linked linked1 = linked.getLinked();
                        //判断子链是否为空,如果为空那么将当前重复的值挂载到这里
                        if (linked1 == null) {
                            Node<T>.Linked linked2 = new Node<T>().new Linked(node.getData());
                            linked.setLinked(linked2);
                            return true;
                        } else {
                            //如果不为空,继续往链表的子链表深入
                            linked = linked1;
                        }
                    }
                }

            }
        }

    }


    @Override
    public T findMax() {
        if (root == null) {
            return null;
        }
       return  findMax(root);
    }
    public T findMax(Node<T> node) {
        //1.原理就是查询所有节点的左子节点,最小的左子节点就是最小值
        if (node.getRightChild()==null) {
            return node.getData();
        }
        return findMin(node.getRightChild());
    }
    @Override
    public T findMin() {
        if (root == null) {
            return null;
        }
        return findMin(root);
    }
    public T findMin(Node<T> node) {
        //1.原理就是查询所有节点的左子节点,最小的左子节点就是最小值
        if (node.getLeftChild()==null) {
            return node.getData();
        }
        return findMin(node.getLeftChild());

    }
    @Override
    public boolean delete(T key) {
        //0.先查询到这个节点,和这个节点的父节点  [0]父节点 ,[1]当前节点
        Map<String, Object> map = this.deleteFind(key);
        //没有这个要删除的节点
        if (map==null) {
            return false;
        }
        Node<T> parent =(Node<T>) map.get("parent");
        Node<T> current =(Node<T>) map.get("current");
        String direction =(String) map.get("direction");

        //1.删除的元素没有子节点(左和右),那么直接删除当前节点
            if (current.getLeftChild()==null&&current.getRightChild()==null) {
                //将对象引用设置为空,加快垃圾收集器去回收这个空对象
                current.delete(current);//清空数据
                return true;
            }
        //2.1删除的元素有左子节点,就将这个左子节点代替当前节点
        if (current.getLeftChild()!=null&&current.getRightChild()==null) {
            //如果当前节点是在父节点的左边,那么就将当前节点的左子节点,覆盖当前节点的父节点的左子节点
            if(direction.equals("centre")) {
                root = current.getLeftChild();
            }else if (direction.equals("left")) {
                parent.setLeftChild(current.getLeftChild());
            } else {
                parent.setRightChild(current.getLeftChild());
            }
            return true;
        }
        //2.2删除的元素有右子节点,就将这个右子节点代替当前节点
        if (current.getLeftChild()==null&&current.getRightChild()!=null) {
            //如果当前节点是在父节点的右边,那么就将当前节点的右子节点,覆盖当前节点的父节点的右子节点
            if(direction.equals("centre")) {
                root = current.getRightChild();
            }else if (direction.equals("right")) {
                parent.setRightChild(current.getRightChild());
            } else {
                parent.setLeftChild(current.getRightChild());
            }
            return true;
        }

        //3.删除的元素有(左右)子节点 ,那么需要找到被删除节点的右子节点中的最小左子节点,然后替换删除的节点(注意:需要考虑最小节点有右节点的情况)
        if (current.getLeftChild()!=null&&current.getRightChild()!=null) {
            //3.1删除的元素右节点没有左节点的情况,将删除元素的右节点覆盖删除的节点,然后在将删除元素的左节点放入到删除元素的右节的左节点中
            Node<T> rightChild = current.getRightChild();
            if (rightChild.getLeftChild() == null&&direction.equals("centre")) {
                rightChild.setLeftChild(root.getLeftChild());
                root=rightChild;
                return  true;
            } else if (rightChild.getLeftChild() == null) {
                rightChild.setLeftChild(current.getLeftChild());
                parent.setRightChild(rightChild);
                return  true;
            }
            //删除元素右节点的最小左节点的父节点
            Node<T> minParentChild =current;
            //删除元素右节点的最小左节点
            Node<T> minChild =current.getRightChild();
            //3.2删除的元素右节点有左节点的情况,查询当前被删除的节点的右子节点的最小左子节点
            while (minChild.getLeftChild() != null) {
                minParentChild=minChild; //父节点
                minChild= minChild.getLeftChild(); //子节点
            }

         //3.3删除元素的右节点的最小左节点是否还存在右节点的情况 ,如果存在那么,就将右节点放入最小左节,然后将最小左节放入被删除的节点
         //移动顺序(不要弄反了不然会有问题的): 最小左节点的右节点 -> 最小左节点 -> 被删除节点
          if (minChild.getRightChild()!=null) {
              minParentChild.setLeftChild(minChild.getRightChild());
          }
         //3.4节点不存在右节点情况,将找到的最小左节点覆盖删除的节点,并且删除原来节点的位置
          if (minChild.getRightChild()==null) {
                minParentChild.setLeftChild(null);
          }
         //3.5(3.3和3.4)复用代码
            if (direction.equals("left")) {
                parent.setLeftChild(minChild);
                return true;
            } else if (direction.equals("centre")) {
                minChild.setLeftChild(root.getLeftChild());
                minChild.setRightChild(root.getRightChild());
                root=minChild;
                return true;
            } else {
                parent.setRightChild(minChild);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean update(T key) {
        try {
            T t = this.find(key);
            if (t==null) {
                return  false;
            }
            //方式1: 使用对象copy(反射方式), 如果出现问题那么就换成BeanCopier或者BeanUtils
            BeanCopyUtil.copy(key,t);
            //方式2:获取到修改的父节点,然后将需要修改的节点给覆盖了就行
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Map<String,Object> deleteFind(T key) {
        Map<String,Object> nodes=new HashMap<>();
        Node<T> node1 = new Node<>(key);
        nodes.put("parent",root);
        nodes.put("direction","centre");
        return deleteFind(nodes,root,node1);
    }
    private  Map<String,Object> deleteFind(Map<String,Object> nodes, Node<T> node,  Node<T> node1) {
        //0. 当前节点等于查找的值
        if (node.getClassId()==node1.getClassId()) {
            nodes.put("current",node); //当前节点
            return nodes;
        }
        //1 .如果查找值的小于当前节点那么就往左找
        if (node.getClassId()>node1.getClassId()) {
            nodes.put("parent",node);//记录父节点
            nodes.put("direction","left");//记录当前节点属于父节点哪一个方向
            if (node.getLeftChild()!=null) {
                return deleteFind(nodes,node.getLeftChild(),node1);
            }
        }
        //2. 如果查找的值大于当前节点那么就往右找
        if (node.getClassId()<node1.getClassId()) {
            nodes.put("parent",node);//记录父节点
            nodes.put("direction","right");//记录当前节点属于父节点哪一个方向
            if (node.getRightChild() != null) {
                return deleteFind(nodes,node.getRightChild(),node1);
            }
        }
        //没有找到
        return null;
    }
}
