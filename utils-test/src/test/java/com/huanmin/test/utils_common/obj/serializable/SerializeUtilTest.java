package com.huanmin.test.utils_common.obj.serializable;



import com.huanmin.test.entity.UserEntity;
import com.utils.common.obj.serializable.SerializeUtil;
import com.utils.null_chain.NULL;
import com.utils.null_chain.NullChain;
import com.utils.null_chain.NullChainDefault;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class SerializeUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(SerializeUtilTest.class);

    @Test
    public void show() {
        UserEntity build = UserEntity.builder().id(1).age(2).name("啊是大打撒打撒撒旦大苏打撒旦撒大苏打的啊实打实大苏打大苏打").build();
        String userDir = System.getProperty("user.dir") + "/SerializeUtilTest";

        File file = new File(userDir);

        SerializeUtil.serializeToFile(build, file);

        UserEntity t1 = SerializeUtil.readUnserialize(file, UserEntity.class);

        System.out.println(t1);
    }

}
