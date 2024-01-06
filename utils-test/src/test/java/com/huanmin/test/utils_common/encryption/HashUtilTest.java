package com.huanmin.test.utils_common.encryption;

import com.utils.common.encryption.hash.HashUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(DES3UtilsTest.class);
// 下面这么多算法 区别就是 生成的长度不同,长度越长被破解的可能就越低,但是速度就会相对慢些
// 看你业务场景  最短的就是MD5 也是最快的

    @Test
    public void testMd5() { // 绝对唯一
        String srcStr = "user=8888118181&pass=123125412";
        String result = HashUtil.md5(srcStr);
        System.out.println("md5 result is " + result);
//a3669634948c69143e5d7735d4a89673
    }

    @Test
    public void testSha1() {  // 绝对唯一
        String srcStr = "user=8888118181&pass=123125412";
        String result = HashUtil.sha1(srcStr);
        System.out.println("sha1 result is " + result);
//ae99531df79f5833fae4312c39d1bf340a042d59
    }

    @Test
    public void testSha256() {
        String srcStr = "user=8888118181&pass=123125412";
        String result = HashUtil.sha256(srcStr);
        System.out.println("sha256 result is " + result);
//b0237c74375728ec072e876cdd9e9ae4a686ad59466d7e7d6a61d79d11ee6dd2
    }
    @Test
    public void testSha224() {
        String srcStr = "user=8888118181&pass=123125412";
        String result = HashUtil.sha224(srcStr);
        System.out.println("sha224 result is " + result);
//f534625b11c7f1502e44d25c133298f555f97366e9b00c93d40a05d1
    }

    @Test
    public void testSha384() {
        String srcStr = "zheng";
        String result = HashUtil.sha384(srcStr);
        System.out.println("sha384 result is " + result);
//e60732c544571981ee53b2c7bac80d4c9c8316e874c7c9416b5dd3434db8d58b0860f23533f1e563614cfacebd552d86
    }

    @Test
    public void testSha512() {
        String srcStr = "zheng";
        String result = HashUtil.sha512(srcStr);
        System.out.println("sha512 result is " + result);
//76092fabe77d72bea83567773f605b3e014bc63f7b4e04269cee019cb8bacbf725f90b23fbccbe393e1fdae49993c4285cbb0be69711b6475abdef9c60b8caf7
    }
}

