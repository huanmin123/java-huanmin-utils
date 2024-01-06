package com.huanmin.test.utils_common.base;

import com.utils.common.base.DateUtil;
import com.utils.common.enums.DateEnum;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class DateUtilsTest {
    @Test
    public void show(){
        System.out.println(  DateUtil.dateStrng());
    }
    @Test
    public void show1(){
        System.out.println( "show1"+ DateUtil.date());
    }
    @Test
    public void show2(){
        System.out.println( "show2"+ DateUtil.dateStrEq("2020-6-6 18:40:5"));
    }
    @Test
    public void show3(){
        System.out.println(  "show3"+DateUtil.dateStrEq("2020-6-5 00:00:00","2020-6-6 18:40:5"));
    }

    @Test
    public void show4(){
        System.out.println(  "show4"+DateUtil.dateEq(System.currentTimeMillis(),1606753636253L )) ;
    }

    @Test
    public void show5(){
        Date date = DateUtil.millisecondTurnDate(1606753636253L);
        System.out.println( "show5"+ date  ) ;
    }


    @Test
    public void show6(){
        String dateStr1 = "2019-01-19 6:6:5";
        String dateStr2 = "2019-01-19 6:5:5";
        System.out.println( DateUtil.getDateStrPoor(dateStr1,dateStr2));
    }

    @Test
    public void show7(){
        long test = DateUtil.strToDateLong("20220922175000", DateEnum.UNSIGNED_DATETIME_PATTERN);
        System.out.println("=====:"+test);


    }

}
