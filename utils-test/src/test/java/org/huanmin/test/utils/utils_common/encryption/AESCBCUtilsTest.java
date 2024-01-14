package org.huanmin.test.utils.utils_common.encryption;

import org.huanmin.utils.common.encryption.aes.AESCBCUtil;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class AESCBCUtilsTest {

    @Test
    public void show1(){
        String key="1234567891234567";
        String iv="39400A13BD6BE6C8";

        System.out.println("--------AES_CBC加密解密---------");
        String cbcResult = AESCBCUtil.encrypt("aaaaaa",key,iv);
        System.out.println("aes_cbc加密结果:" + cbcResult);
        System.out.println("---------解密CBC---------");
        String cbcDecrypt = AESCBCUtil.decrypt(cbcResult,key,iv);
        System.out.println("aes解密结果:" + cbcDecrypt);
    }
    @Test
    public void show2(){
        String key="1234567891234567";
        String iv="39400A13BD6BE6C8";
        String str="aaaaaa";
        System.out.println("--------AES_CBC加密解密---------");
        byte[] encrypt = AESCBCUtil.encrypt(str.getBytes(StandardCharsets.UTF_8), key, iv);
        System.out.println("aes_cbc加密结果:" + Arrays.toString(encrypt));
        System.out.println("---------解密CBC---------");
        byte[] decrypt = AESCBCUtil.decrypt(encrypt, key, iv);

        System.out.println("aes解密结果:" + new String(decrypt,StandardCharsets.UTF_8));
    }

}
