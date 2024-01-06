package com.huanmin.test.utils_common.file;


import com.utils.common.base.CodeTimeUtil;
import com.utils.common.base.FakerData;
import com.utils.common.base.ResourceFileUtil;
import com.utils.common.base.UserData;
import com.utils.common.container.ArrayByteUtil;
import com.utils.common.file.FileTool;
import com.utils.common.file.ReadFileBytesUtil;
import com.utils.common.file.ReadWriteFileUtils;
import com.utils.common.file.WriteFileStrUtil;
import lombok.SneakyThrows;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ReadWriteFileUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(ReadWriteFileUtilsTest.class);

    @Test
    public void readWriteStrLine() throws FileNotFoundException {
        File file = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("file/DES3Input.txt");
        String write = file.getParent() + File.separator + "new-DES3Input.txt";
        String read = file.getAbsolutePath();
        ReadWriteFileUtils.readWriteStrLine(new File(read), new File(write), false, (line) -> {
            System.out.println(line);
            return line;
        });

    }

    @Test
    public void readWriteByte() throws FileNotFoundException {
        File file = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("file/DES3Input.txt");
        String write = file.getParent() + File.separator + "newbyte-DES3Input.txt";
        String read = file.getAbsolutePath();
        FileInputStream fis1 = new FileInputStream(read);
        ReadWriteFileUtils.readWriteByte(fis1, new File(write), false);

    }

    @Test
    public void reaByte() throws Exception {
//        File file = ResourceFileUtil.getFile("file/DES3Input.txt");
//        String read = file.getAbsolutePath();
        String read = "C:\\Users\\Administrator\\Desktop\\fsdownload\\TEST.dmp";
        FileInputStream fis1 = new FileInputStream(read);
        byte[] bytes = ReadFileBytesUtil.reaStartByte(fis1, 100);
        String s = ArrayByteUtil.byte2HexHead(bytes, 4,",");

        System.out.println(s);

    }


    //多线程并发写
    @SneakyThrows
    @Test
    public void writeMore() {
        File absoluteFileOrDirPathAndCreateNewFile = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("/test.txt");
        List<UserData> userDatas = FakerData.getUserDataList(200000);
//        Collection<Future<?>> collection = new ArrayList<>();
//        CodeStartAndStopTimeUtil.creator(()->{
//            List<UserData> userDatas1 = FakerData.getUserDatas(userDatas.size() / 16);
//            for (int i = 0; i < 16; i++) {
//                Future<?> test = ExecutorUtils.createFuture("test", () -> {
//                    ReadWriteFileUtils.writeStrAppend(absoluteFileOrDirPathAndCreateNewFile, userDatas1.toString());
//                });
//                collection.add(test);
//            }
//        });
//
//        ExecutorUtils.waitComplete(collection);  //1448


        CodeTimeUtil.creator(()->{
            WriteFileStrUtil.writeStrAppend(absoluteFileOrDirPathAndCreateNewFile, userDatas.toString());

     });

    }

    @Test
    public  void readByteNumSplit() throws IOException {
        File file = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("file/text.text");
        FileTool.readByteNumSplit(file, ",", (number)->{
            System.out.println(number);
        });
       
    }
    

    @Test
    public  void readByteNumWidth() throws IOException {
        // 获取不同系统的换行符
        String lineSeparator = System.lineSeparator();
        System.out.println(lineSeparator);

        File file = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("file/text1.text");
        FileTool.readByteNumWidth(file, 10, (number)->{
            System.out.println(number);
        });
    }
    
 
    
}
