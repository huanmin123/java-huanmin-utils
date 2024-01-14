package org.huanmin.utils.timerwheel;

import com.alibaba.fastjson.JSONArray;
import org.huanmin.utils.common.base.AssertUtil;
import org.huanmin.utils.common.base.DateUtil;
import org.huanmin.utils.common.enums.DateEnum;
import org.huanmin.utils.common.json.JsonFastJsonUtil;
import org.huanmin.utils.common.string.StringUtil;
import org.huanmin.utils.timerwheel.mode.TimeBeginMode;
import org.huanmin.utils.timerwheel.mode.TimeEndMode;
import org.huanmin.utils.timerwheel.mode.TimePointMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/11/30 17:07
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 * 语法格式
 * 语法: 0,0,7,0,0,0,0,- 长度为8
 * 对应时间: 秒,分,时,天,星期,月,年,执行计划
 * 执行计划
 * 数值: 执行几次
 * n: 循环执行
 * ^n: 时间乘阶 2*2,4*2,8*2....
 * ^数值: 时间乘阶几次 ^2 那么就是2*2,4*2
 * 符号
 * ~: 区间 1~4 那么就会执行1,2,3,4
 * /: 间隔 2/5 那么从2开始下次之后就从5开始执行
 * []: 分段 [1,5,10,12] 分别在这几个时间点执行
 * 模式
 * 模式就是可以: 月底,月初,每周几,星期末,年末,节假日等,(可以支持自定义扩张需要对外提供生成器)
 * 执行计划: m(循环执行) ,m数值(有限执行)
 * begin: 时间底(天底,周末,月底,年底)
 * end: 时间初(天初,周初,月初,年初)
 * point(?): 指定时间点,指定每月5号,每周3,每2天
 * 以上是基础模式,其他模式需要自定义(节假日,特殊日期等)
 * 注意: 在模式的基础上还可以配合时间比如: (每天2点1分10秒执行)
 * 案例可以参考: c语言-自研定时器计划任务语法   csdn
 */
public class TimerResolverCore {
    public String second;//秒 0-59
    public String minute;//分钟 0-59
    public String hour; //小时0-23
    public String day;  //天 1-31
    public String week;//星期 0-6
    public String month;//月份 1-12
    public String year;//年
    public String plan;//执行计划 -代表不执行,n 循环执行,数字代表执行次数 , ^n 阶级执行(^1 表示一阶执行,^2 表示二阶执行,^3 表示三阶执行...^n表示n阶执行)  mode 代表执行模式 mode2 代表模式执行2次....
    public long timestamp;//时间戳 用于计算 秒级别
    // 下次执行时间
    public String help;//协助计算的字段
    public Map<String, Object> mode;//模式




    boolean mode_exist(String item, TimerResolverCore timerResolver) {
        if (!timerResolver.plan.contains("mode")) {
            return false;
        }
        Set<Map.Entry<String, Object>> entries = timerResolver.mode.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            if (item.startsWith(entry.getKey())) {//如果匹配到模式,那么就转换为统一结构化格式
                return true;
            }
        }
        return false;
    }


    //模式解析
    long mode_timer(String item, int seat, TimerResolverCore timerResolver) {
        if (timerResolver.plan.contains("mode")) {
            Set<Map.Entry<String, Object>> entries = timerResolver.mode.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                if (item.startsWith(entry.getKey())) {//如果匹配到模式
                    TimerResolverModeMethod pVoid = (TimerResolverModeMethod) entry.getValue();
                    return pVoid.modeMethod(item, seat, timerResolver);//执行模式方法
                }
            }
        }
        return 0;// 没有匹配到模式
    }

    //添加模式方法(如果执行计划为mode,则支持)
    void add_timer_mode(TimerResolverCore timerResolver, String key, TimerResolverModeMethod method) {
        if (timerResolver.plan.contains("mode")) {
            timerResolver.mode.put(key, method);
        }
    }

    void inside_add_timer_mode(TimerResolverCore timerResolver) {
        if (timerResolver.plan.contains("mode")&&timerResolver.mode.isEmpty()) {
            add_timer_mode(timerResolver, "begin", new TimeBeginMode());
            add_timer_mode(timerResolver, "end", new TimeEndMode());
            add_timer_mode(timerResolver, "point", new TimePointMode());
        }
    }

    //获取下一次执行时间,返回时间戳
    long get_next_time(TimerResolverCore timerResolver) {
        if (timerResolver.plan == null) {
            return 0;
        }

        if (timerResolver.plan.contains("mode")) {
            long mode_timer1 = mode_timer(get_return_data(timerResolver, "second"), 0, timerResolver);
            timerResolver.timestamp += DateUtil.getTimeDifferenceSecond(timerResolver.timestamp, mode_timer1);
            long mode_timer2 = mode_timer(get_return_data(timerResolver, "minute"), 1, timerResolver);
            timerResolver.timestamp += DateUtil.getTimeDifferenceSecond(timerResolver.timestamp, mode_timer2);
            long mode_timer3 = mode_timer(get_return_data(timerResolver, "hour"), 2, timerResolver);
            timerResolver.timestamp += DateUtil.getTimeDifferenceSecond(timerResolver.timestamp, mode_timer3);
            long mode_timer4 = mode_timer(get_return_data(timerResolver, "day"), 3, timerResolver);
            timerResolver.timestamp += DateUtil.getTimeDifferenceSecond(timerResolver.timestamp, mode_timer4);
            long mode_timer5 = mode_timer(get_return_data(timerResolver, "week"), 4, timerResolver);
            timerResolver.timestamp += DateUtil.getTimeDifferenceSecond(timerResolver.timestamp, mode_timer5);
            long mode_timer6 = mode_timer(get_return_data(timerResolver, "month"), 5, timerResolver);
            timerResolver.timestamp += DateUtil.getTimeDifferenceSecond(timerResolver.timestamp, mode_timer6);
            long mode_timer7 = mode_timer(get_return_data(timerResolver, "year"), 6, timerResolver);
            timerResolver.timestamp += DateUtil.getTimeDifferenceSecond(timerResolver.timestamp, mode_timer7);
            timerResolver.timestamp= DateUtil.millisTurnSecond(timerResolver.timestamp);//毫秒时间戳转换为秒时间戳
        }
        long current = timerResolver.timestamp;
        //获取当前时间
        int second = StringUtil.str_to_int(get_return_data(timerResolver, "second"));
        int minute =StringUtil.str_to_int(get_return_data(timerResolver, "minute"));
        int hour = StringUtil.str_to_int(get_return_data(timerResolver, "hour"));
        int day = StringUtil.str_to_int(get_return_data(timerResolver, "day"));
        int week =StringUtil.str_to_int(get_return_data(timerResolver, "week"));
        int month =StringUtil.str_to_int(get_return_data(timerResolver, "month"));
        int year =StringUtil.str_to_int(get_return_data(timerResolver, "year"));
        long newTime = DateUtil.addDateTimestamp(current,
                second,
                minute,
                hour,
                day,
                month,
                year,
                week
        );
        timerResolver.timestamp = newTime;
        if (timerResolver.plan.contains("mode")) {//如果执行计划为mode,则支持
            return newTime;
        } else {
            return current;
        }
    }


    //解析字符串-,-,-,-,-,-,-,-并创建TimerResolver
    public TimerResolverCore create_timer_resolver(String time) throws Exception {

        if (time == null || time.length() == 0) {
            return null;
        }
        //去空
        time = time.trim();
        //排除[]内的,号
        int start = 0;
        while ((start = StringUtil.str_find_n(time, "[", start, time.length())) != -1) {
            int end = StringUtil.str_find_n(time, "]", start, time.length());
            time = StringUtil.str_replace_all_n(time, ",", "@", start, end);
            start++;
        }

        TimerResolverCore timerResolver = new TimerResolverCore();

        String[] pCharlist = time.split(",");
        int str_len = pCharlist.length;
        if (str_len != 8) {
            System.out.println("字符串格式错误,长度不为8,长度为:" + str_len);
            throw new Exception("create_timer_resolver语法错误: " + time);
        }
        timerResolver.second = pCharlist[0];
        timerResolver.minute = pCharlist[1];
        timerResolver.hour = pCharlist[2];
        timerResolver.day = pCharlist[3];
        timerResolver.week = pCharlist[4];
        timerResolver.month = pCharlist[5];
        timerResolver.year = pCharlist[6];
        timerResolver.timestamp = DateUtil.getNowTimeSecond();//获取当前时间戳
        //执行计划 -代表不执行,n 循环执行,数字代表执行次数 , ^n 阶级执行(^1 表示一阶执行,^2 表示二阶执行,^3 表示三阶执行...^n表示n阶执行)
        timerResolver.plan = "-".equals(pCharlist[7]) ? "0" : pCharlist[7];
        timerResolver.help = "{}";
        timerResolver.mode = new HashMap<>(20);
        //添加模式方法
        inside_add_timer_mode(timerResolver);
        //转换为结构化格式
        switchStructure(timerResolver);

        //计算本次执行时间
        if (!timerResolver.plan.contains("mode")) {
            get_next_time(timerResolver);
        }

        return timerResolver;
    }

    //打印TimerResolver,格式为-,-,-,-,-,-,-,-
    public void print_timer_resolver(TimerResolverCore timerResolver) {
        String format = String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", timerResolver.second, timerResolver.minute, timerResolver.hour,
                timerResolver.day, timerResolver.week, timerResolver.month, timerResolver.year,
                timerResolver.plan);
        System.out.println(format);

    }

    //执行n次定时器并且打印效果
    public void print_format_resolver(TimerResolverCore timerResolver, int n) throws Exception {
        for (int i = 0; i < n; ++i) {
            long i1 = resolver(timerResolver);
            String string = DateUtil.dateTurnString(DateUtil.secondTurnDate(i1), DateEnum.DATETIME_PATTERN);
            System.out.println("预计执行时间: " + i1 + "(时间戳)---" + string + "(格式化时间)\n");
        }
    }

    //解析定时器语法,并且生成下次执行时间,如果返回0,则此语法已经执行结束了,可以删除对应的定时器了
    public long resolver(TimerResolverCore timerResolver) throws Exception {
        //判断是否可执行
        if ("0".equals(timerResolver.plan)) {//不执行
            return 0;
        }

        if ("n".equals(timerResolver.plan)) {//循环执行
            return get_next_time(timerResolver);
        }
        //阶级执行
        if (timerResolver.plan != null && timerResolver.plan.startsWith("^")) {
            Map<String, Object> pMapSecond = JsonFastJsonUtil.toMap(timerResolver.second);
            pMapSecond.put("return", String.valueOf(Integer.parseInt(get_return_data(timerResolver, "second")) * 2));
            timerResolver.second = JsonFastJsonUtil.toFeaturesJson(pMapSecond);


            Map<String, Object> pMapMinute = JsonFastJsonUtil.toMap(timerResolver.minute);
            pMapMinute.put("return", String.valueOf(Integer.parseInt(get_return_data(timerResolver, "minute")) * 2));
            timerResolver.minute = JsonFastJsonUtil.toFeaturesJson(pMapMinute);

            Map<String, Object> pMapHour = JsonFastJsonUtil.toMap(timerResolver.hour);
            pMapMinute.put("return", String.valueOf(Integer.parseInt(get_return_data(timerResolver, "hour")) * 2));
            timerResolver.hour = JsonFastJsonUtil.toFeaturesJson(pMapHour);


            Map<String, Object> pMapDay = JsonFastJsonUtil.toMap(timerResolver.day);
            pMapMinute.put("return", String.valueOf(Integer.parseInt(get_return_data(timerResolver, "day")) * 2));
            timerResolver.day = JsonFastJsonUtil.toFeaturesJson(pMapDay);


            Map<String, Object> pMapWeek = JsonFastJsonUtil.toMap(timerResolver.week);
            pMapMinute.put("return", String.valueOf(Integer.parseInt(get_return_data(timerResolver, "week")) * 2));
            timerResolver.week = JsonFastJsonUtil.toFeaturesJson(pMapWeek);

            Map<String, Object> pMapMonth = JsonFastJsonUtil.toMap(timerResolver.month);
            pMapMinute.put("return", String.valueOf(Integer.parseInt(get_return_data(timerResolver, "month")) * 2));
            timerResolver.month = JsonFastJsonUtil.toFeaturesJson(pMapMonth);


            Map<String, Object> pMapYear = JsonFastJsonUtil.toMap(timerResolver.year);
            pMapMinute.put("return", String.valueOf(Integer.parseInt(get_return_data(timerResolver, "year")) * 2));
            timerResolver.year = JsonFastJsonUtil.toFeaturesJson(pMapYear);


            String string = timerResolver.plan.substring(1);
            if ("n".equals(string)) { //如果是n那么就是循环执行
                return get_next_time(timerResolver);
            }
            if (StringUtil.isNumeric(string)) {//如果是数字,那么就是有限阶级执行
                if ("0".equals(string)) {
                    return 0;
                }
                String one = StringUtil.str_calculate_one(string, "--");
                assert one != null;
                timerResolver.plan = "^".concat(one);
                return get_next_time(timerResolver);
            }
        }
        //模式执行
        if (timerResolver.plan != null && timerResolver.plan.startsWith("mode")) {
            timerResolver.timestamp = DateUtil.getDateStart(DateUtil.secondTurnDate(timerResolver.timestamp));// 初始化为当天的开始时间 ,然后在后面的计算中,加上指定的时间
            if (timerResolver.plan.length() == 4) { //循环执行
                return get_next_time(timerResolver);
            } else {
                String string = timerResolver.plan.substring(4);
                if (StringUtil.isNumeric(string)) {//如果是数字,那么就是有限阶级执行
                    if (string.equals("0")) {
                        return 0;
                    }
                    String one = StringUtil.str_calculate_one(string, "--");
                    assert one != null;
                    timerResolver.plan = "mode".concat(one);
                    return get_next_time(timerResolver);
                } else {

                    throw new Exception(String.format("mode解析错误非法字符(必须是mode+数字):%s\n", timerResolver.plan));
                }
            }
        }
        //有限执行
        if (timerResolver.plan != null && StringUtil.isNumeric(timerResolver.plan)) {
            int plan = Integer.parseInt(timerResolver.plan);
            if (plan > 0) {
                timerResolver.plan = String.valueOf(plan - 1);
                return get_next_time(timerResolver);
            }
        }
        return 0;
    }

    public void setTimerResolverField(TimerResolverCore timerResolver, String field, String value) {
        if ("second".equals(field)) {
            timerResolver.second = value;
        } else if ("minute".equals(field)) {
            timerResolver.minute = value;
        } else if ("hour".equals(field)) {
            timerResolver.hour = value;
        } else if ("day".equals(field)) {
            timerResolver.day = value;
        } else if ("week".equals(field)) {
            timerResolver.week = value;
        } else if ("month".equals(field)) {
            timerResolver.month = value;
        } else if ("year".equals(field)) {
            timerResolver.year = value;
        } else if ("plan".equals(field)) {
            timerResolver.plan = value;
        } else if ("timestamp".equals(field)) {
            timerResolver.timestamp = Long.parseLong(value);
        } else if ("help".equals(field)) {
            timerResolver.help = value;
        }
    }

    public String getTimerResolverField(TimerResolverCore timerResolver, String field) {
        if ("second".equals(field)) {
            return timerResolver.second;
        } else if ("minute".equals(field)) {
            return timerResolver.minute;
        } else if ("hour".equals(field)) {
            return timerResolver.hour;
        } else if ("day".equals(field)) {
            return timerResolver.day;
        } else if ("week".equals(field)) {
            return timerResolver.week;
        } else if ("month".equals(field)) {
            return timerResolver.month;
        } else if ("year".equals(field)) {
            return timerResolver.year;
        } else if ("plan".equals(field)) {
            return timerResolver.plan;
        } else if ("timestamp".equals(field)) {
            return String.valueOf(timerResolver.timestamp);
        } else if ("help".equals(field)) {
            return timerResolver.help;
        }
        return null;
    }

    //获取结构化数据的返回数据
    String get_return_data(TimerResolverCore timerResolver, String field) {

        String data = getTimerResolverField(timerResolver, field);
        //判断是否是JSON格式
        if (JsonFastJsonUtil.isJson(data)) {
            //解析字典
            Map<String, Object> pMap = JsonFastJsonUtil.toMap(data);
            //获取类型
            String type =  pMap.get("type").toString();
            //获取data
            data = pMap.get("data") == null ? null : pMap.get("data").toString();
            //获取index
            String index = pMap.get("index") == null ? null : pMap.get("index").toString();
            //获取返回数据
            String returnData = pMap.get("return").toString();
            //获取数组
            JSONArray jsonArray = data == null ? null : JSONArray.parseArray(data);
            //获取下一个值
            Integer nextIndex = index == null ? null : Integer.parseInt(index) + 1;

            //如果是[]类型 那么就是分段  例如 [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24]
            //如果类型是~ 那么就是区间 1~5 表示[1,2,3,4,5]
            if (type.contains("[]") || type.contains("~")) {
                //判断是否超出数组长度,如果超出长度,从头开始
                if (nextIndex >= jsonArray.size()) {
                    //超出长度,返回空
                    nextIndex = 0;
                }
                //获取下一个值
                String nextValue = jsonArray.get(nextIndex).toString();
                //设置返回数据
                pMap.put("return", nextValue);
                //设置index
                pMap.put("index", nextIndex);
                //修改原数据
                setTimerResolverField(timerResolver, field, JsonFastJsonUtil.toJson(pMap));
                //返回数据
                return returnData;
            } else if (type.contains("/")) { // 如果类型是/ (5/2)表示从5开始,每隔2个数 执行一次
                //如果类型是/
                if (nextIndex == 1) {
                    //判断index是否是1,如果是1,取第二个值,并且转换为普通结构
                    String string = String.format("{ \"type\":\"number\" , \"return\": %s } ", jsonArray.get(nextIndex));
                    setTimerResolverField(timerResolver, field, string);
                }
                return returnData;
            } else if (type.contains("number") || type.contains("mode")) {
                //如果类型是number或者mode,那么获取return
                return returnData;
            }
        }
        AssertUtil.isTrue(false, "结构化数据格式错误:" + data);
        return null;
    }


    //定时器语法转换为结构化格式
    void switchStructure(TimerResolverCore timerResolver) throws Exception {
        //秒
        if (timerResolver.second.contains("[")) {
            timerResolver.second = section_analysis(timerResolver.second, timerResolver);
        } else if (timerResolver.second.contains("~")) {
            timerResolver.second = range_analysis(timerResolver.second, timerResolver);
        } else if (timerResolver.second.contains("/")) {
            timerResolver.second = interval_analysis(timerResolver.second, timerResolver);
        } else if (mode_exist(timerResolver.second, timerResolver)) {
            timerResolver.second = mode_analysis(timerResolver.second, timerResolver);
        } else {
            timerResolver.second = normal_analysis(timerResolver.second);
        }
        //分
        if (timerResolver.minute.contains("[")) {
            timerResolver.minute = section_analysis(timerResolver.minute, timerResolver);
        } else if (timerResolver.minute.contains("~")) {
            timerResolver.minute = range_analysis(timerResolver.minute, timerResolver);
        } else if (timerResolver.minute.contains("/")) {
            timerResolver.minute = interval_analysis(timerResolver.minute, timerResolver);
        } else if (mode_exist(timerResolver.minute, timerResolver)) {
            timerResolver.minute = mode_analysis(timerResolver.minute, timerResolver);
        } else {
            timerResolver.minute = normal_analysis(timerResolver.minute);
        }
        //时
        if (timerResolver.hour.contains("[")) {
            timerResolver.hour = section_analysis(timerResolver.hour, timerResolver);
        } else if (timerResolver.hour.contains("~")) {
            timerResolver.hour = range_analysis(timerResolver.hour, timerResolver);
        } else if (timerResolver.hour.contains("/")) {
            timerResolver.hour = interval_analysis(timerResolver.hour, timerResolver);
        } else if (mode_exist(timerResolver.hour, timerResolver)) {
            timerResolver.hour = mode_analysis(timerResolver.hour, timerResolver);
        } else {
            timerResolver.hour = normal_analysis(timerResolver.hour);
        }
        //日
        if (timerResolver.day.contains("[")) {
            timerResolver.day = section_analysis(timerResolver.day, timerResolver);
        } else if (timerResolver.day.contains("~")) {
            timerResolver.day = range_analysis(timerResolver.day, timerResolver);
        } else if (timerResolver.day.contains("/")) {
            timerResolver.day = interval_analysis(timerResolver.day, timerResolver);
        } else if (mode_exist(timerResolver.day, timerResolver)) {
            timerResolver.day = mode_analysis(timerResolver.day, timerResolver);
        } else {
            timerResolver.day = normal_analysis(timerResolver.day);
        }
        //周
        if (timerResolver.week.contains("[")) {
            timerResolver.week = section_analysis(timerResolver.week, timerResolver);
        } else if (timerResolver.week.contains("~")) {
            timerResolver.week = range_analysis(timerResolver.week, timerResolver);
        } else if (timerResolver.week.contains("/")) {
            timerResolver.week = interval_analysis(timerResolver.week, timerResolver);
        } else if (mode_exist(timerResolver.week, timerResolver)) {
            timerResolver.week = mode_analysis(timerResolver.week, timerResolver);
        } else {
            timerResolver.week = normal_analysis(timerResolver.week);
        }
        //月
        if (timerResolver.month.contains("[")) {
            timerResolver.month = section_analysis(timerResolver.month, timerResolver);
        } else if (timerResolver.month.contains("~")) {
            timerResolver.month = range_analysis(timerResolver.month, timerResolver);
        } else if (timerResolver.month.contains("/")) {
            timerResolver.month = interval_analysis(timerResolver.month, timerResolver);
        } else if (mode_exist(timerResolver.month, timerResolver)) {
            timerResolver.month = mode_analysis(timerResolver.month, timerResolver);
        } else {
            timerResolver.month = normal_analysis(timerResolver.month);
        }
        //年
        if (timerResolver.year.contains("[")) {
            timerResolver.year = section_analysis(timerResolver.year, timerResolver);
        } else if (timerResolver.year.contains("~")) {
            timerResolver.year = range_analysis(timerResolver.year, timerResolver);
        } else if (timerResolver.year.contains("/")) {
            timerResolver.year = interval_analysis(timerResolver.year, timerResolver);
        } else if (mode_exist(timerResolver.year, timerResolver)) {
            timerResolver.year = mode_analysis(timerResolver.year, timerResolver);
        } else {
            timerResolver.year = normal_analysis(timerResolver.year);
        }

    }


    //分段[]解析
    private String section_analysis(String item, TimerResolverCore timerResolver) throws Exception {
        //在帮助里存储当前的次数
        Map<String, Object> pMap = JsonFastJsonUtil.toMap(timerResolver.help);
        if (!pMap.containsKey("basicsPlan")) {
            pMap.put("basicsPlan", timerResolver.plan);
            timerResolver.help = JsonFastJsonUtil.toFeaturesJson(pMap);
        }
        //将[]里面的@转换为,号
        item = StringUtil.str_replace_all_n(item, "@", ",", 0, item.length());
        List<String> pCharlist = JsonFastJsonUtil.toList(item, String.class);
        //验证是否都是数字
        for (int i = 0; i < pCharlist.size(); ++i) {

            if (!StringUtil.isNumeric(pCharlist.get(i))) {
                throw new Exception("[]解析错误非法字符(必须是数字):" + pCharlist.get(i));

            }
        }
        String string = String.format("{ \"type\":[] , \"data\":%s , \"index\":0 ,\"return\":%s } ", item, pCharlist.get(0));
        //添加执行的次数
        Map<String, Object> pMap1 = JsonFastJsonUtil.toMap(timerResolver.help);
        String basicsPlan = pMap1.get("basicsPlan").toString();
        if ("n".equals(basicsPlan) || "^".equals(basicsPlan)) {
            timerResolver.plan = timerResolver.plan;
        } else {
            if (timerResolver.plan.equals(basicsPlan)) {
                timerResolver.plan = String.valueOf(pCharlist.size() * Integer.parseInt(basicsPlan));
            } else {
                timerResolver.plan = String.valueOf(
                        Integer.parseInt(timerResolver.plan) + (pCharlist.size() * Integer.parseInt(basicsPlan)));
            }
        }
        return string;
    }

    //区间解析 符号为~ ,位置1表示秒,2表示分支,3表示小时,4表示天,5表示星期,6表示月,7表示年
    private String range_analysis(String item, TimerResolverCore timerResolver) throws Exception {
        //在帮助里存储当前的次数
        Map<String, Object> pMap = JsonFastJsonUtil.toMap(timerResolver.help);
        if (!pMap.containsKey("basicsPlan")) {
            pMap.put("basicsPlan", timerResolver.plan);
            timerResolver.help = JsonFastJsonUtil.toFeaturesJson(pMap);
        }
        String[] pCharlist = item.split("~");
        //验证是否都是数字
        for (String s : pCharlist) {
            if (!StringUtil.isNumeric(s)) {
                throw new Exception(String.format("~解析错误非法字符(必须是数字):%s\n", s));
            }
        }
        List<String> range = StringUtil.get_range(pCharlist[0], pCharlist[1]);
        String data = JsonFastJsonUtil.toFeaturesJson(range);
        String string = "{" + "\"type\":\"~\"" + "," + "\"data\":" + data + "," + "\"index\":0" + "," + "\"return\":" + range.get(0) + "}";
        //添加执行的次数
        Map<String, Object> pMap1 = JsonFastJsonUtil.toMap(timerResolver.help);
        String basicsPlan = pMap1.get("basicsPlan").toString();
        if ("n".equals(basicsPlan) || basicsPlan.startsWith("^")) {
            timerResolver.plan = timerResolver.plan;
        } else {
            if (timerResolver.plan.equals(basicsPlan)) {
                timerResolver.plan = String.valueOf(range.size() * Integer.parseInt(basicsPlan));
            } else {
                timerResolver.plan = String.valueOf(Integer.parseInt(timerResolver.plan) + (range.size() * Integer.parseInt(basicsPlan)));
            }
        }
        return string;
    }


    //间隔/ 解析  (5/2)表示从5开始,每隔2个数 执行一次
    private String interval_analysis(String item, TimerResolverCore timerResolver) throws Exception {
        //在帮助里存储当前的次数
        Map<String, Object> pMap = JsonFastJsonUtil.toMap(timerResolver.help);
        if (!pMap.containsKey("basicsPlan")) {
            pMap.put("basicsPlan", timerResolver.plan);
            timerResolver.help = JsonFastJsonUtil.toFeaturesJson(pMap);
        }

        String[] pCharlist = item.split("/");
        //验证是否都是数字
        for (int i = 0; i < pCharlist.length; ++i) {
            if (!StringUtil.isNumeric(pCharlist[i])) {
                throw new Exception(String.format("/解析错误非法字符(必须是数字):%s\n", pCharlist[i]));
            }
        }
        String data = "[" + pCharlist[0] + "," + pCharlist[1] + "]";
        String string = "{" + "\"type\":\"/\"" + "," + "\"data\":" + data + "," + "\"index\":0" + "," + "\"return\":" + pCharlist[0] + "}";
        //添加执行的次数
        Map<String, Object> pMap1 = JsonFastJsonUtil.toMap(timerResolver.help);
        String basicsPlan = pMap1.get("basicsPlan").toString();
        if ("n".equals(basicsPlan) || "^".equals(basicsPlan)) {
            timerResolver.plan = timerResolver.plan;
        } else {
            if (timerResolver.plan.equals(basicsPlan)) {
                timerResolver.plan = Integer.toString(Integer.parseInt(basicsPlan) + 1);
            }
        }
        return string;
    }

    String mode_analysis(String item, TimerResolverCore timerResolver) throws Exception {
        Set<Map.Entry<String, Object>> entries = timerResolver.mode.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            if (item.startsWith(entry.getKey())) {
                String string = "{" + "\"type\":\"mode\"" + "," + "\"return\":\"" + item + "\"}";
                return string;
            }
        }
        throw new Exception(String.format("mode模式解析错误没有找到对应的模式-请检查模式是否自定义-并且添加到模式集中:%s\n", item));
    }

    //普通解析
    String normal_analysis(String item) throws Exception {
        if (StringUtil.isNumeric(item)) {
            String string = "{" + "\"type\":\"number\"" + "," + "\"return\":" + item + "}";
            return string;
        }
        throw new Exception(String.format("解析错误,无法解析的表达式:%s\n", item));

    }

    // 获取计划的执行类型(有限还是无限,如果是有限则返回执行次数,如果是无限则返回-1,如果执行计划是0那么返回0(未启动))
    public int get_plan_type(TimerResolverCore timerResolver) {
        if ("0".equals(timerResolver.plan)) {//未启动
            return 0;
        }
        if ("n".equals(timerResolver.plan)) { //无限
            return -1;
        }
        if ("^n".equals(timerResolver.plan)) {//无限
            return -1;
        }
        if ("^".startsWith(timerResolver.plan)) {//获取阶级的执行次数
            return Integer.parseInt(timerResolver.plan.replace("^", ""));
        }
        if (StringUtil.isNumeric(timerResolver.plan)) {//获取执行次数
            return Integer.parseInt(timerResolver.plan);
        }
        return 0;
    }
}
