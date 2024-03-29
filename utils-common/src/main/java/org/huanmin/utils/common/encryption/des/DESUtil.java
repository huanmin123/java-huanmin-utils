package org.huanmin.utils.common.encryption.des;

import org.huanmin.utils.common.base.UniversalException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Objects;

//密钥的长度64位，8个字节。
public class DESUtil {
    public static final String DES = "DES";
    public static final String charset = "UTF-8"; // 编码格式；默认null为GBK
    public static final int keysizeDES = 0;

    private static DESUtil instance;

    private DESUtil() {
    }

    // 单例
    public static DESUtil getInstance() {
        if (instance == null) {
            synchronized (DESUtil.class) {
                if (instance == null) {
                    instance = new DESUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 使用 DES 进行加密
     */
    public String encode(String res, String key) {
        return keyGeneratorES(res, DES, key, keysizeDES, true);
    }

    /**
     * 使用 DES 进行解密
     */
    public String decode(String res, String key) {
        return keyGeneratorES(res, DES, key, keysizeDES, false);
    }

    // 使用KeyGenerator双向加密，DES/AES，注意这里转化为字符串的时候是将2进制转为16进制格式的字符串，不是直接转，因为会出错
    private String keyGeneratorES(String res, String algorithm, String key, int keysize, boolean isEncode) {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(algorithm);
            if (keysize == 0) {
                byte[] keyBytes = key.getBytes(charset);
                kg.init(new SecureRandom(keyBytes));
            } else if (key == null) {
                kg.init(keysize);
            } else {
                byte[] keyBytes = key.getBytes(charset);
                kg.init(keysize, new SecureRandom(keyBytes));
            }
            SecretKey sk = kg.generateKey();
            SecretKeySpec sks = new SecretKeySpec(sk.getEncoded(), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            if (isEncode) {
                cipher.init(Cipher.ENCRYPT_MODE, sks);
                byte[] resBytes = res.getBytes(charset);
                return parseByte2HexStr(cipher.doFinal(resBytes));
            } else {
                cipher.init(Cipher.DECRYPT_MODE, sks);
                return new String(cipher.doFinal(Objects.requireNonNull(parseHexStr2Byte(res))));
            }
        } catch (Exception e) {
             UniversalException.logError(e);
        }
        return null;
    }

    // 将二进制转换成16进制
    private String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    // 将16进制转换为二进制
    private byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


}

