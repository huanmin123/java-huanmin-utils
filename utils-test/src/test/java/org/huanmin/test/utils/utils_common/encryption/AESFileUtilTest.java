package org.huanmin.test.utils.utils_common.encryption;


import org.huanmin.utils.common.file.ResourceFileUtil;
import org.huanmin.utils.common.encryption.aes.AESFileUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AESFileUtilTest {
    @Test
    public void encryptiontext() throws FileNotFoundException {
        File file = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("file/aaa.txt");
        String absolutePath = file.getAbsolutePath();
        String key = "1234567891234567";
        AESFileUtil.encryption(absolutePath, key, true);
    }

    @Test
    public void decrypttext() throws IOException {
        String key = "1234567891234567";
        File file = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("file/aaa.txt_encrypt_812-2023fP4wN19mH15bM5jW47vW-353.txt");
        String absolutePath = file.getAbsolutePath();
        AESFileUtil.decrypt(absolutePath, key, true);
    }

    @Test
    public void encryptiondocx() throws FileNotFoundException {
        File file = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("file/用户操作手册.docx");
        String absolutePath = file.getAbsolutePath();
        String key = "1234567891234567";
        AESFileUtil.encryption(absolutePath, key, false);
    }

    @Test
    public void decryptdocx() throws IOException {
        String key = "1234567891234567";
        File file =
                ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("file/用户操作手册.docx_encrypt_284-2023jJ4mV19tV15dE23oM38yV-988.docx");
        String absolutePath = file.getAbsolutePath();
        AESFileUtil.decrypt(absolutePath, key, false);
    }

    @Test
    public void test3() throws FileNotFoundException {
        File file = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("mp4/2222.mp4");
        String absolutePath = file.getAbsolutePath();
        String key = "1234567891234567";
        AESFileUtil.encryption(absolutePath, key, false);
    }

    @Test
    public void test5() throws IOException {
        String key = "1234567891234567";
        File file = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("mp4/2222_encrypt_926-2022eV5qG3jW18mG46sR22sQ-487.mp4");
        String absolutePath = file.getAbsolutePath();
        AESFileUtil.decrypt(absolutePath, key, false);
    }

    @Test
    public void test6() throws FileNotFoundException {
        File file = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("img/a.jpg");
        String absolutePath = file.getAbsolutePath();
        String key = "1234567891234567";
        AESFileUtil.encryption(absolutePath, key, false);
    }

    @Test
    public void test7() throws IOException {
        String key = "1234567891234567";
        File file = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("img/a.jpg_encrypt_455-2023xX4xY19pC15yO55lX1wX-5.jpg");
        String absolutePath = file.getAbsolutePath();
        AESFileUtil.decrypt(absolutePath, key, false);
    }


}
