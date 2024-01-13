package com.huanmin.test.utils.utils_common.base;


import com.huanmin.utils.common.base.CodeTimeUtil;
import com.huanmin.utils.common.base.DataGroup;
import com.huanmin.utils.common.base.FakerData;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DataGroupTest {
    @Test
    public void dataGroupOnce() throws Exception {
        //执行时间
        CodeTimeUtil.creator(()->{
            long[] datalong = FakerData.getDatalong(10000);
            List<long[]> list = DataGroup.dataGroupOnce(datalong.length, 100);
            System.out.println("segmentation:len:"+list.size());
            for (long[] longs : list) {
                System.out.println(longs[0] + "  " + longs[1]);
            }
        });
    }

    @Test
    public void dataGrouptWice() throws Exception {
        //执行时间
        CodeTimeUtil.creator(()->{
            long[] datalong = FakerData.getDatalong(10000);
            Map<String, List<long[]>> map = DataGroup.dataGrouptWice(datalong.length, 100, 10);
            List<long[]> segmentation = map.get("segmentation"); //一次分组
            System.out.println("segmentation:len:"+segmentation.size());
            segmentation.forEach(longs -> {
                System.out.println(longs[0] + "  " + longs[1]);
            });
            System.out.println("==================================");
            List<long[]> segmentationGoup1 = map.get("segmentationGoup");//基于第一次分组长度进行二次分组
            for (long[] longs : segmentationGoup1) {
    
                System.out.println(longs[0] + "  " + longs[1]);
            }
          
        });
    }
    @Test
    public  void getVSection(){
        Long v = 1000L;
        long[] datalong = FakerData.getDatalong(10000);
        List<long[]> longs = DataGroup.dataGroupOnce(datalong.length, 100);
        Integer vSection = DataGroup.getVSection(longs, v);
        System.out.println("index:"+vSection);


        long[] longs1 = longs.get(vSection);

        System.out.println(longs1[0] + "  " + longs1[1]);
    }
}
