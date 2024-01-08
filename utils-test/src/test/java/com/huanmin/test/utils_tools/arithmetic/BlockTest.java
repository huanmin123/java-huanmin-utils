package com.huanmin.test.utils_tools.arithmetic;


import com.utils.common.base.CodeTimeUtil;
import com.utils.common.base.UserData;
import com.utils.arithmetic.blocksearch.BlockList;
import com.utils.arithmetic.blocksearch.BlockObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class BlockTest {
    private static final Logger logger = LoggerFactory.getLogger(BlockTest.class);
    //组装数据1
    @Test
    public void aVoid() throws Exception {
        // 1. 待查找数组
        List<UserData> list22=new ArrayList<UserData>(1000001);
        for (int i = 0; i < 1000001; i++) {
            Random random=new Random();
            int i1 = random.nextInt(1000001);
            UserData build = UserData.builder().id(i1).age(i1).name("").build();
            list22.add(build);
        }

        //执行时长：200 毫秒左右.
        AtomicReference<BlockList<UserData>> userDataBlockList1= new AtomicReference<>();
                CodeTimeUtil.creator(()->{
              userDataBlockList1.set(BlockList.builder(UserData.class).create().ListToBlockListCreate(list22));
        });
        //执行时长：0 毫秒.
        CodeTimeUtil.creator(()->{
            BlockObject<UserData> userDataBlockObject = userDataBlockList1.get().method().get(1000000);
            System.out.println( "aVoid"+userDataBlockObject);
        });

    }

    //组装数据2
    @Test
    public void shiw2() throws Exception {
        List<BlockObject<UserData>> array=new ArrayList<>();
        for (int i = 0; i < 5000000; i++) {
            UserData build = UserData.builder().id(i).age(i).name("").build();
            BlockObject<UserData> userDataBlockObject=new BlockObject<>(build.getId(),build);
            array.add(userDataBlockObject);
        }
        CodeTimeUtil.creator(()->{
        BlockList.builder(UserData.class).create().blockListCreate(array);
        });
    }



}
