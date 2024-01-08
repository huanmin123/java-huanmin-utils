package com.huanmin.test.utils_tools.arithmetic;

import com.utils.common.base.CodeTimeUtil;
import com.utils.common.base.UserData;
import com.utils.arithmetic.tree.binarytree.BinaryTree;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author huanmin
 * @date 2024/1/8
 */
public class BinaryTreeTest {
    @Test
    public void insertTree() throws Exception {

        List<UserData> list22 = new ArrayList<UserData>(1000001);
        for (int i = 0; i < 1000001; i++) {
            Random random = new Random();
            int i1 = random.nextInt(1000001);
            UserData build = UserData.builder().id(i1).age(i1).name("").build();
            list22.add(build);
        }
        //执行时长：7506 毫秒.
        AtomicReference<BinaryTree<UserData>> userDataBlockList1 = new AtomicReference<>();
        CodeTimeUtil.creator(() -> {
            userDataBlockList1.set(new BinaryTree<UserData>(list22));
        });
        //执行时长：0 毫秒.
        CodeTimeUtil.creator(() -> {
            UserData build = UserData.builder().id(1000000).age(1000000).name("").build();
            System.out.println(userDataBlockList1.get().find(build));
        });
        //执行时长：0 毫秒.
        CodeTimeUtil.creator(() -> {
            UserData build = UserData.builder().id(1000000).age(1000000).name("").build();
            userDataBlockList1.get().findAll(3); //有序集合
        });


    }


    @Test
    public void updateTree() {
        BinaryTree<UserData> bt = new BinaryTree<UserData>();
        UserData build2 = UserData.builder().id(4).age(4).name("").build();
        bt.insert(build2);
        UserData build5 = UserData.builder().id(5).age(5).name("").build();
        bt.insert(build5);

        UserData buildu = UserData.builder().id(4).age(6).name("6").build();
        bt.update(buildu);
        System.out.println(bt.find(buildu));
    }

    @Test
    public void breadthFindAll() {
        List<UserData> list22 = new ArrayList<UserData>(500);
        for (int i = 0; i <500; i++) {
            Random random = new Random();
            int i1 = random.nextInt(500);
            UserData build = UserData.builder().id(i1).age(i1).name("").build();
//            UserData build = UserData.builder().id(i).age(i).name("").build();
            list22.add(build);

            ; //有序集合
        }
        System.out.println(list22.size());
        BinaryTree<UserData> userDataBinaryTree = new BinaryTree<>(list22);
        System.out.println(userDataBinaryTree.breadthFindAll().size());
    }


    @Test
    public void deleteTree1() {
        //1.删除的元素没有子节点(左和右),那么直接删除当前节点
        BinaryTree<UserData> bt = new BinaryTree<UserData>();
        UserData build2 = UserData.builder().id(4).age(4).name("").build();
        bt.insert(build2);
        //[UserData(id=4, name=, age=4)]
        System.out.println(bt.findAll(2));
        UserData build11 = UserData.builder().id(4).age(21).name("").build();
        bt.delete(build11);
        //[]
        System.out.println(bt.findAll(2));
    }


    @Test
    public void deleteTree2() {
        //2.删除的元素有左子节点,就将这个左子节点代替当前节点
        BinaryTree<UserData> bt = new BinaryTree<UserData>();
        UserData build2 = UserData.builder().id(4).age(4).name("").build();
        bt.insert(build2);
        UserData build4 = UserData.builder().id(3).age(3).name("").build();
        bt.insert(build4);

        //[UserData(id=3, name=, age=3), UserData(id=4, name=, age=4)]
        System.out.println(bt.findAll(2));
        UserData build11 = UserData.builder().id(4).age(21).name("").build();
        bt.delete(build11);
        //[UserData(id=3, name=, age=3)]
        System.out.println(bt.findAll(2));
    }


    @Test
    public void deleteTree3() {
        //2.删除的元素有左子节点,就将这个左子节点代替当前节点
        BinaryTree<UserData> bt = new BinaryTree<UserData>();
        UserData build2 = UserData.builder().id(4).age(4).name("").build();
        bt.insert(build2);
        UserData build4 = UserData.builder().id(5).age(5).name("").build();
        bt.insert(build4);

        //[UserData(id=4, name=, age=4), UserData(id=5, name=, age=5)]
        System.out.println(bt.findAll(2));
        UserData build11 = UserData.builder().id(4).age(21).name("").build();
        bt.delete(build11);
        //[UserData(id=5, name=, age=5)]
        System.out.println(bt.findAll(2));
    }

    @Test
    public void deleteTree4() {
        //3.删除的元素有(左右)子节点 ,那么需要找到被删除节点的右子节点中的最小左子节点,然后替换删除的节点(注意:需要考虑最小节点有右节点的情况)

        //3.1删除的元素右节点没有左节点的情况,将删除元素的右节点覆盖删除的节点,然后在将删除元素的左节点放入到删除元素的右节的左节点中
        BinaryTree<UserData> bt = new BinaryTree<UserData>();
        UserData build2 = UserData.builder().id(4).age(4).name("").build();
        bt.insert(build2);
        UserData build4 = UserData.builder().id(5).age(5).name("").build();
        bt.insert(build4);
        UserData build41 = UserData.builder().id(6).age(6).name("").build();
        bt.insert(build41);
        UserData build3 = UserData.builder().id(3).age(3).name("").build();
        bt.insert(build3);

        //[UserData(id=4, name=, age=4), UserData(id=5, name=, age=5)]
        System.out.println(bt.findAll(2));
        UserData build11 = UserData.builder().id(4).age(21).name("").build();
        bt.delete(build11);
        //[UserData(id=5, name=, age=5)]
        System.out.println(bt.findAll(2));
    }

    @Test
    public void deleteTree5() {
        //3.删除的元素有(左右)子节点 ,那么需要找到被删除节点的右子节点中的最小左子节点,然后替换删除的节点(注意:需要考虑最小节点有右节点的情况)

        //3.3删除元素的右节点的最小左节点是否还存在右节点的情况 ,如果存在那么,就将右节点放入最小左节,然后将最小左节放入被删除的节点
        BinaryTree<UserData> bt = new BinaryTree<UserData>();
        UserData build2 = UserData.builder().id(4).age(4).name("").build();
        bt.insert(build2);
        UserData build3 = UserData.builder().id(3).age(3).name("").build();
        bt.insert(build3);
        UserData build8 = UserData.builder().id(8).age(8).name("").build();
        bt.insert(build8);
        UserData build41 = UserData.builder().id(5).age(5).name("").build();
        bt.insert(build41);
        UserData build6 = UserData.builder().id(6).age(6).name("").build();
        bt.insert(build6);

        UserData build11 = UserData.builder().id(10).age(10).name("").build();
        bt.insert(build11);
        UserData build22 = UserData.builder().id(13).age(13).name("").build();
        bt.insert(build22);


        //[UserData(id=4, name=, age=4), UserData(id=5, name=, age=5)]
        System.out.println(bt.findAll(2));
        UserData build1111 = UserData.builder().id(4).age(21).name("").build();
        bt.delete(build1111);
        //[UserData(id=5, name=, age=5)]
        System.out.println(bt.findAll(2));
    }

    @Test
    public void deleteTree6() {
        //3.删除的元素有(左右)子节点 ,那么需要找到被删除节点的右子节点中的最小左子节点,然后替换删除的节点(注意:需要考虑最小节点有右节点的情况)

        //3.4节点不存在右节点情况,将找到的最小左节点覆盖删除的节点,并且删除原来节点的位置
        BinaryTree<UserData> bt = new BinaryTree<UserData>();
        UserData build2 = UserData.builder().id(4).age(4).name("").build();
        bt.insert(build2);
        UserData build4 = UserData.builder().id(8).age(8).name("").build();
        bt.insert(build4);
        UserData build6 = UserData.builder().id(6).age(6).name("").build();
        bt.insert(build6);
        UserData build11 = UserData.builder().id(10).age(10).name("").build();
        bt.insert(build11);
        UserData build22 = UserData.builder().id(13).age(13).name("").build();
        bt.insert(build22);

        UserData build3 = UserData.builder().id(3).age(3).name("").build();
        bt.insert(build3);

        //[UserData(id=4, name=, age=4), UserData(id=5, name=, age=5)]
        System.out.println(bt.findAll(2));
        UserData build1111 = UserData.builder().id(4).age(21).name("").build();
        bt.delete(build1111);
        //[UserData(id=5, name=, age=5)]
        System.out.println(bt.findAll(2));
    }
}
