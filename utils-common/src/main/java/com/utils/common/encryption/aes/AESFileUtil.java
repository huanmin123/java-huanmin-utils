package com.utils.common.encryption.aes;

import com.utils.common.container.ArrayByteUtil;
import com.utils.common.file.FileUtil;
import com.utils.common.file.ReadWriteFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;

// 文件加密  ,支持 所有文件的格式 , 加密速度ms~s  测试1个g文件也就1秒~2秒左右  ,加密后的文件大小几乎没有变化

public class AESFileUtil {
    private static final Logger logger = LoggerFactory.getLogger(AESFileUtil.class);
    
    // 加密缓存大小
    private static final int encryptionPrefixByte = 2048;
    // 解密缓存大小 必须和加密缓存是一致的 ,这个是AES加密后的膨胀,需要自己测试下 ,目前2048=2064
    
    //     二进制加密和解密
    private static final int encryptionPrefixByte1 = 10240;
    
    private static final String iv = "39400A13BD6BE6C8"; // 偏移量
    private static String encryptionPrefix = "_encrypt_";
    private static String decryptPrefix = "_decrypt_";
    // 是否全加密, 因为除了txt文件之外的文件我们都可以认为是,二进制文件
    // 那么我们只需要更改一部分字节就能完成文件的加密,这样能大大的提高了文件的加密速度
    // false 不是全部加密,那么真对二进制的文件我们会采用 加密前10240个字节的数据
    // 因为文件重要信息都是保存在文件的开头,我们把这些信息进行加密后,软件就不知道这是什么文件了
    // 如果是true 那么无论什么文件类型,每一个字节都有会进行加密的 ,这种加密比较安全但是稍微会影响速度
    // 如果不是安全性非常高的话,建议就false就行了

    
    /**
     *
     * @param absolutePath
     * @param key
     * @param full  true全部加密  ,false只加密前10240个字节   文本文件必须全部加密否则会出现部分明文的现象
     * @throws FileNotFoundException
     */
    public static void encryption(String absolutePath, String key,boolean full) throws FileNotFoundException {
        String prefixPathAndName = FileUtil.filePartInfo(absolutePath, "prefixPathAndName");
        String suffixAndSpot = FileUtil.filePartInfo(absolutePath, "suffixAndSpot");
        String write =
                prefixPathAndName
                        + encryptionPrefix
                        + FileUtil.getGenerateFileName()
                        + suffixAndSpot;
        // 如果是txt 那么就走全加密, 否则会出现部分明文的现象
        if (full) {
            ReadWriteFileUtils.readWriteByteHandle(
                    new File(absolutePath),
                    new File(write),
                    false,
                    encryptionPrefixByte,
                    (bytes) -> {
                        // 加密
                        byte[] encrypt = AESCBCUtils.encrypt(bytes, key, iv);
                        //字节长度填充
                        encrypt = ArrayByteUtil.fillBytes(encrypt,  encryptionPrefixByte*2);
                        return encrypt;
                    });
        }else{ //二进制文件
            AtomicBoolean start = new AtomicBoolean(true);
            //部分加密
            ReadWriteFileUtils.readWriteByteHandle(
                    new File(absolutePath),
                    new File(write),
                    true,
                    encryptionPrefixByte1,
                    (bytes) -> {
                        if (start.get()) {
                            start.set(false);
                            // 加密
                            byte[] encrypt = AESCBCUtils.encrypt(bytes, key, iv);
                            //字节长度填充
                            bytes = ArrayByteUtil.fillBytes(encrypt,  encryptionPrefixByte1*2);
                        }
                        return bytes;
                    });
        }
      
    }
    
    public static void decrypt(String absolutePath, String key,boolean full) {
        String suffix = FileUtil.filePartInfo(absolutePath, "suffix");
        String write = absolutePath.replaceAll(encryptionPrefix, decryptPrefix);
        new File(write).delete();
        if (full) {// 如果是txt 那么就走全解密, 否则会出现部分明文的现象
            ReadWriteFileUtils.readWriteByteHandle(
                    new File(absolutePath),
                    new File(write),
                    false,
                    encryptionPrefixByte*2,
                    (bytes) -> {
                        bytes = ArrayByteUtil.getActualBytes(bytes);
                        // 解密
                        byte[] bytes1 = AESCBCUtils.decrypt(bytes, key, iv);
                        return bytes1;
                    });
        }else{ //二进制文件
            AtomicBoolean start = new AtomicBoolean(true);
            //部分解密
            ReadWriteFileUtils.readWriteByteHandle(
                    new File(absolutePath),
                    new File(write),
                    true,
                    encryptionPrefixByte1*2,
                    (bytes) -> {
                        if (start.get()) {
                            start.set(false);
                            bytes = ArrayByteUtil.getActualBytes(bytes);
                            // 解密
                            bytes = AESCBCUtils.decrypt(bytes, key, iv);
                        }
                        return bytes;
                    });
        }
    }
}
