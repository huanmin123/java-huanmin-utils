package com.huanmin.test.utils.utils_common.encryption;


import com.huanmin.utils.common.encryption.rsa.RSAUtil;

public class RSAUtilsTest {

    public static void main(String[] args) {
        //生成公钥和私钥
        RSAUtil.genKeyPair();
        String  publicKey=RSAUtil.publicKey(); //公钥
        String  privateKey=RSAUtil.privateKey(); //私钥
        //加密字符串
        String message = "df723820";
        System.out.println("随机生成的公钥为:" + publicKey);
        System.out.println("随机生成的私钥为:" + privateKey);
        //加密 生成密文
        String messageEn = RSAUtil.encrypt(message, publicKey);
        System.out.println("加密后的字符串为:" + messageEn);
        //解密
        String messageDe = RSAUtil.decrypt(messageEn, privateKey);
        System.out.println("还原后的字符串为:" + messageDe);
    }
}
