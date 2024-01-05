package com.utils.common.encryption.aes;

public class BasicsAes {
    public static final String AES = "AES";
    public static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";
    public static final String AES_EBC_PKCS5PADDING = "AES/ECB/PKCS5Padding";
    private static final int KEY_LENGTH_16 = 16;
    private static final int KEY_LENGTH_32 = 32;
    /**
     * 检查key是否合法
     *
     * @param key {@link String}秘钥信息
     * @throws Exception 异常信息
     */
    public  static void checkKey(String key) throws Exception {
        if (key == null || key.length() != KEY_LENGTH_16 && key.length() != KEY_LENGTH_32) {
            throw new Exception("加密秘钥不正确");
        }
    }

    /**
     * 检查偏移矢量是否合法
     *
     * @param iv {@link String} 偏移矢量
     * @throws Exception 异常信息
     */
    public static void checkIV(String iv) throws Exception {
        if (iv == null || iv.length() != KEY_LENGTH_16) {
            throw new Exception("偏移矢量不正确，必须为16位");
        }
    }
    public static void checkKeyAndIV(String key,String iv) throws Exception {
        checkKey(key);
        checkIV(iv);
    }


}
