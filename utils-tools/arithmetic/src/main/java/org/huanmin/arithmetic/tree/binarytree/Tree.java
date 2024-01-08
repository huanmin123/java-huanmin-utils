package org.huanmin.arithmetic.tree.binarytree;

import java.util.List;
/**
 * @author huanmin
 */
public interface Tree<T>  {
    //查找指定节点
    public T   find(T key);
    //查询全部 ,各种遍历方式
    public List<T> findAll(int type);
    //广度查询
    List<T> breadthFindAll();
    //插入新节点
    public boolean insert(T data);
    //查找最大值
    public T  findMax();
    //查找最小值
    public T  findMin();
    //删除节点
    public boolean delete(T key);
    //修改节点
    public boolean update(T key);
    //Other Method......
}