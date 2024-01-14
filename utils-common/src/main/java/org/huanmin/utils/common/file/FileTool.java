package org.huanmin.utils.common.file;

import org.huanmin.utils.common.base.NullUtil;
import org.huanmin.utils.common.string.StringUtil;
import org.huanmin.utils.common.base.UniversalException;
import info.monitorenter.cpdetector.io.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class FileTool {
    //文件编码获取
    public static String getFileCharsetName(File file) {
        try {
            if( file.length()==0){
               throw new RuntimeException("文件长度为0,无法获取编码");
            }
            // 获取 CodepageDetectorProxy 实例
            CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
            // 添加解析器，会使用到添加的后 2 个 ext 里的 jar 包
            detector.add(new ParsingDetector(false));
            detector.add(JChardetFacade.getInstance());
            detector.add(ASCIIDetector.getInstance());
            detector.add(UnicodeDetector.getInstance());
            Charset charset = detector.detectCodepage(file.toURI().toURL());
            if (charset != null) {
                return charset.name();
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    /**
     * 文件编码转换  ,将当前文件的编码转换为指定的编码
     *
     * @param oldFile      旧的文件
     * @param oldCoding    旧文件编码
     * @param targetFile   目标文件
     * @param targetCoding 目标文件编码
     */
    public static void fileCodingTransition(
            File oldFile, String oldCoding, File targetFile, String targetCoding) {
        try (BufferedReader br =
                     new BufferedReader(
                             new InputStreamReader(Files.newInputStream(oldFile.toPath()), oldCoding));
             BufferedWriter bw =
                     new BufferedWriter(
                             new OutputStreamWriter(
                                     Files.newOutputStream(targetFile.toPath()), targetCoding));) {
            char[] c = new char[1024 * 8];
            int len = 0;
            while ((len = br.read(c)) != -1) {
                bw.write(c, 0, len);
            }
        } catch (Exception e) {
             UniversalException.logError(e);
        }
    }
    
    public static void fileCodingTransition(
            File oldFile, File targetFile, String targetCoding) {
        String fileCharsetName = FileTool.getFileCharsetName(oldFile);
        FileTool.fileCodingTransition(oldFile, fileCharsetName, targetFile, targetCoding);
        
    }
    
    /**
     * 读取文件数字,每次读取一个数字
     * 按照分割符读取文件数字,每次读取一个数字   建议分割符号:  "\u0001|\u0001"
     *
     * @param read  读取文件
     * @param split 分隔符
     * @return
     */
    public static void readByteNumSplit(File read, String split, Consumer<Integer> consumer) {
        byte[] data = new byte[20];//一个数字最大20个字节
        Long index = 0L;
        try (RandomAccessFile r = new RandomAccessFile(read, "r");) {
            while (r.read(data) != -1) {
                String str = new String(data, StandardCharsets.UTF_8);
                str = str.replaceAll("\r\n|\n", split);//因为可能出现\r\n被截断成\n的情况,所以需要将换行符替换成分隔符
                String[] split1 = str.split(split);
                String num = split1[0];
                if (NullUtil.isEmpty(num)) {//如果是空的,则跳过
                    index += 1;
                    r.seek(index);
                    data = new byte[20];
                    continue;
                }
                boolean numeric = StringUtil.isNumeric(num);
                if (numeric) {
                    consumer.accept(Integer.parseInt(num));
                    index += num.length() + 1;
                    r.seek(index);
                    data = new byte[20];
                } else {
                    index += num.length() + 1;
                    r.seek(index);
                }
            }
        } catch (IOException e) {
             UniversalException.logError(e);
        }
        
    }
    
    /**
     * 读取文件数字,每次读取一个数字
     *
     * @param read     读取文件
     * @param width    按照数字宽度读取文件数字 ,10位是几十亿 11位是几百亿....
     * @param consumer 00000000001  使用 Long.parseLong(str);是可以转换为1的
     */
    public static void readByteNumWidth(File read, Integer width, Consumer<String> consumer) {
        byte[] data = new byte[width];
        Long index = 0L;
        try (RandomAccessFile r = new RandomAccessFile(read, "r");) {
            while (r.read(data) != -1) {
                String str = new String(data, StandardCharsets.UTF_8);
                if (str.contains(System.lineSeparator())) {//如果包含换行符,则跳过
                    index = index + System.lineSeparator().length();
                    r.seek(index);
                    data = new byte[width];
                    continue;
                }
                
                boolean numeric = StringUtil.isNumeric(str);
                if (numeric) {
                    consumer.accept(str);
                    index += width;
                    r.seek(index);
                    data = new byte[width];
                } else {
                    index += width;
                    r.seek(index);
                }
            }
        } catch (IOException e) {
             UniversalException.logError(e);
        }
        
    }
    
    //截取文件指定行之后的内容   到指定结束行之前的内容
    public static String cutFileLine(File file, String startLine, String endLine) {
        AtomicLong startLineIndex = new AtomicLong();//开始行的索引
        AtomicLong endLineIndex = new AtomicLong();//结束行的索引
        AtomicBoolean start = new AtomicBoolean(false);
        RandomAccessFileUtil.randomAccessFileReadLine(file, (line, index) -> {
            try {
                if (line.contains(startLine)) {
                    start.set(true);
                    startLineIndex.set(index.getFilePointer());
                }
                if (line.contains(endLine)) {
                    endLineIndex.set(index.getFilePointer()-endLine.length());
                    return "break";
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return line;
        });
        
        if (start.get()) {
            StringBuilder stringBuilder = new StringBuilder();
            AtomicBoolean init = new AtomicBoolean(true);
            RandomAccessFileUtil.randomAccessFileReadLine(file, (line, index) -> {
                try {
                    if (init.get()) {
                        init.set(false);
                        index.seek(startLineIndex.get());
                        return "continue";
                    }
                    if (  index.getFilePointer() <= endLineIndex.get()) {
                        stringBuilder.append(line).append(System.lineSeparator());
                    }else{
                        return "break";
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return "continue";
            });
            return stringBuilder.toString();
        }
        return null;
    }
    
    //截取文件指定行之后的内容一直到指定行的内容  ,然后写入到指定文件
    public static void cutFileLineToWrite(File inFile, File outFile, String startLine, String endLine) {
        String s = cutFileLine( inFile, startLine, endLine);
        if (NullUtil.notEmpty(s)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(outFile))) {
                bw.write(s);
            } catch (IOException e) {
                 UniversalException.logError(e);
            }
        }
    }
}
