package com.utils.common.obj.copy;

import com.utils.common.base.UniversalException;
import com.utils.common.obj.reflect.MethodUtil;
import org.springframework.beans.BeanUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 建议在1000个以内copy对象时使用 ,大量对象使用BeanCopierUtil
 * 对象内必须有get set方法
 * </p>
 *
 * @author zhanghao
 */
public class BeanCopyUtil extends BeanUtils {

    //将a对象中的属性值复制到b对象中,浅拷贝
    public static void copyProperties(Object a, Object b) {
        BeanUtils.copyProperties(a, b);
    }

    //将a对象中的属性值复制到b对象中,忽略null值和空字符串,忽略大小写
    public static void copyPropertiesIgnoreNull(Object a, Object b) {
        BeanUtils.copyProperties(a, b, getNullPropertyNames(a));
    }

    private static String[] getNullPropertyNames(Object source) {
        try {
            List<String> list = new ArrayList<>();
            Class<?> aClass = source.getClass();
            //获取全部的属性
            Field[] declaredFields = aClass.getDeclaredFields();
            //遍历属性
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                Object object = declaredField.get(source);
                if (object == null) {
                    list.add(declaredField.getName());
                } else if (object instanceof String) {
                    if ("".equals(object)) {
                        list.add(declaredField.getName());
                    }
                }
            }
            return list.toArray(new String[list.size()]);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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


    //深拷贝list
    public static <T extends Serializable> List<T> deepCopyList(List<T> list) throws IOException, ClassNotFoundException {
        List<T> newList = new ArrayList<>();
        for (T item : list) {
            newList.add(deepCopy(item));
        }
        return newList;
    }


    //深拷贝(序列化方式)
    public static <T extends Serializable> T deepCopy(T obj)  {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param source 源文件的
     * @param target 目标文件
     * @return
     * @throws Exception
     */
    // 该方法实现对Customer对象的拷贝操作
    public static <T> void copy(T source, T target, String... ignoreProperties) {
        try {
            Class<?> classType = source.getClass();
            //获得对象的所有成员变量
            Field[] fields = classType.getDeclaredFields();
            for (Field field : fields) {
                //获取成员变量的名字
                String name = field.getName();//获取成员变量的名字，此处为id，name,age
                //过滤serialVersionUID
                if ("serialVersionUID".equals(name)) {
                    continue;
                }
                //排除ignoreProperties
                if (ignoreProperties != null && ignoreProperties.length > 0) {
                    boolean flag = false;
                    for (String ignoreProperty : ignoreProperties) {
                        if (ignoreProperty.equals(name)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        continue;
                    }
                }

                //获取get和set方法的名字
                String firstLetter = name.substring(0, 1).toUpperCase();    //将属性的首字母转换为大写
                String getMethodName = "get" + firstLetter + name.substring(1);
                String setMethodName = "set" + firstLetter + name.substring(1);
                //获取方法对象
                Method getMethod = classType.getMethod(getMethodName);
                Method setMethod = classType.getMethod(setMethodName, field.getType());//注意set方法需要传入参数类型

                //调用get方法获取旧的对象的值
                Object value = getMethod.invoke(source);

                //调用set方法将这个值复制到新的对象中去
                setMethod.invoke(target, value);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            UniversalException.logError(e);
        }

    }
    public static <T> void copyIgnoreNull(T source, T target) {
        copy(source, target, getNullPropertyNames(source));
    }

    public static <T> T copy(T source) {
        try {
            Class<?> classType = source.getClass();
            //用第二个带参数的构造方法生成对象
            /**
             *     Constructor cons2 = classType.getConstructor(new Class[] {String.class, int.class});
             *         Object obj2 = cons2.newInstance(new Object[] {"ZhangSan",20});
             */
            Object objectCopy = source.getClass().getConstructor(new Class[]{}).newInstance(new Object[]{});
            //获得对象的所有成员变量
            Field[] fields = classType.getDeclaredFields();
            for (Field field : fields) {
                //获取成员变量的名字
                String name = field.getName();    //获取成员变量的名字，此处为id，name,age
                if ("serialVersionUID".equals(name)) {
                    continue;
                }
                //获取get和set方法的名字
                String firstLetter = name.substring(0, 1).toUpperCase();    //将属性的首字母转换为大写
                String getMethodName = "get" + firstLetter + name.substring(1);
                String setMethodName = "set" + firstLetter + name.substring(1);
                //System.out.println(getMethodName + "," + setMethodName);

                //获取方法对象
                Method getMethod = classType.getMethod(getMethodName);
                Method setMethod = classType.getMethod(setMethodName, field.getType());//注意set方法需要传入参数类型
                //调用get方法获取旧的对象的值
                Object value = getMethod.invoke(source, new Object[]{});
                //调用set方法将这个值复制到新的对象中去
                setMethod.invoke(objectCopy, new Object[]{value});
            }
            return (T) objectCopy;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            UniversalException.logError(e);
        }

        return null;
    }

}
