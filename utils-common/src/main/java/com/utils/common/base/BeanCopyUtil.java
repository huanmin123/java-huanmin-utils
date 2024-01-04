package com.utils.common.base;

import org.springframework.beans.BeanUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * BeanCopyUtil
 * </p>
 *
 * @author zhanghao
 */
public class BeanCopyUtil extends BeanUtils {

    //将a对象中的属性值复制到b对象中
    public static void copyProperties(Object a, Object b) {
        BeanUtils.copyProperties(a, b);
    }
    //拷贝a的list,生成b的list,而b无须和a是同一个类,只要有相同的属性即可
    public static <S, T> List<T> copyListProperties(List<S> sources, Class<T> target) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = BeanUtils.instantiateClass(target);
            copyProperties(source, t);
            list.add(t);
        }
        return list;
    }




    //深拷贝
    public static <T extends Serializable> T deepCopy(T obj) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (T) ois.readObject();
    }

    //深拷贝list
    public static <T extends Serializable> List<T> deepCopyList(List<T> list) throws IOException, ClassNotFoundException {
        List<T> newList = new ArrayList<>();
        for (T item : list) {
            newList.add(deepCopy(item));
        }
        return newList;
    }
}
