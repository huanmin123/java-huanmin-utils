package com.huanmin.utils.common.encryption.aes;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Objects;

// 比EBC方式多了一个偏移量   一般偏移量写死 ,公钥可以提供, 这样解密必须使用加密时候的代码才行
public class AESCBCUtil {
    private static final Logger logger = LoggerFactory.getLogger(AESFileUtil.class);

    /**
     * AES_CBC加密
     *
     * @param content 待加密内容
     * @return
     */
    public static String encrypt(String content, String KEY, String IV_SEED) {
        byte[] encrypted = content.getBytes(StandardCharsets.UTF_8);
        Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(encrypt(encrypted, KEY, IV_SEED));
    }

    /**
     * AES_CBC解密
     *
     * @param content 待解密内容
     * @return
     */
    @SneakyThrows
    public static String decrypt(String content, String KEY, String IV_SEED) {
        // 先进行Base64解码
        Decoder decoder=Base64.getDecoder();
        byte[] decodeBase64 =decoder.decode(content); // 先用base64解密
        return new String(Objects.requireNonNull(decrypt(decodeBase64, KEY, IV_SEED)), StandardCharsets.UTF_8);
    }

    /**
     * AES_CBC加密
     *
     * @param content 待加密内容
     * @return
     */
    @SneakyThrows
    public static byte[] encrypt(byte[] content, String key, String iv) {
        BasicsAes.checkKeyAndIV(key, iv);
        // 判断秘钥是否为16位
        // 对密码进行编码
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        // 设置加密算法，生成秘钥
        SecretKeySpec skeySpec = new SecretKeySpec(bytes, BasicsAes.AES);
        // "算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance(BasicsAes.AES_CBC_PKCS5PADDING);
        // 偏移
        IvParameterSpec iv1 = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        // 选择加密
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv1);
        // 根据待加密内容生成加密的字节数组
        return cipher.doFinal(content);
    }

    /**
     * AES_CBC解密
     *
     * @param content 待解密内容
     * @return
     */
    @SneakyThrows
    public static byte[] decrypt(byte[] content, String key, String iv) {
        BasicsAes.checkKeyAndIV(key, iv);
        // 对密码进行编码
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        // 设置解密算法，生成秘钥
        SecretKeySpec skeySpec = new SecretKeySpec(bytes, BasicsAes.AES);
        // 偏移
        IvParameterSpec iv1 = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        // "算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance(BasicsAes.AES_CBC_PKCS5PADDING);
        // 选择解密
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv1);
        // 根据待解密内容进行解密
        return cipher.doFinal(content);
    }


    
    //秘钥获取
    public static String getKey(String key) {
        // 密钥补位
        int plus= 16-key.length();
        byte[] data = key.getBytes(StandardCharsets.UTF_8);
        byte[] raw = new byte[16];
        byte[] plusbyte={ 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        for(int i=0;i<16;i++)
        {
            if (data.length > i)
                raw[i] = data[i];
            else
                raw[i] = plusbyte[plus];
        }
        return new String(raw, StandardCharsets.UTF_8);
    }
    
}
