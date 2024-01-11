package com.huanmin.test.utils.utils_common.encryption;


import com.huanmin.utils.common.encryption.xor.XORUtil;

public class XORUtilTest {
    public static void main(String[] args) {

        XORUtil instance=  XORUtil.getInstance();
        String jiami= instance.encode("124ff","abc");
        System.out.println(jiami);
        String jiemi = instance.decode(jiami,"abc");
        System.out.println(jiemi);
        //针对 纯数字 进行加密  和解密
        int num_jiami= instance.code(543,"abv");
        System.out.println(num_jiami);
        int num_jiemi= instance.code(num_jiami,"abv");
        System.out.println(num_jiemi);

    }
}
