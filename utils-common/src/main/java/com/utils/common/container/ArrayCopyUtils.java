package com.utils.common.container;

import com.utils.common.base.UniversalException;

import java.lang.reflect.Array;

/**
 * 数组复制
 */
public class ArrayCopyUtils {

    /**
     *  将原数组复制到目标数组里, 前提条件是必须目标数组等于或者大于原数组的长度
     * @param src  原数组
     * @param dest 目标数组
     * @return
     * 
     */
    public static   boolean copyAll(Object src,Object dest)  {
        int end = Array.getLength(src);
        return copy(src,dest, 0,end,0);
    }

    /**
     *  将原数组复制到目标数组里, 前提条件是必须目标数组等于或者大于原数组的长度
     * @param src  原数组
     * @param dest 目标数组
     * @param start 拷贝src,起始位置
     */
    public static   boolean copyStart(Object src,Object dest,int start)  {
        return copy(src,dest, start,Array.getLength(src),0);

    }

    /**
     * 复制全部的src到dest里, 并且可以调整每次复制到dest的位置(从什么位置开始复制)
     * @param src  原数组
     * @param dest 目标数组
     * @param targetStart 从什么位置开始复制
     * @return
     */
    public static   boolean copyTargetStart(Object src,Object dest,int targetStart)  {
        return copy(src,dest, 0,Array.getLength(src),targetStart);

    }

    /**
     *  将原数组复制到目标数组里, 前提条件是必须目标数组等于或者大于原数组的长度
     * @param src  原数组
     * @param dest 目标数组
     * @param end 拷贝src,结束位置
     * @return
     * 
     */
    public static   boolean copEnd(Object src,Object dest,int end)  {
        return copy(src,dest, 0,end,0);
    }

    /**
     *  将原数组复制到目标数组里, 前提条件是必须目标数组等于或者大于原数组的长度
     * @param src  原数组
     * @param dest 目标数组
     * @param start  拷贝src起始位置   ,从0开始
     * @param end    拷贝src的结束位置 ,从0开始
     * @return
     * 
     */
    public static   boolean copy(Object src,Object dest,int start,int end)  {

        return copy(src,dest, start,end,0);
    }

    /**
     * 将原数据的内容copy到新数组汇总
     * @param src 原数组
     * @param dest 目标数组
     * @param start  拷贝src起始位置   ,从0开始
     * @param end    拷贝src的结束位置 ,从0开始
     * @param targetStart   dest目标数组起始位置 ,从0开始
     */
    public static   boolean copy(Object src,Object dest,int start,int end,int targetStart)  {
        try {
            if (src.getClass().isArray()&&dest.getClass().isArray()){
                int length= Array.getLength(src);//目标数组的长
                int length1 = Array.getLength(dest);//目标数组的长度
                int copyCount = end - start; //需要拷贝的数量

                if(copyCount<=0){
                    throw new Exception("复制src的数量不能是0请检查起始或者结束位置是否有问题");
                }

                if (copyCount<=length1){
                    //溢出处理
                    int gauging = start + copyCount;
                    if(gauging>length){
                        //调整复制的数量,不够了,那么只复制剩下的
                        copyCount=length-start;
                    }

                    System.arraycopy(src,start,dest,targetStart, copyCount);
                }else{
                    throw new Exception("copy的目标数组的长度不符合,必须大于或者等于原数组的长度,或者调整copy的个数");
                }
            }else{
                throw new Exception("参数不是数组");
            }
        } catch (Exception e) {
             UniversalException.logError(e);
            return  false;
        }
        return  true;
    }


}
