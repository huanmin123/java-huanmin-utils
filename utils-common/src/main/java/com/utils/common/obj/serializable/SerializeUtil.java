package com.utils.common.obj.serializable;



import com.utils.common.base.UniversalException;
import com.utils.common.file.ReadFileBytesUtil;
import com.utils.common.file.WriteFileBytesUtil;

import java.io.*;

//对象序列化
public class SerializeUtil {
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
             UniversalException.logError(e);
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static void serializeToFile(Object object, File read) {
        byte[] serialize = serialize(object);
        WriteFileBytesUtil.writeByte(serialize, read, false);
    }

    public static <T> T unserialize(byte[] bytes, Class<T> tClass) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static <T> T readUnserialize(File read, Class<T> tClass) {
        byte[] bytes = ReadFileBytesUtil.readByte(read);
        return unserialize(bytes, tClass);

    }


}

