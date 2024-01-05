package com.utils.common.encryption.aes;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

// 对称加密
public class AESECBUtils {


    // 加密
    @SneakyThrows
    public static String encrypt(String sSrc, String sKey)  {
        byte[] encrypt = encrypt(sSrc.getBytes(StandardCharsets.UTF_8), sKey);
        Base64.Encoder encoder = Base64.getEncoder();
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return encoder.encodeToString(encrypt);

    }

    // 解密
    @SneakyThrows
    public static String decrypt(String sSrc, String sKey)  {
        Base64.Decoder decoder=Base64.getDecoder();
        byte[] encrypted1 =decoder.decode(sSrc); // 先用base64解密
        return  new String(Objects.requireNonNull(decrypt(encrypted1, sKey)),StandardCharsets.UTF_8);
    }


    @SneakyThrows
    public static  byte[] encrypt(byte[] sSrc, String sKey)  {
        BasicsAes.checkKey(sKey);
        byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, BasicsAes.AES);
        Cipher cipher = Cipher.getInstance(BasicsAes.AES_EBC_PKCS5PADDING);//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return  cipher.doFinal(sSrc);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    // 解密
    @SneakyThrows
    public static byte[] decrypt(byte[] sSrc, String sKey)  {
        BasicsAes.checkKey(sKey);
        byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, BasicsAes.AES);
        Cipher cipher = Cipher.getInstance(BasicsAes.AES_EBC_PKCS5PADDING);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return  cipher.doFinal(sSrc);
    }




}
