package com.huanmin.test.server.common.obj.serializable;


import com.huanmin.test.entity.UserData;
import com.utils.common.base.ResourceFileUtil;
import com.utils.common.obj.serializable.CompressUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class CompressUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(CompressUtilTest.class);
    public static void main(String[] args) {
        UserData build = UserData.builder().id(1).age(2).name("啊是大打撒打撒撒旦大苏打撒旦撒大苏打的啊实打实大苏打大苏打").build();
//        byte[] serialize = SerializeUtil.serialize(build);
//        System.err.println("对象序列化后占            ："+serialize.length+"字节");

        String absoluteFilePathAndCreate = ResourceFileUtil.getAbsoluteFileOrDirPathAndCreate("/CompressUtilTest");
        File file = new File(absoluteFilePathAndCreate);


        CompressUtil.writeCompressObjectToFile(build,file);

        UserData t1 = CompressUtil.readFileCompressObject(file, UserData.class);

        System.out.println(t1);



    }


}
