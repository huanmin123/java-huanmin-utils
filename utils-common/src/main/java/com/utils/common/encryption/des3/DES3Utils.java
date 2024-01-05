package com.utils.common.encryption.des3;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.util.Base64;

//  秘钥  必须是大于24长度
public class DES3Utils {
    /**
     * 加密
     * @param text 待加密内容
     * @param key 公钥 长度32字符
     * @return
     */
    public static String DESEncrypt(String text, String key) {
        try {
            // 进行DES3加密后的内容的字节
            DESedeKeySpec dks = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey skey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            byte[] encryptedData = cipher.doFinal(text.getBytes("utf-8"));
            // 进行DES3加密后的内容进行BASE64编码

            Base64.Encoder encoder = Base64.getEncoder();

            return encoder.encodeToString(encryptedData).replaceAll("(\\\r\\\n|\\\r|\\\n|\\\n\\\r)", "");
        } catch (Exception e){
            return text;
        }
    }

    /**
     * 解密
     * @param text 待解密内容
     * @param key 公钥
     * @return
     */
    public static String DESDecrypt(String text, String key) {
        try {
            // 进行DES3加密后的内容进行BASE64解码
            Base64.Decoder decoder=Base64.getDecoder();
            byte[] base64DValue =decoder.decode(text); // 先用base64解密
            // 进行DES3解密后的内容的字节
            DESedeKeySpec dks = new DESedeKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey skey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            byte[] encryptedData = cipher.doFinal(base64DValue);
            return new String(encryptedData,"utf-8");
        } catch (Exception e) {
            return text;
        }
    }
}