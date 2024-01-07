package com.huanmin.test.utils_tools.file_tool.file;

import com.alibaba.fastjson.JSONObject;

import com.utils.common.base.FakerData;
import com.utils.common.file.ResourceFileUtil;
import com.utils.common.base.UserData;
import com.utils.common.file.csv.ObjToCSV;
import com.utils.common.file.csv.ReadAndWriteFileCSV;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * CSV读写测试
 *
 * @Author: huanmin
 * @Date: 2022/6/18 15:05
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class ReadAndWriteFileCSVTest {

    @Test
    public void show1() {
        File absoluteFileOrDirPathAndCreateNewFile = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("/text.csv");
        List<UserData> userDatas = FakerData.getUserDataList(10);
        String str = ObjToCSV.create(userDatas, "|")
                .fieldsAll()
                .addExcludeFields("roleData")
                .addHead()
                .addContents()
                .addEnding((data) -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("count", data.getNumber());
                    jsonObject.put("time", data.getCreateDate());
                    jsonObject.put("separator", data.getSeparator());
                    return jsonObject.toJSONString();
                })
                .ToString();

        ReadAndWriteFileCSV.write(str, absoluteFileOrDirPathAndCreateNewFile);
    }

    @Test
    public void show2() {
        File absoluteFileOrDirPathAndCreateNewFile = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("/text.csv");
        String read = ReadAndWriteFileCSV.read(absoluteFileOrDirPathAndCreateNewFile);
        System.out.println(read);
    }

    @Test
    public void show3() {
        File absoluteFileOrDirPathAndCreateNewFile = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("/text.csv");
        List<String> list = ReadAndWriteFileCSV.readSkipHead(absoluteFileOrDirPathAndCreateNewFile);
        System.out.println(list);
    }

    //跳过头部和尾部
    @Test
    public void show3_1() {
        File absoluteFileOrDirPathAndCreateNewFile = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("/text.csv");
        List<String> list = ReadAndWriteFileCSV.readSkipHeadAndEnd(absoluteFileOrDirPathAndCreateNewFile);
        for (String s : list) {
            System.out.println(s);
        }

    }

    @Test
    public void show4() {
        File absoluteFileOrDirPathAndCreateNewFile = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("/text.csv");
        String read = ReadAndWriteFileCSV.readEndLine(absoluteFileOrDirPathAndCreateNewFile);
        System.out.println(read);
    }

}
