package com.huanmin.utils.common.base;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据分组,计算下标
 */
public class DataGroup {
    private static final Logger logger = LoggerFactory.getLogger(DataGroup.class);
    /**
     *
     * @param sum  总数
     * @param num 一次分组每段分配多少       long[0] 起始位置 long[1] 结束位置   下标从0开始
     * @return
     */
    public static  List<long[]>  dataGroupOnce(long sum,long num) {
        return dataGroupt(sum,num,false,0).get("segmentation");
    }
    
    /**
     * 获取值所在的区间,利用二分法
     * @param list 分组后的集合
     * @param v 值
     * @return index(int)  section(long[])
     */
    public static Integer getVSection(List<long[]> list,long v){

        int low = 0;
        int high = list.size() - 1;
        while (low <= high) {
            int middle = (low + high) / 2;
            if (v >= list.get(middle)[0] && v < list.get(middle)[1]) {
                return middle;
            }
            if (v < list.get(middle)[0]) {
                high = middle - 1;
            }
            if (v >= list.get(middle)[1]) {
                low = middle + 1;
            }
        }
        return null;
    }


    /**
     *
     * @param sum  总数
     * @param num 一次分组每段分配多少
     * @param num1 二次分组每段分配的个数(就是根据第一次分组后的总长度再次分组)
     *  意思就是segmentation是数据的总分割,segmentationGoup是segmentation的分割
     * @return    segmentation   segmentationGoup
     */
    public static   Map<String,List<long[]>>   dataGrouptWice(long sum,long num,long num1) {
        return dataGroupt(sum,num,true,num1);
    }

    /**
     * 分段器 (将数据分组(多线程) ,一般线程使用,还可以开启二次分组,就是对分组后的再次分组(多进程+多线程))
     * @param sum  总数
     * @param num 一次分组每段分配多少
     * @param group 二次分组
     * @param num1  二次分组每段分配的个数

     * @return
     */

    private static  Map<String,List<long[]>>  dataGroupt(long sum,long num,boolean group,long num1) {
        Map<String,List<long[]>> map=new HashMap<>();
        List<long[]> list=new ArrayList<long[]>();
        if(sum<=0){
            System.out.println("总数不能是空或者小于0");
            return  map;
        }
        if (sum<=num) {
            list.add(new long[]{0,sum});
            map.put("segmentation",list) ;
            return map ;
        }
        long result = (long) Math.ceil((double) sum / num) ;


        long retain = 0;  // 保留值

        for (long i = 0; i < result; i++) {
            if (retain == 0) {
                list.add(new long[]{0,i + num});
                retain = i + num;
            } else {
                long c = retain + num;
                if (c < sum) {
                    list.add(new long[]{retain, c});
                    retain = c;
                } else {
                    list.add(new long[]{retain, ((sum - retain) + retain)} );
                }
            }

        }
        map.put("segmentation",list) ;
        if (group){
            Map<String, List<long[]>> segmentation = dataGroupt(map.get("segmentation").size(), num1, false, 0);
            map.put("segmentationGoup",segmentation.get("segmentation"));
        }
        return map;
    }



}
