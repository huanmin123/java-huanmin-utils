package com.huanmin.test.utils_common.base;

import com.huanmin.test.core.annotation.SpringTest;
import com.huanmin.test.core.annotation.Test;
import com.utils.common.file.ResourceFileUtil;
import com.utils.common.file.TransitionFileStreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
@SpringTest
@Component
@Slf4j
public class ResourceFileUtilTestReality {
    @Test
    public void getResourceFileContent() throws Exception {
        String relativePath = "database.properties";
//        InputStream resourceAsStream = ResourceFileUtilTestReality.class.getClassLoader().getResourceAsStream(relativePath);
//        String s = TransitionFileStreamUtil.readIoToStr(resourceAsStream);
//        System.out.println(s);
        InputStream fileStream = ResourceFileUtil.getFileStream(relativePath);
        String s1 = TransitionFileStreamUtil.readIoToStr(fileStream);
        System.out.println(s1);
    }

    @Test
    public void getCurrentProjectAbsolutePath() {
        String userDir = System.getProperty("user.dir");
        System.out.println(userDir);
    }
}
