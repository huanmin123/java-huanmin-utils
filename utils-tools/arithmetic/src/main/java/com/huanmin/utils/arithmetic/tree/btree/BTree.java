package com.huanmin.utils.arithmetic.tree.btree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 *
 * @author huanmin
 */
public class BTree<T> {
    private final int degree;
    private BTreeNode<T> root;
    private final int ceil; //分割值

    //初始化阶级
    public BTree(int degree) {
        //阶不能小于2
        if (degree < 2) {
            throw new IllegalArgumentException("degree mustn't < 2");
        }
        this.degree = degree;
        //计算分割下标
        this.ceil = (int) Math.ceil((double) degree / 2) - 1;
    }


    class BTreeNode<n> {

        private int keySiez;//keys 从1开始计算
        private Object[] keys;  //存储key   (这个key我们可以设计一个key,value形式的对象,利用反射)
        private int sonNodeSize;//nodeSize 从1开始计算
        private BTreeNode<n>[] sonbTreeNodes; //存储全部子节点
        private BTreeNode<n> parent; //存储父节点,这样便于操作

        private String sign; //标记,用于特殊方面...


        public BTreeNode() {
            //设置最大key的长度(degree-1)    ,要多预留一个用于交互(degree)
            this.keys = new Object[degree];
            //设置子节点最大长度(degree) ,要多预留一个用于交互(degree+1)
            this.sonbTreeNodes = new BTreeNode[degree + 1];
        }


        private int getKeyLength() {
            return keySiez;
        }

        //判断节点的key是否满了
        private boolean getKeyFull() {
            return keySiez == degree;
        }

        private int getSonNodeSize() {
            return sonNodeSize;
        }

        private void setSonbTreeNodes(BTreeNode<n>[] bTreeNode, Integer index) {

            if (index == null) {
                for (BTreeNode<n> nBTreeNode : bTreeNode) {
                    sonbTreeNodes[sonNodeSize] = nBTreeNode;
                    sonNodeSize++;
                }
                return;
            }
            for (BTreeNode<n> nBTreeNode : bTreeNode) {
                sonbTreeNodes[index] = nBTreeNode;
                index++;
                sonNodeSize++;
            }
        }

        private void setSonbTreeNode(BTreeNode<n> bTreeNode) {
            sonbTreeNodes[sonNodeSize] = bTreeNode;
            sonNodeSize++;
        }


        private synchronized void setKey(n key) {
            //如果是空的那么直接插入
            if (keys[0] == null) {
                keys[0] = key;
                keySiez++;
                return;
            }

            //进行从小到大插入值
            //1.先获取插入的位置
            Integer location = null;
            long newKeyId = getClassId(key);
            for (int i = 0; i < getKeyLength(); i++) {
                long OldKeyId = getClassId(keys[i]);
                //拿到插入的下标
                if (OldKeyId > newKeyId) {
                    location = i;
                    break;
                }
            }
            if (location == null) {
                //表示已经查询到最后了也没有比他大的值了那么直接就插入到最后null位置
                keys[getKeyLength()] = key;
                keySiez++;
                return;
            }

            //从下标以后的值往后移动
            moveArrayKey(keys, getKeyLength(), location, 1);
            //将值插入到指定位置
            keys[location] = key;
            //当前值的个数加一
            keySiez++;
        }


        //分裂成2份返回中间值
        private Map<String, Object> bTreeNodeSplit(BTreeNode<n> parent) {

            Map<String, Object> map = new HashMap<>();
            map.put("center", keys[ceil]);

            BTreeNode<n> bTreeNode1 = new BTreeNode<n>();
            //值的分割,中间值不要
            for (int i = 0; i < ceil; i++) {
                bTreeNode1.setKey((n) keys[i]);
            }

            BTreeNode<n> bTreeNode2 = new BTreeNode<n>();
            for (int i = ceil + 1; i < getKeyLength(); i++) {
                bTreeNode2.setKey((n) keys[i]);

            }

            //将子节点的引用分配给分割后的节点,并且调整父节点引用
            if (getSonNodeSize() > 0) {
                //满足分割条件,子节点>0 那么一定是 key+1的个数
                for (int i = 0; i <= ceil; i++) {
                    if (sonbTreeNodes[i] != null) {
                        bTreeNode1.setSonbTreeNode(sonbTreeNodes[i]);
                        sonbTreeNodes[i].parent = bTreeNode1;
                    }
                }
                for (int i = ceil + 1; i < getSonNodeSize(); i++) {
                    if (sonbTreeNodes[i] != null) {
                        bTreeNode2.setSonbTreeNode(sonbTreeNodes[i]);
                        sonbTreeNodes[i].parent = bTreeNode2;
                    }
                }
            }


            BTreeNode<n>[] sonbTreeNodes = new BTreeNode[2];
            sonbTreeNodes[0] = bTreeNode1;
            sonbTreeNodes[1] = bTreeNode2;
            map.put("sonbTreeNodes", sonbTreeNodes);


            //添加新节点的父节点
            bTreeNode1.parent = parent;
            bTreeNode2.parent = parent;


            //移动节点位置(一般分裂都是2),清除原来旧节点,然后空出需要插入节点的位置,返回新插入起始下标
            Integer integer = moveNode(parent, this, 2);
            map.put("fromIdnex", integer);
            return map;

        }

        /**
         * 删除旧节点,空出需要插入节点的位置
         *
         * @param parent  父节点
         * @param delNode 需要删除的节点
         * @param size    需要插入的节点个数
         * @return 返回需要插入下标的起始位置
         */
        private Integer moveNode(BTreeNode<n> parent, BTreeNode<n> delNode, int size) {
            if (parent.getKeyLength() <= 0) {
                return null;
            }
            Integer index = null;
            int sonNodeSize = parent.getSonNodeSize();
            for (int i = 0; i < sonNodeSize; i++) {
                //找到当前节点的下标
                if (parent.sonbTreeNodes[i] == delNode) {
                    index = i;
                }
            }
            //将下标后的所有值往后移动,移动size位
            if (index == sonNodeSize - 1) {
                //如果是最后一位那么直接置空就行,然后返回起始位置
                parent.sonbTreeNodes[parent.sonNodeSize - 1] = null;
            } else {
                parent.sonbTreeNodes[index] = null;
                moveArrayNode(parent.sonbTreeNodes, sonNodeSize, index, size);
            }
            //返回起始位置
            parent.sonNodeSize--;
            return index;
        }


        @Override
        public String toString() {
            return "BTreeNode{" +
                    "keySiez=" + keySiez +
                    ", keys=" + Arrays.toString(keys) +
                    ", nodeSize=" + sonNodeSize +
                    ", sonbTreeNodes=" + Arrays.toString(sonbTreeNodes) +
                    '}';
        }
    }


    //插入逻辑
    public void add(T data) throws Exception {


        //如果root为空那么创建新的节点
        if (root == null) {
            BTreeNode<T> newNode = new BTreeNode<T>();
            insert(newNode, data);
            root = newNode;
            return;
        }
        //root节点key,没有插入满,并且还没有子节点,那么继续插入
        if (!root.getKeyFull() && root.getSonNodeSize() == 0) {
            insert(root, data);
            return;
        }
        //此刻root的key已经满了那么进行分裂节点
        if (root.getSonNodeSize() == 0) {
            nodeSplit(root);
        }


        //如果root节点的子节点不为空那么,一直深入到叶子节点
        //找到对应的叶子节点
        BTreeNode<T> tbTreeNode = selectLeaf(data);
        long newKey = getClassId(data); //拿到插值的索引
        int keyLength = tbTreeNode.getKeyLength();
        for (int i = 0; i < keyLength; i++) {
            long oldKey = getClassId(tbTreeNode.keys[i]);//节点对应的索引
            //如果发现当前的值存在了,那么直接提示报错,不然这样会影响树的结构,(如果需要插入重复存在的数据,可以使用链表来关联起来)
            if (oldKey == newKey) {
                throw new Exception("repetition data  , keyID: " + newKey);
            }
        }

        //将值插入找到的叶子节点
        insert(tbTreeNode, data);

    }


    //删除逻辑
    public boolean del(T data) {
        Map<String, Object> node1 = getNode(data);
        if (node1 == null) {
            return false;
        }
        BTreeNode<T> node = (BTreeNode<T>) node1.get("node"); //拿到叶子节点
        int delIndex = (int) node1.get("index");
        // 到这一步 已经找到需要操作的节点,和要删除这个节点那个值的下标了
        //如果删除的值是在叶子节点中
        delLeaf(node, delIndex);
        //删除的值不是叶子节点,那么需要进行复杂的操作,进行前驱或者后驱到叶子节点取值覆盖删除的key
        delPrecursorOrSucceed(node, delIndex);
        return true;
    }

    private void delPrecursorOrSucceed(BTreeNode<T> node, int delIndex) {
        if (node.getSonNodeSize() > 0) {
            //前驱,delIndex-1 节点下的最大子节点一直到叶子节点,然后取叶子节点的最大值
            BTreeNode<T> sonbTreeNode = node.sonbTreeNodes[delIndex];
            while (sonbTreeNode.getSonNodeSize() > 0) {
                sonbTreeNode = sonbTreeNode.sonbTreeNodes[sonbTreeNode.getSonNodeSize() - 1];
            }
            //拿到前驱最大叶子节点的最大key
            node.keys[delIndex] = sonbTreeNode.keys[sonbTreeNode.getKeyLength() - 1];
            delArrayKey(sonbTreeNode, sonbTreeNode.getKeyLength() - 1);
        }
    }

    private void delLeaf(BTreeNode<T> node, int delIndex) {
        //如果是叶子节点
        if (node.getSonNodeSize() == 0) {
            delArrayKey(node, delIndex);
        }
    }

    //数组删除-后覆盖删除位置
    private void delArrayKey(BTreeNode<T> node, int delIndex) {
        int keyLength1 = node.getKeyLength();
        if (keyLength1 < 1) {
            node.keys[delIndex] = null;
        } else {
            //将后面的值覆盖删除的值
            for (int i = delIndex; i < keyLength1; i++) {
                node.keys[i] = node.keys[i + 1];
            }
        }
        node.keySiez--;
    }


    /**
     * @param array       位移的数组
     * @param arrayLength 数组长度
     * @param moveindex   位移的位置
     * @param size        向后位移几位
     */
    private void moveArrayNode(BTreeNode[] array, int arrayLength, int moveindex, int size) {
        for (int i = arrayLength - 1; i >= moveindex; i--) {
            //把当前值放入,数组后i+size位置
            array[i + size - 1] = array[i];
        }
    }

    /**
     * @param array       位移的数组
     * @param arrayLength 数组长度
     * @param moveindex   位移的位置
     * @param size        向后位移几位
     */
    private void moveArrayKey(Object[] array, int arrayLength, int moveindex, int size) {
        //从下标以后的值往后移动(包括下标)
        for (int i = arrayLength - 1; i >= moveindex; i--) {
            array[i + size] = array[i];
        }
    }


    //查询指定值
    public T getData(T data) {
        Map<String, Object> node = getNode(data);
        if (node==null) {
            return null;
        }
        BTreeNode<T> node1 = (BTreeNode<T>) node.get("node");
        Object o = node1.keys[(int) node.get("index")];
        return ( T)o;
    }

    //修改指定值
    public boolean getUpdate(T oldData,T newData) {
        Map<String, Object> node = getNode(oldData);
        if (node==null) {
            return false;
        }
        BTreeNode<T> node1 = (BTreeNode<T>) node.get("node");
        node1.keys[(int) node.get("index")]=newData;
        return true;
    }

    private Map<String, Object> getNode(T data) {
        BTreeNode<T> node = root; //拿到叶子节点
        long newKey = getClassId(data); //拿到索引
        while (node.getSonNodeSize() > 0) {
            int keyLength = node.getKeyLength();
            //找比插入值大的下标,那么就是所对应的子节点下标,如果没找到那么就是最后一个子节点,然后继续深入
            Integer max = null;
            int state = 0;
            for (int i = 0; i < keyLength; i++) {
                long oldKey = getClassId(node.keys[i]);//节点对应的索引
                //找到值了,返回节点
                if (oldKey == newKey) {
                    state = 1;
                    break;
                }

                if (oldKey > newKey) {
                    max = i;
                    break;
                }
            }
            //找到了
            if (state == 1) {
                break;
            }

            //没找到那么取最后一个子节点
            if (max == null) {
                max = node.getSonNodeSize() - 1;
            }
            node = node.sonbTreeNodes[max];
        }


        //遍历找到的节点,或者叶子节点,找到对应的值的下标
        Integer index = null;
        int keyLength = node.getKeyLength();
        for (int i = 0; i < keyLength; i++) {
            long oldKey = getClassId(node.keys[i]);//节点对应的索引
            //找到值了,返回节点
            if (oldKey == newKey) {
                index = i;
                break;
            }
        }

        if (index == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("node", node);
        map.put("index", index);
        return map;
    }


    //查询叶子节点逻辑
    private BTreeNode<T> selectLeaf(T data) {
        BTreeNode<T> node = root; //拿到叶子节点
        long newKey = getClassId(data); //拿到索引
        while (node.getSonNodeSize() > 0) {

            int keyLength = node.getKeyLength();
            //找比插入值大的下标,那么就是所对应的子节点下标,如果没找到那么就是最后一个子节点,然后继续深入
            Integer max = null;
            for (int i = 0; i < keyLength; i++) {
                long oldKey = getClassId(node.keys[i]);//节点对应的索引
                if (oldKey > newKey) {
                    max = i;
                    break;
                }
            }
            //没找到那么取最后一个子节点
            if (max == null) {
                max = node.getSonNodeSize() - 1;

            }

            node = node.sonbTreeNodes[max];
        }


        return node;
    }


    private void insert(BTreeNode<T> node, T data) {
        //先插入值,然后在进行判断key是否满了,如果满了那么进行分割
        node.setKey(data);
        //插入不进去了满了
        if (node.getKeyFull()) {
            //进行分割节点
            nodeSplit(node);
        }
    }


    //节点分割
    private void nodeSplit(BTreeNode<T> bTreeNode) {
        //判断是否是root节点
        if (bTreeNode == root) {
            //如果是root节点那么就创建新节点代替root节点然后将原来的root节点进行分割,之后进行关联父节点
            BTreeNode<T> newRootNode = new BTreeNode<T>();
            Map<String, Object> map = bTreeNode.bTreeNodeSplit(newRootNode);
            //添加节点值
            newRootNode.setKey((T) map.get("center"));
            //添加分裂后的子节点
            newRootNode.setSonbTreeNodes((BTreeNode<T>[]) map.get("sonbTreeNodes"), (Integer) map.get("fromIdnex"));
            //将新节点代替原来的root节点
            root = newRootNode;
        } else {

            BTreeNode<T> oldNode = bTreeNode; //获取当前节点
            //一直向上查找,直到key不满为止
            while (oldNode.getKeyFull()) {
                if (oldNode == root) {
                    //如果是root节点的话,就使用root节点的分割方式
                    nodeSplit(oldNode);
                    break;

                } else {
                    //子节点分割
                    Map<String, Object> map = oldNode.bTreeNodeSplit(oldNode.parent);
                    //给父节点,添加节点值
                    oldNode.parent.setKey((T) map.get("center"));
                    //添加分裂后的子节点,到父节点
                    oldNode.parent.setSonbTreeNodes((BTreeNode<T>[]) map.get("sonbTreeNodes"), (Integer) map.get("fromIdnex"));
                    //然后继续向上查找
                    oldNode = oldNode.parent;
                }

            }

        }

    }


    //通过反射拿到对象内的id值  ,(不允许字符串类型 ,只能是数值类型,或者对象类型里有id字段)
//            long OldKeyId = getClassId(o);
//             long newKeyId = getClassId(key);
    private long getClassId(Object o) {
        if (o instanceof Number) {
            Number data = (Number) (o);
            return data.longValue();
        }
        long l = 0L;
        try {
            Class<?> aClass = o.getClass();
            Field field = aClass.getDeclaredField("id");
            field.setAccessible(true);
            l = (long) field.get(o);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return l;
    }


    public void show() {

        //给节点打标
        BTreeNode<T> node = root;
        while (node.getSonNodeSize() > 0) {
            node.sonbTreeNodes[0].sign = "Front";
            node = node.sonbTreeNodes[0];
        }
        node = root;
        while (node.getSonNodeSize() > 0) {
            node.sonbTreeNodes[node.getSonNodeSize() - 1].sign = "Ent";
            node = node.sonbTreeNodes[node.getSonNodeSize() - 1];
        }


        //变量节点,以树形式展示
        Queue<BTreeNode<T>> queue = new LinkedList<BTreeNode<T>>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            BTreeNode<T> tree = queue.poll();

            if (tree == root) {
                System.out.print("front------------------------------------");
            }

            //一层一层打印节点
            if ("Front".equals(tree.sign)) {
                System.out.print("front------------------------------------");
            }
            System.out.print("|\t");
            for (int i = 0; i < tree.getKeyLength(); i++) {
                long classId = getClassId(tree.keys[i]);
                System.out.print(classId + ",");
            }
            System.out.print("\t|-");
            if ("Ent".equals(tree.sign)) {
                System.out.println("------------------------------------Ent");
            }
            if (tree == root) {
                System.out.println("------------------------------------Ent");
            }

            for (int i = 0; i < tree.getSonNodeSize(); i++) {
                queue.offer(tree.sonbTreeNodes[i]);
            }

        }


    }
}
