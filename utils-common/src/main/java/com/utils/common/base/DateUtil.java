package com.utils.common.base;


import com.utils.common.enums.DateEnum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {

    //日期转换成字符串
    public static String dateTurnString(Date date, DateEnum patt) {
        SimpleDateFormat sdf = new SimpleDateFormat(patt.getValue());
        String format = sdf.format(date);
        return format;
    }

    //世界时间转北京时间(需要+8小时)
    public static String UTCToCST(String UTCStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DateEnum.DATETIME_T_PATTERN.getValue());
        Date date = sdf.parse(UTCStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
        Date time = calendar.getTime();
        SimpleDateFormat sdf1 = new SimpleDateFormat(DateEnum.DATETIME_PATTERN.getValue());
        return sdf1.format(time);
    }


    //字符串转换成日期
    public static Date stringTurnDate(String str, String patt) {
        SimpleDateFormat sdf = new SimpleDateFormat(patt);
        Date parse = null;
        try {
            parse = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    //字符串转换成时间戳
    public static long stringTurnLong(String str, String patt) {
        SimpleDateFormat sdf = new SimpleDateFormat(patt);
        try {
            return sdf.parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //获取当前时间位的毫秒数
    public static int getNowTimeMillis() {
        long l = System.currentTimeMillis();
        //截取后三位
        String s = String.valueOf(l);
        String substring = s.substring(0, s.length() - 3);
        return Integer.parseInt(substring);
    }



    //获取指定时间的毫秒数
    public static int getDateTimeMillis(Date date) {
        long l = date.getTime();
        //截取后三位
        String s = String.valueOf(l);
        String substring = s.substring(s.length() - 3);
        return Integer.parseInt(substring);
    }
    //获取当前时间戳秒级别
    public static long getNowTimeSecond() {
        return System.currentTimeMillis() / 1000;
    }
    //毫秒时间戳转换成秒时间戳
    public static long millisTurnSecond(long millis) {
        //判断是否是13位
        String s = String.valueOf(millis);
        if (s.length() == 13) {
            return millis / 1000;
        }
        return millis;
    }

    //秒时间戳转换成日期
    public static Date secondTurnDate(long second) {
        //判断是否是10位
        if (String.valueOf(second).length() == 10) {
            second = second * 1000;
        }
        return new Date(second);
    }


    //当前日期   字符串形式  ?-?-?  : :
    public static String dateStrng() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat(DateEnum.DATETIME_PATTERN.getValue());
        return ft.format(dNow);
    }


    //将格式字符串日期时间转换为毫秒时间戳
    public static long strToDateLong(String dateStr, DateEnum DateEnum) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateEnum.getValue());
        try {
            return sdf.parse(dateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    //当前日期  返回Date对象
    public static Date date() {
        return new Date();
    }


    //比较 两个时间戳(毫秒) 大小   左边 比较右边
    //  1 大于  0小于  -1 相等
    public static int dateEq(Long data1, Long data2) {

        boolean pd = data1 > data2; //将本地时间 和 其他的时间进行比较
        if (pd) { //大于
            return 1;
        } else {
            boolean pd1 = data1.equals(data2); //将本地时间 和 其他的时间进行比较
            if (pd1) {
                //相等
                return -1;
            }
            return 0;//小于
        }
    }

    public static int dateEq(String data1, String data2, DateEnum DateEnum) {
        long da1 = stringTurnLong(data1, DateEnum.getValue());
        long da2 = stringTurnLong(data2, DateEnum.getValue());
        boolean pd = da1 > da2; //将本地时间 和 其他的时间进行比较
        if (pd) {
            //data1时间 大
            return 1;
        } else {
            boolean pd1 = da1 == da2; //将本地时间 和 其他的时间进行比较
            if (pd1) {
                //相等
                return -1;
            }
            //data2 小
            return 0;
        }
    }


    //毫秒转Date
    public static Date millisecondTurnDate(Long milliSecond) {
        Date date = new Date();
        date.setTime(milliSecond);
        return date;
    }


    //当前时间和 指定时间  比较   传入字符串格式的时间 比如  2020-6-6 18:40:5
    //1 大于  0小于
    public static int dateStrEq(String strdate) {
        SimpleDateFormat df = new SimpleDateFormat(DateEnum.DATETIME_PATTERN.getValue());//设置日期格式
        df.setLenient(false);//false严格处理 是否符合 现实中的日期格式  比如 这个月没有31号 但是不开启的情况下 不会报错的
        Date time = null;// 如果格式不对 就会报错
        try {
            time = df.parse(strdate);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        Date data = new Date(); //获取本地时间
        boolean pd = data.after(time); //将本地时间 和 其他的时间进行比较
        if (pd) {
            System.out.println("本地时间 大 ");
            return 1;
        } else {
            System.out.println("本地时间 小 ");
            return 0;
        }
    }

    // 两个指定时间 相比较   左边和右边对比   左边小 0  左边大 1   时间格式 2020-6-5 00:00:00
    public static int dateStrEq(String strDate1, String strDate2) {
        SimpleDateFormat df = new SimpleDateFormat(DateEnum.DATETIME_PATTERN.getValue());//设置日期格式
        df.setLenient(false);//false严格处理 是否符合 现实中的日期格式  比如 这个月没有31号 但是不开启的情况下 不会报错的
        Date time1 = null;// 如果格式不对 就会报错
        Date time2 = null;// 如果格式不对 就会报错
        try {
            time1 = df.parse(strDate1);
            time2 = df.parse(strDate2);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

        boolean pd = time1.after(time2); //将strDate1时间 和 strDate2时间进行比较
        if (pd) {
            System.out.println("左 大 ");
            return 1;
        } else {
            System.out.println("左 小 ");
            return 0;
        }

    }

    //   endDate 减  nowDate
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    //   endDate 减  nowDate
    public static String getDateStrPoor(String endDate, String nowDate) {
        SimpleDateFormat format = new SimpleDateFormat(DateEnum.DATETIME_PATTERN.getValue());
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(endDate);
            date2 = format.parse(nowDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = date1.getTime() - date2.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    //指定时间加上年月日时分秒后的时间(不加的话就是0)
    public static Date addDate(Date date, int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, year);
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DAY_OF_MONTH, day);
        cal.add(Calendar.HOUR_OF_DAY, hour);
        cal.add(Calendar.MINUTE, minute);
        cal.add(Calendar.SECOND, second);
        return cal.getTime();
    }

    //指定时间添加秒
    public static long add_second(long timestamp, int second) {
        return timestamp + second;
    }
    //指定时间添加分钟
    public static long add_minute(long timestamp, int minute) {
        return timestamp + minute * 60L;
    }

    //指定时间添加小时
    public static long add_hour(long timestamp, int hour) {
        return timestamp + hour * 3600L;
    }

    //指定时间添加天
    public static long add_day(long timestamp, int day) {
        return timestamp + day * 86400L;
    }

    //指定时间添加月
    public static long add_month(long timestamp, int month) {
        return timestamp + month * 2592000L;
    }

    //指定时间添加年
    public static long add_year(long timestamp, int year) {
        return timestamp + year * 31104000L;
    }

    //指定时间添加星期
    public static long add_week(long timestamp, int week) {
        return timestamp + week * 604800L;
    }

    //指定时间减去秒
    public static long sub_second(long timestamp, int second) {
        return timestamp - second;
    }

    //指定时间减去分钟
    public static long sub_minute(long timestamp, int minute) {
        return timestamp - minute * 60L;
    }

    //指定时间减去小时
    public static long sub_hour(long timestamp, int hour) {
        return timestamp - hour * 3600L;
    }

    //指定时间减去天
    public static long sub_day(long timestamp, int day) {
        return timestamp - day * 86400L;
    }

    //指定时间减去月
    public static long sub_month(long timestamp, int month) {
        return timestamp - month * 2592000L;
    }

    //指定时间减去年
    public static long sub_year(long timestamp, int year) {
        return timestamp - year * 31104000L;
    }

    //指定时间减去星期
    public static long sub_week(long timestamp, int week) {
        return timestamp - week * 604800L;
    }
    //指定时间添加秒,分,时,天,月,年,星期, 24小时   timestamp  时间戳  second 秒
  public static long  addDateTimestamp(long timestamp, int second, int minute, int hour, int day, int month, int year, int week) {
        return timestamp + second + minute * 60L + hour * 3600L + day * 86400L + month * 2592000L + year * 31104000L +
                week * 604800L;
    }



    //获取指定时间下个星期n的日期时间戳
    public static long getDateWeek(Date date, int week) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        cal.set(Calendar.DAY_OF_WEEK, getWeekInt(week));
        return cal.getTimeInMillis();
    }
    //星期映射表
    public static String getWeek(int week) {
        String weekStr = "";
        switch (week) {
            case 1:
                weekStr = "星期日"; //星期日
                break;
            case 2:
                weekStr = "星期一";
                break;
            case 3:
                weekStr = "星期二";
                break;
            case 4:
                weekStr = "星期三";
                break;
            case 5:
                weekStr = "星期四";
                break;
            case 6:
                weekStr = "星期五";
                break;
            case 7:
                weekStr = "星期六";
                break;
        }
        return weekStr;
    }
    public static Integer getWeekInt(int week) {
        Integer weekStr = null;
        switch (week) {
            case 1:
                weekStr = 2;
                break;
            case 2:
                weekStr = 3;
                break;
            case 3:
                weekStr = 4;
                break;
            case 4:
                weekStr = 5;
                break;
            case 5:
                weekStr = 6;
                break;
            case 6:
                weekStr = 7;
                break;
            case 7:
                weekStr = 1;
                break;
        }
        return weekStr;

    }
    //获取指定时间下秒的开始时间的日期时间戳
    public static long getDateNextSecond(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, 1);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    //获取指定时间下一分钟的开始时间的日期时间戳
    public static long getDateNextMinute(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    //获取指定时间下一小时的开始时间的日期时间戳
    public static long getDateNextHour(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    //获取指定时间下一天的开始时间的日期时间戳
    public static long getDateNextDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    //获取指定时间下一周的开始时间的日期时间戳,周一为一周的开始
    public static long getDateNextWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    //获取指定时间下一月的开始时间的日期时间戳
    public static long getDateNextMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    //获取指定时间下一年的开始时间的日期时间戳
    public static long getDateNextYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, 1);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    //获取指定时间的开始时间的日期时间戳 (也就是指定时间的00:00:00)
    public static long getDateStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    //获取指定时间的结束日期时间戳 (也就是指定时间的23:59:59)
    public static long getDateEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }

    //获取指定时间是本周的第几天 1~7
    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int i = cal.get(Calendar.DAY_OF_WEEK);
        return  getWeekInt(i);
    }

    //获取指定时间下周的第几天 1~7
    public static long getDateNextWeek(Date date, int week) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        week = week + 1;
        if (week == 8) {
            week = 1;
        }
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        cal.set(Calendar.DAY_OF_WEEK, week);
        return cal.getTimeInMillis();
    }

    //指定时间下周初的时间戳
    public static long getDateNextWeekStart(Date date) {
        long dateNextWeek = getDateNextWeek(date, 1);
        return getDateStart(new Date(dateNextWeek));
    }

    //获取指定时间的下分钟结束时间的日期时间戳
    public static long getDateNextMinuteEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 59);
        return cal.getTimeInMillis();
    }

    // 获取指定时间的下小时结束时间的日期时间戳结束时间
    public static long getDateNextHourEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTimeInMillis();
    }

    // 获取指定时间的下天结束时间的日期时间戳
    public static long getDateNextDayEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTimeInMillis();
    }

    // 获取指定时间的下周结束时间的日期时间戳
    public static long getDateNextWeekEnd(Date date) {
        long dateNextWeek = getDateNextWeek(date, 7);
        return getDateEnd(new Date(dateNextWeek));
    }

    // 获取指定时间的下月结束时间的日期时间戳 ,也就是下月的最后一天的23:59:59
    public static long getDateNextMonthEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 2);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTimeInMillis();
    }

    // 获取指定时间的下年结束时间的日期时间戳  ,也就是下年的最后一天的23:59:59
    public static long getDateNextYearEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, 2);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTimeInMillis();
    }

    //计算两个时间戳的时间差(返回秒)
    public static long getTimeDifferenceSecond(long start_time, long end_time) {
        if (start_time == 0 || end_time == 0) {
            return 0;
        }
        if (start_time > end_time) {
            return start_time - end_time;
        } else {
            return end_time - start_time;
        }
    }
    
    
    // 修改字符串时间格式
    public static String changeTimeFormat(String time, DateEnum oldFormat, DateEnum newFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(oldFormat.getValue());
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(newFormat.getValue());
        return sdf1.format(date);
    }
    //字符串时间+8小时返回字符串
    

    public static void main(String[] args) {
        long dateWeek = DateUtil.getDateWeek(new Date(),2);
        Date date = new Date(dateWeek);
        String s = DateUtil.dateTurnString(date, DateEnum.DATETIME_PATTERN);
        System.out.println(s);

    }



}
