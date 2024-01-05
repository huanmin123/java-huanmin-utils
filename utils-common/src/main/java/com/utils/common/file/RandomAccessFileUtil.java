package com.utils.common.file;

import com.utils.common.base.NullUtils;
import com.utils.common.container.ArrayByteUtil;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.BiFunction;

public class RandomAccessFileUtil {
    
    
    /**
     * 读取指定文件的指定位置的指定字节数
     *
     * @param file  读取的文件
     * @param begin 从什么地方开始读取
     * @param bytes 读取多少字节到数组中
     */
    public static void randomAccessFileReadBytes(
            File file, int begin, byte[] bytes) {
        readFileIsUTF_8(file);
        try (RandomAccessFile r = new RandomAccessFile(file, "r");) {
            r.seek(begin);
            r.read(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 按行读取文件
     *
     * @param file     读取的文件
     * @param function 读取的时候的处理函数,返回值是写入的内容,当返回值是null的时候,则停止读取
     */
    public static void randomAccessFileReadLine(File file, BiFunction<String, RandomAccessFile, String> function) {
        randomAccessFileReadWritLine(file, file, function, (b,w)->"continue");
    }
    
    //按行写入文件
    public static void randomAccessFileWriteLine(File file, BiFunction<String, RandomAccessFile, String> function) {
        randomAccessFileReadWritLine(file, file, null, function);
    }
    
    //按字节读取文件
    public static void randomAccessFileReadBytes(File file, int size, BiFunction<byte[], RandomAccessFile, byte[]> function) {
        randomAccessFileReadWritBytes(file, file, size, function, (b,w)->"continue".getBytes(StandardCharsets.UTF_8));
    }
    
    //按字节写入文件
    public static void randomAccessFileWriteBytes(File file, int size, BiFunction<byte[], RandomAccessFile, byte[]> function) {
        randomAccessFileReadWritBytes(file, file, size,null, function);
    }
    
    
    /**
     * 按行读写文件 ,可控制读写
     *
     * @param rfile 读取的文件
     * @param wfile 写入的文件
     * @param fr    读取的时候的处理函数,返回值是写入的内容,当返回值是blank的时候,则停止读取,如果返回值是continue,则继续读取,如果返回值是null报错,如果返回值是其他,则执行到写入的步骤
     * @param fw    写入的时候的处理函数,返回值是写入的内容,当返回值是blank的时候,则停止读取,如果返回值是continue,则继续读取,如果返回值是null报错
     *              如果想实现追加写入,则需要在写入的时候w.seek( w.length());将文件的指针移动到文件的末尾
     */
    public static void randomAccessFileReadWritLine(File rfile,
                                                    File wfile,
                                                    BiFunction<String, RandomAccessFile, String> fr,
                                                    BiFunction<String, RandomAccessFile, String> fw
    ) {
        
        //判断文件是否是UTF-8编码,否则抛出异常
        readFileIsUTF_8(rfile);
        fr = NullUtils.notEmptyElse(fr, (str, r1) -> str);
        fw = NullUtils.notEmptyElse(fw, (str1, w1) -> str1);//如果写取规则是null,那么默认写的时候不做任何处理,并且直接跳过写入
        try (RandomAccessFile r = new RandomAccessFile(rfile, "r");
             RandomAccessFile w = new RandomAccessFile(wfile, "rw");
        ) {
            String line;
            while ((line = r.readLine()) != null) {
                //因为RandomAccessFile的readLine读取的时候是按照ISO_8859_1编码读取的,所以需要转换成UTF-8
                line = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                String apply = fr.apply(line, r);
                if (apply == null) {
                    throw new RuntimeException("读取的时候出现了错误,返回值是null");
                } else if (apply.equals("continue")) {
                    continue;
                } else if (apply.equals("break")) {
                    break;
                } else  {
                    String apply1 = fw.apply(apply, w);
                    if (apply1 == null) {
                        throw new RuntimeException("写入的时候出现了错误,返回值是null");
                    } else if (apply1.equals("continue")) {
                        continue;
                    } else if (apply1.equals("break")) {
                        break;
                    }  else {
                        w.write(apply1.getBytes(StandardCharsets.UTF_8));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 按字节读写文件 ,可控制读写
     *
     * @param rfile 读取的文件
     * @param wfile 写入的文件
     * @param size  每次读取的字节数
     * @param fr    读取的时候的处理函数,返回值是写入的内容,当返回值是null的时候,则停止读取
     * @param fw    写入的时候的处理函数,返回值是写入的内容,当返回值是null的时候,则停止写入
     *              <p>
     *              如果想实现追加写入,则需要在写入的时候w.seek( w.length());将文件的指针移动到文件的末尾
     */
    public static void randomAccessFileReadWritBytes(File rfile,
                                                    File wfile,
                                                    int size,
                                                    BiFunction<byte[], RandomAccessFile, byte[]> fr,
                                                    BiFunction<byte[], RandomAccessFile, byte[]> fw
    
    ) {
        //判断文件是否是UTF-8编码,否则抛出异常
        readFileIsUTF_8(rfile);
        fr = NullUtils.notEmptyElse(fr, (bytes, r1) -> bytes);//如果读取规则是null,那么默认读取的时候不做任何处理
        fw = NullUtils.notEmptyElse(fw, (bytes, w1) -> bytes);//如果写取规则是null,那么默认写的时候不做任何处理,并且直接跳过写入
        try (RandomAccessFile r = new RandomAccessFile(rfile, "r");
             RandomAccessFile w = new RandomAccessFile(wfile, "rw");
        ) {
            byte[] bytes = new byte[size];
            int len=0;
            label1:
            label2:
            while ((len=r.read(bytes)) != -1) {
                byte[] apply = fr.apply(bytes, r);
                if (apply == null) {
                    throw new RuntimeException("读取的时候出现了错误,返回值是null");
                }
                String str = new String(apply, StandardCharsets.UTF_8);
                switch (str) {
                    case "continue":
                        continue;
                    case "break":
                        break label1;
                    default :
                        byte[] apply1 = fw.apply(apply, w);
                        if (apply1 == null) {
                            throw new RuntimeException("写入的时候出现了错误,返回值是null");
                        }
                        String str1 = new String(apply1, StandardCharsets.UTF_8);
                        switch (str1) {
                            case "continue":
                                continue;
                            case "break":
                                break label2;
                            default:
                                //如果读取的长度小于size那么表示读取到最后了,则需要将多余的字节去掉
                                if (len < size) {
                                    apply1 = ArrayByteUtil.getActualBytes(apply1);
                                }
                                w.write(apply1);
                                break;
                        }
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //判断文件是否是UTF-8编码,如果是则返回true,否则抛出异常
    public static void readFileIsUTF_8(File file) {
        String fileCharsetName = FileTool.getFileCharsetName(file);
        if (!Objects.equals(fileCharsetName, "UTF-8")) {
            throw new RuntimeException("文件编码不是UTF-8,请转换编码后再使用此方法,否则会出现乱码");
        }
    }
    
}
