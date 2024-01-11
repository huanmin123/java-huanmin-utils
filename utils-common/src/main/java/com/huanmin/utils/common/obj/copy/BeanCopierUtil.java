package com.huanmin.utils.common.obj.copy;

import org.springframework.cglib.beans.BeanCopier;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用Cglib的BeanCopier完成bean对象拷贝
 * 条件	                        结果
 * 属性名相同，并且属性类型相同	    ok
 * 属性名相同，并且属性类型不相同   	no
 * target的setter不规范	        抛异常
 * 建议大量copy时候使用 100000+
 */
public class BeanCopierUtil {

    /**
     * BeanCopier的缓存   , 实体类必须实现 equals和hashCode方法  ,可以使用 lombok的@Data
     */
    static final ConcurrentHashMap<String, BeanCopier> BEAN_COPIER_CACHE = new ConcurrentHashMap<>();

    /**
     * BeanCopier的copy   - 实体类必须实现setXx ,源文件属性的名称和目标文件属性名称必须一致  ,属性个数可以不用一致会自动适配存在的就拷贝
     * @param source 源文件的
     * @param target 目标文件
     * 因为是引用类型所以直接作用于目标对象所以无需返回值
     */
    public static void copy(Object source, Object target, String... ignoreProperties) {
        String key = genKey(source.getClass(), target.getClass());
        BeanCopier beanCopier;
        if (BEAN_COPIER_CACHE.containsKey(key)) {
            beanCopier = BEAN_COPIER_CACHE.get(key);
        } else {
            beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
            BEAN_COPIER_CACHE.put(key, beanCopier);
        }
        beanCopier.copy(source, target, null);

    }



    /**
     * 生成key
     * @param srcClazz 源文件的class
     * @param tgtClazz 目标文件的class
     * @return string
     */
    private static String genKey(Class<?> srcClazz, Class<?> tgtClazz) {
        return srcClazz.getName() + tgtClazz.getName();
    }


    /**
     *
     * @param source 原数据源
     * @param target 目标对象数据类型
     * @return  返回目标类型对象
     */
    public static <T,D> Object copyObj(D source, Class<T> target){

        T newInstance = null;
        try {
            newInstance = target.newInstance();
        } catch (InstantiationException | IllegalAccessException instantiationException) {
            instantiationException.printStackTrace();
        }
        assert newInstance != null;
        copy(source,newInstance);
        return  newInstance;

    }


    /**
     *
     * @param sources list数据源
     * @param target 目标对象数据类型
     * @return 返回目标类型对象
     */
    public static <T,D> List<T> copylist(List<D> sources,Class<T> target){
        List<T> es = new ArrayList<>();
        for (D e : sources) {
            es.add((T)copyObj(e,target));
        }
        return es;
    }


    /**
     *
     * @param sources Map数据源
     * @param target 目标对象数据类型  只作用于Value
     * @return 返回目标类型对象
     */
    public static <T,S,V> Map<S,T> copyMap(Map<S,V> sources, Class<T> target){
        Map<S,T> es = new HashMap<S,T>();
        for (Map.Entry<S, V> dvEntry : sources.entrySet()) {
            es.put(dvEntry.getKey(),(T)copyObj(dvEntry.getValue(),target));
        }

        return es;
    }



    /**
     *
     * @param sources 数组数据源
     * @param target 目标对象数据类型
     * @return 返回目标类型对象
     */
    public static <T,D>  T[] copyArray(D[] sources,Class<T> target){
        T[] ts = (T[]) Array.newInstance(target, sources.length);
        for (int i = 0; i < sources.length; i++) {
            ts[i]= (T)copyObj(sources[i],target);
        }
        return ts;
    }



}
