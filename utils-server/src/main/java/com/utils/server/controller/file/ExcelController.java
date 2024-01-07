package com.utils.server.controller.file;



import com.utils.common.base.UserData;
import org.huanmin.file_tool.excel.ExcelDownLoadUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HuAnmin
 * @version 1.0
 * @email 3426154361@qq.com
 * @date 2021/3/25-14:45
 * @description 类描述....
 */

@RestController
@RequestMapping("/excel")
public class ExcelController {

    //http://localhost:7001/excel/test
    @GetMapping("/test")
    public void show(HttpServletResponse response) throws IOException {


        //------插入数据
        UserData UserData1= UserData.builder().id(1).age(22).sex("男").name("hu1").build();
        UserData UserData2= UserData.builder().id(2).age(23).sex("女").name("hu2").build();
        UserData UserData3= UserData.builder().id(3).age(24).sex("男").name("hu3").build();
        List<Object> list=new ArrayList<Object>(){
            {
                add(UserData1);
                add(UserData2);
                add(UserData3);
            }
        };

        // 将list插入 自动创建list类型里的UserData 的excel类型是xlsx文件
        //同时将list里的数据全部自动录入到UserData表里
        ExcelDownLoadUtils.downloadExcel(list,"xlsx",response);

    }

    @GetMapping("/test1")
    public void show1(HttpServletResponse response) throws IOException {

        try {
            //------插入数据
            List<Object> list=new ArrayList<Object>(){
                {
                    add(UserData.builder().id(1).name("hu1").age(22).sex("男").build());
                    add(UserData.builder().id(2).name("hu2").age(23).sex("男").build());
                    add(UserData.builder().id(3).name("hu3").age(24).sex("女").build());
                    add(UserData.builder().id(4).name("hu3").age(24).sex("女").build());
                    add(UserData.builder().id(5).name("hu3").age(24).sex("男").build());

                }
            };

            // 依据上面的数据  我们将名字重复的 合并单元格   也就是 hu3       3

            // 将list插入 自动创建list类型里的UserData 的excel类型是xlsx文件
            //同时将list里的数据全部自动录入到UserData表里

            //其他参数说明：1：开始行 2：结束行  3：开始列 4：结束列     从无论是行还是列都是从0开始

            //下面这行代码的意思是   将第4行到第6行和第2列到第2列的单元格合并
            ExcelDownLoadUtils.downloadExcel(list,"xlsx",3,5,1,1,response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
