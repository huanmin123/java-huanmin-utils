package com.huanmin.utils.file_tool.csv;




import com.huanmin.utils.common.file.ReadFileBytesUtil;
import com.huanmin.utils.common.file.ReadFileLineUtil;
import com.huanmin.utils.common.file.ReadWriteFileUtils;
import com.huanmin.utils.common.file.WriteFileBytesUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 读写CSV文件(各种方式)
 *
 * @Author huanmin
 * @Date 2022/6/18 {TIME}
 * @Version 1.0
 * @Description 文件作用详细描述....
 */
public class ReadAndWriteFileCSV {

    //将csv内容写入到文件中
    public static void write(String str,File writeFile){
        WriteFileBytesUtil.writeByte(str.getBytes(StandardCharsets.UTF_8),writeFile);
    }
    //将CSV文件一次读取到内存中
    public static String read(File file){
        byte[] bytes = ReadFileBytesUtil.readByte(file);
        return   new String(bytes, StandardCharsets.UTF_8);
    }
    //跳过头部的第一行,进行读取
    public static List<String> readSkipHead(File file){
        List<String> list=new ArrayList<>();
        ReadFileLineUtil.readFileSkipStrLine(file,1, list::add);
        return   list;
    }
    //跳过头部和尾部
    public static List<String> readSkipHeadAndEnd(File file){
        List<String> list = readSkipHead(file);
        list.remove(list.size()-1);
        return list;
    }
    //读取文件最后一行
    public static String readEndLine(File file){
        return   ReadFileLineUtil.readFileEndLine(file);
    }

    //读取文件第一行
    public static String readFirstLineOne(File file){
        return  ReadFileLineUtil.readFirstLineOne(file);
    }

    //每次读取一行,然后处理后写入新文件里
    public static void readAndwriteHandle(File read, File write, Function<String, String> function){
        ReadWriteFileUtils.readWriteStrLine(read,write,function);
    }

}
