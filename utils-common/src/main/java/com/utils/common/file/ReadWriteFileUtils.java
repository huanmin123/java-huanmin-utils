package com.utils.common.file;

import com.utils.common.base.UniversalException;
import com.utils.common.container.ArrayByteUtil;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.function.Function;

/**
 * 文件读写(字符流,字节流)
 */
public class ReadWriteFileUtils {
    private static final Logger logger = LoggerFactory.getLogger(ReadWriteFileUtils.class);
  


    /**
     * 文件按行读取,进行处理, 然后在写入
     *
     * @param readPath  读取文件的路径
     * @param writePath 写入文件的路径
     * @param append    是否是追加的方式 , true 追加方式, false覆盖方式
     * @param function  ,需要处理的函数 ,每次返回一条数据进行处理,然后将结果写入到文件中
     */
    public static void readWriteStrLine(
            File readPath, File writePath, boolean append, Function<String, String> function) {
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(Files.newInputStream(readPath.toPath())));
             BufferedWriter bw =
                     new BufferedWriter(
                             new OutputStreamWriter(new FileOutputStream(writePath, append)));) {
            StringBuilder builder = new StringBuilder(1024 * 1024);
            int sizelen = 0;
            String line = null;
            while ((line = br.readLine()) != null) {
                line = function.apply(line); // 行处理函数
                builder.append(line).append("\n");
                if (sizelen == 20) { // 每20行 写入一次
                    bw.write(builder.toString());
                    sizelen = 0;
                    builder = new StringBuilder(1024 * 1024); // 清空缓冲区
                }
                sizelen++;
            }
            // 将余下 的内容全部写入
            bw.write(builder.toString());
            builder = new StringBuilder();
        } catch (Exception e) {
             UniversalException.logError(e);
        }
    }
    
    public static void readWriteStrLine(File readPath, File writePath, Function<String, String> function) {
        readWriteStrLine(readPath, writePath, false, function);
    }


    @SneakyThrows
    public static void readWriteByte(File file, File writePath, boolean append) {
        readWriteByte(new FileInputStream(file), writePath, append);
    }

    /**
     * @param fileInputStream 文件流前端上传到后端就是文件流 或者 FileInputStream fis1 = new FileInputStream(readPath);
     * @param writePath       将文件写入到哪里
     * @param append          是否是追加的方式 , true 追加方式, false覆盖方式
     */
    public static void readWriteByte(
            FileInputStream fileInputStream, File writePath, boolean append) {
        try (BufferedInputStream fis = new BufferedInputStream(fileInputStream);
             FileOutputStream fos1 = new FileOutputStream(writePath, append);
             BufferedOutputStream fos = new BufferedOutputStream(fos1);) {
            byte[] data = new byte[4096];
            int len;
            while ((len = fis.read(data)) != -1) {
                fos.write(data, 0, len); // 写入数据
            }
        } catch (IOException e) {
             UniversalException.logError(e);
        };
    }
    
  

    /**
     * 二进制读写文件 ,每次读取size个byte,然后进行处理,然后写入到文件中
     *
     * @param filePath       读取的文件路径
     * @param writePath      写入的文件
     * @param append         是否是追加的方式 , true 追加方式, false覆盖方式
     * @param size           每次读取多少byte (int)
     * @param handle         读写中处理

     */
    public static void readWriteByteHandle(
            File filePath,
            File writePath,
            boolean append,
            int size,
            Function<byte[],byte[]> handle) {
        try (BufferedInputStream fis = new BufferedInputStream(Files.newInputStream(filePath.toPath()));
             BufferedOutputStream fos =
                     new BufferedOutputStream(new FileOutputStream(writePath, append));) {

            byte[] data = new byte[size];
            int len;
            while ((len = fis.read(data)) != -1) {
                if (len < size) {//表示读取到了文件的末尾,那么需要进行数组的调整,不然会出现乱码
                    data = ArrayByteUtil.getActualBytes(data);
                }
                // 处理函数
                byte[] apply = handle.apply(data);
                // 获取处理后的数组实际的长度
                fos.write(apply, 0, apply.length); // 写入数据
                data = new byte[size]; // 清空数组
            }
        } catch (IOException e) {
             UniversalException.logError(e);
            return;
        } catch (Exception e) {
             UniversalException.logError(e);
        }
    }




    
}
