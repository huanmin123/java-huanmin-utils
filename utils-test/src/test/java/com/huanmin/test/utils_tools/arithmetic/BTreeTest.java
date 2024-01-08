package com.huanmin.test.utils_tools.arithmetic;


import com.utils.common.base.FakerData;
import com.utils.common.base.UserData;
import com.utils.arithmetic.tree.btree.BTree;
import org.junit.Test;

import java.util.List;

/**

 */
public class BTreeTest {


    @Test
    public void test1() throws Exception {
       BTree<Integer> bTree = new BTree<>(3);
        bTree.add(22);
        bTree.add(10);
        bTree.add(13);
        bTree.add(30);
        bTree.add(35);
        bTree.add(39);
        bTree.add(40);
        bTree.add(45);
        bTree.add(46);
        bTree.add(47);
        bTree.add(48);
        bTree.add(21);
        bTree.add(17);
        bTree.add(15);
        bTree.add(14);
        bTree.add(11);
        bTree.add(8);
        bTree.add(7);
        bTree.add(6);
        bTree.add(60);
        bTree.add(61);
        bTree.add(65);
//

        bTree.show();

        /*
         * front------------------------------------|	30,	|-------------------------------------Ent
         * front------------------------------------|	10,15,	|-|	45,	|-------------------------------------Ent
         * front------------------------------------|	7,	|-|	13,	|-|	21,	|-|	39,	|-|	47,60,	|-------------------------------------Ent
         * front------------------------------------|	6,	|-|	8,	|-|	11,	|-|	14,	|-|	17,	|-|	22,	|-|	35,	|-|	40,	|-|	46,	|-|	48,	|-|	61,65,	|-------------------------------------Ent
         */


    }

    @Test
    public void test2() throws Exception {
        BTree<Integer> bTree = new BTree<>(5);
        bTree.add(22);
        bTree.add(10);
        bTree.add(13);
        bTree.add(30);
        bTree.add(35);
        bTree.add(39);
        bTree.add(40);
        bTree.add(45);
        bTree.add(46);
        bTree.add(47);
        bTree.add(48);
        bTree.add(21);
        bTree.add(17);
        bTree.add(15);
        bTree.add(14);
        bTree.add(11);
        bTree.add(8);
        bTree.add(7);
        bTree.add(6);
        bTree.add(60);
        bTree.add(61);
        bTree.add(65);

        bTree.show();

    /*
    front------------------------------------|	22,	|-------------------------------------Ent
    front------------------------------------|	11,15,	|-|	39,46,60,	|-------------------------------------Ent
    front------------------------------------|	6,7,8,10,	|-|	13,14,	|-|	17,21,	|-|	30,35,	|-|	40,45,	|-|	47,48,	|-|	61,65,	|-------------------------------------Ent
     */


    }



    @Test
    public void test4() throws Exception {
        BTree<Integer> bTree = new BTree<>(3);
        bTree.add(22);
        bTree.add(10);
        bTree.add(13);
        bTree.add(30);
        bTree.add(35);
        bTree.add(39);
        bTree.add(40);
        bTree.add(45);
        bTree.add(46);
        bTree.add(47);
        bTree.add(48);
        bTree.add(21);
        bTree.add(17);
        bTree.add(15);
        bTree.add(14);
        bTree.add(11);
        bTree.add(8);
        bTree.add(7);
        bTree.add(6);
        bTree.add(5);
        bTree.add(60);
        bTree.add(61);
        bTree.del(5);

        bTree.show();

   /*
front------------------------------------|	30,	|-------------------------------------Ent
front------------------------------------|	10,15,	|-|	45,	|-------------------------------------Ent
front------------------------------------|	7,	|-|	13,	|-|	21,	|-|	39,	|-|	47,60,	|-------------------------------------Ent
front------------------------------------|	6,	|-|	8,	|-|	11,	|-|	14,	|-|	17,	|-|	22,	|-|	35,	|-|	40,	|-|	46,	|-|	48,	|-|	61,	|-------------------------------------Ent

    */


    }


    @Test
    public void test5() throws Exception {



        List<UserData> userDatas = FakerData.getUserDataList(10);
        System.out.println(userDatas);



        BTree<UserData> bTree = new BTree<>(3);

        for (UserData userData : userDatas) {
            bTree.add(userData);
        }

        bTree.show();

        UserData data = bTree.getData(userDatas.get(2));

        System.out.println(data);




   /*
front------------------------------------|	30,	|-------------------------------------Ent
front------------------------------------|	10,15,	|-|	45,	|-------------------------------------Ent
front------------------------------------|	7,	|-|	13,	|-|	21,	|-|	39,	|-|	47,60,	|-------------------------------------Ent
front------------------------------------|	5,6,	|-|	8,	|-|	11,	|-|	14,	|-|	17,	|-|	22,	|-|	35,	|-|	40,	|-|	46,	|-|	48,	|-|	61,	|-------------------------------------Ent

    */


    }
}
