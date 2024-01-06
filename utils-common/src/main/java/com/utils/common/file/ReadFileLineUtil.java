package com.utils.common.file;

import com.utils.common.base.UniversalException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class ReadFileLineUtil {
    //字符流 读取文件全部内容
    public static String readFileStrAll(File file) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())));) {
            String lin = null;
            while ((lin = br.readLine()) != null) {
                // 每次处理一行
                sb.append(lin).append("\n");
            }
        } catch (Exception e) {
             UniversalException.logError(e);
        }
        return sb.toString();

    }



    // 字符流  (每次处理一行)
    public static void readFileStrLine(File file, Consumer<String> consumer) {
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())));) {
            String lin = null;
            Integer lineNum = 0;
            while ((lin = br.readLine()) != null) {
                // 每次处理一行
                consumer.accept(lin);
            }
        } catch (Exception e) {
             UniversalException.logError(e);
        }
        long endTime = System.currentTimeMillis();
    }
    
    // 跳跃前多少行开始读取 ,从1开始
    public static void readFileSkipStrLine(File file, int skip, Consumer<String> consumer) {
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())));) {
            for (int i = 0; i < skip; i++) {
                br.readLine();
            }
            String lin = null;
            while ((lin = br.readLine()) != null) {
                // 每次处理一行
                consumer.accept(lin);
            }
        } catch (Exception e) {
             UniversalException.logError(e);
        }
    }
    
    //读取文件最后的一行
    public static String readFileEndLine(File file) {
        String line = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())));) {
            while (true) {
                String line1 = br.readLine();
                if (line1 == null) {
                    break;
                }
                line = line1;
            }
            
        } catch (Exception e) {
             UniversalException.logError(e);
        }
        return line;
    }
    
    
    /**
     * 读取文件的前多少行
     *
     * @Author: huanmin
     * @Date: 2022/6/19 0:42
     * @param: null
     * @return:
     * @Description: 方法功能和使用详细描述....
     */
    public static void readFileFirstLine(File file, int size, Consumer<String> consumer) {
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())));) {
            
            String lin = null;
            int count = 0;
            while ((lin = br.readLine()) != null) {
                count++;
                // 每次处理一行
                consumer.accept(lin);
                if (count == size) {
                    return;
                }
            }
        } catch (Exception e) {
             UniversalException.logError(e);
        }
    }
    
    //读取文件第一行
    public static String readFirstLineOne(File file) {
        AtomicReference<String> str = new AtomicReference<String>();
        readFileFirstLine(file, 1, str::set);
        return str.get();
    }
    
    // 每次读取指定行数
    public static void readFileStrAssignLine(
            File file, int lineSize, Consumer<StringBuilder> consumer) {
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())));) {
            StringBuilder builder = new StringBuilder(1024 * 1024);
            int sizelen = 0;
            String lin = null;
            while ((lin = br.readLine()) != null) {
                builder.append(lin);
                if (sizelen == lineSize) { // 每n行读取一次
                    consumer.accept(builder);
                    sizelen = 0;
                    builder = new StringBuilder(1024 * 1024); // 清空缓冲区
                }
                sizelen++;
            }
            // 将余下 的内容全部写入
            consumer.accept(builder);
            builder = new StringBuilder();
        } catch (Exception e) {
             UniversalException.logError(e);
        }
    }
}
