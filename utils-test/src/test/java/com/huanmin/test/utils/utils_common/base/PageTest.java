package com.huanmin.test.utils.utils_common.base;


import com.huanmin.utils.common.base.DataPaging;
import com.huanmin.utils.common.base.UserData;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PageTest {
    private static final Logger logger = LoggerFactory.getLogger(PageTest.class);

    //    根据提供的数据总行数进行获取分页信息
    @Test
    public void test1() {
        //总记录数  (需要分页的全部数据的数量)
        int total = 10;
        //当前页
        int cpage = 2;
        //每页显示多少条
        int pagesize = 2;

//         获取分页信息
        DataPaging page = new DataPaging(total, cpage, pagesize);
        System.out.println("开始页:" + page.getLpage() + "__当前页：" + page.getCurrentpage() + "__结束页" + page.getRpage() + "____总页数：" + page.getLast());
    }

//   根据提供的数据进行分页

    @Test
    public void test2() {


        // 查询出来的数据源
        List<UserData> list = new ArrayList<UserData>() {{

            add(UserData.builder().id(1).name("a1").age(22).build());
            add(UserData.builder().id(2).name("a2").age(22).build());
            add(UserData.builder().id(3).name("a3").age(22).build());
            add(UserData.builder().id(4).name("a4").age(22).build());
            add(UserData.builder().id(5).name("a4").age(22).build());
            add(UserData.builder().id(6).name("a4").age(22).build());
            add(UserData.builder().id(7).name("a4").age(22).build());
            add(UserData.builder().id(8).name("a4").age(22).build());
            add(UserData.builder().id(9).name("a4").age(22).build());
            add(UserData.builder().id(10).name("a4").age(22).build());
            add(UserData.builder().id(11).name("a4").age(22).build());
        }};

        //当前页
        int cpage = 2;
        //每页显示多少条
        int pagesize = 3;

//        开始分页  以及获取分页信息
        DataPaging<UserData> page = new DataPaging(cpage, pagesize, list);  //常用 直接返回分页后的数据

//        Page<UserData> page = new Page(list.size(), 2,3);   // 只计算 分页信息
        System.out.println("开始页:" + page.getLpage() + "__当前页：" + page.getCurrentpage() + "__结束页" + page.getRpage() + "____总页数：" + page.getLast());

        System.out.println("分页后的数据:" + page.getList());


    }

}
