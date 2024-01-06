package com.huanmin.test.utils_common.encryption;


import com.utils.common.encryption.aes.AESCBCUtil;
import com.utils.common.encryption.aes.AESECBUtil;

import java.util.UUID;

public class AESECBUtilsTest {


  public static void main(String[] args) throws Exception {
    /*
     * 此处使用AES-128-ECB加密模式，key需要为16位。
     */
//    String cKey = "1234567890123456";
    String cKey = AESCBCUtil.getKey(UUID.randomUUID().toString());
    // 需要加密的字串
    String cSrc = "www.gowhere.so";
    System.out.println(cSrc);
    // 加密
    String enString = AESECBUtil.encrypt(cSrc, cKey);
    System.out.println("加密后的字串是：" + enString);

    // 解密
    String DeString = AESECBUtil.decrypt(enString, cKey);
    System.out.println("解密后的字串是：" + DeString);


  }



}
