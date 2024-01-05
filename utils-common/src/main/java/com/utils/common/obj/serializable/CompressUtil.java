package com.utils.common.obj.serializable;


import com.utils.common.file.ReadFileBytesUtil;
import com.utils.common.file.WriteFileBytesUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 高级压缩对象 (对字符串这些压缩比较强) ,如果对象全是数字的话那么普通的序列化比较好
 * @author huanmin
 */

@Slf4j
public class CompressUtil {

    //序列化
    public  static  byte[] writeCompressObject(Object object){

        byte[] data = null;
        try{
            //建立字节数组输出流
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            GZIPOutputStream gzout = new GZIPOutputStream(bao);
            ObjectOutputStream out = new ObjectOutputStream(gzout);
            out.writeObject(object);
            out.flush();
            out.close();
            gzout.close();

            data = bao.toByteArray();
            bao.close();
        }catch(IOException e){
            System.err.println(e);
        }
        return data;
    }

    // 序列化后写入文件中
    public  static  void writeCompressObjectToFile(Object object,File write){
        WriteFileBytesUtil.writeByte( writeCompressObject(object),write,false);
    }

     //反序列化
    public static <T>  T readCompressObject(byte[] data,Class<T> tClass){
        Object object = null;
        try{
            //建立字节数组输入流
            ByteArrayInputStream i = new ByteArrayInputStream(data);
            //建立gzip解压输入流
            GZIPInputStream gzin=new GZIPInputStream(i);
            //建立对象序列化输入流
            ObjectInputStream in = new ObjectInputStream(gzin);
            //按制定类型还原对象
            object = in.readObject();
        }catch(ClassNotFoundException e){
            System.err.println(e.getMessage());
        }catch (IOException ex) {
            System.err.println(ex);
        }

        return (T)object;
    }
    public static <T>  T readFileCompressObject(File read,Class<T> tClass){
       return readCompressObject( ReadFileBytesUtil.readByte(read),tClass);
    }



}

