package com.huanmin.test.utils_common.encryption;

import com.utils.common.encryption.des.DESUtil;

public class DESUtilTest {
    public static void main(String[] args) {
        DESUtil instance= DESUtil.getInstance();
        String jiami=  instance.encode("12345abc67","abc");
        System.out.println(jiami);
        String jiemi=instance.decode(jiami,"abc");
        System.out.println(jiemi);

    }
}
