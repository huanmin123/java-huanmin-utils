package com.utils.common.obj.serializable;

import com.entity.UserData;
import com.path.ResourceFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class SerializeUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(SerializeUtilTest.class);
    public static void main(String[] args) {
        UserData build = UserData.builder().id(1).age(2).name("啊是大打撒打撒撒旦大苏打撒旦撒大苏打的啊实打实大苏打大苏打").build();

        String absoluteFilePathAndCreate = ResourceFileUtil.getAbsoluteFileOrDirPathAndCreate("/SerializeUtilTest");
        File file = new File(absoluteFilePathAndCreate);

        SerializeUtil.serializeToFile(build,file);

        UserData t1 = SerializeUtil.readUnserialize(file, UserData.class);

        System.out.println(t1);



    }
}
