package com.huanmin.test.utils_tools.arithmetic;

import org.huanmin.arithmetic.tree.avltree.AVLTree;
import org.junit.Test;

/**
 * @author huanmin
 * @date 2024/1/8
 */
public class AVLTreeTest {
    @Test
    public void show() {

        AVLTree<Integer> avlTree=new AVLTree<>();
        avlTree.insert(1);
        avlTree.insert(2);
        avlTree.insert(3);
        avlTree.insert(4);


    }
}
