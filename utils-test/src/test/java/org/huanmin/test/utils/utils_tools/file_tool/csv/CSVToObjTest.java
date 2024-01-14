package org.huanmin.test.utils.utils_tools.file_tool.csv;


import org.huanmin.test.entity.UserEntity;
import org.huanmin.utils.common.file.ResourceFileUtil;
import org.huanmin.utils.common.base.UserData;

import org.huanmin.utils.file_tool.csv.CSVToObj;
import org.huanmin.utils.file_tool.csv.ReadAndWriteFileCSV;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * csv转对象测试
 *
 * @Author: huanmin
 * @Date: 2022/6/18 16:44
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class CSVToObjTest {

    @Test
    public void show1(){
        File absoluteFileOrDirPathAndCreateNewFile = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("/file/text.csv");
        //读取头部对应字段
        String head = ReadAndWriteFileCSV.readFirstLineOne(absoluteFileOrDirPathAndCreateNewFile);
        //去掉头部和尾部的多余信息,只显示内容
        List<String> list = ReadAndWriteFileCSV.readSkipHeadAndEnd(absoluteFileOrDirPathAndCreateNewFile);
        //做映射,然后转换csv格式为实体对象
        List<UserEntity> result = new CSVToObj<UserEntity>(list, "|"){}.mapping(head).transform().result();
        for (UserEntity userData : result) {
            System.out.println(userData);
        }
    }
    @Test
    public void show2(){
        File absoluteFileOrDirPathAndCreateNewFile = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("/file/text.csv");
        //手动指定文件头部
        String head ="id|name|pass|age|sex|site";
        //去掉头部和尾部的多余信息,只显示内容
        List<String> list = ReadAndWriteFileCSV.readSkipHeadAndEnd(absoluteFileOrDirPathAndCreateNewFile);
        List<UserData> result = new CSVToObj<UserData>(list, "|"){}.mapping(head).transform().result();
        for (UserData userData : result) {
            System.out.println(userData);
        }
    }

    //
}
