package com.utils.common.multithreading.timer.timerwheel.mode;


import com.utils.common.base.DateUtil;
import com.utils.common.multithreading.timer.timerwheel.TimerResolverCore;
import com.utils.common.multithreading.timer.timerwheel.TimerResolverModeMethod;
import com.utils.common.multithreading.timer.timerwheel.timerannotation.TimerResolver;

/**
 * 判断指定时间,初的时间
 *
 * @Author: huanmin
 * @Date: 2022/12/1 0:16
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class TimeBeginMode implements TimerResolverModeMethod {
    @Override
    public long modeMethod(String item, int seat, TimerResolverCore timerResolver) {
        if (seat == 0) { //秒数
            return DateUtil.getDateNextSecond(DateUtil.secondTurnDate(timerResolver.timestamp));
        } else if (seat == 1) { //分钟数
            return DateUtil.getDateNextMinute(DateUtil.secondTurnDate(timerResolver.timestamp));
        } else if (seat == 2) { //小时数
            return DateUtil.getDateNextHour(DateUtil.secondTurnDate(timerResolver.timestamp));
        } else if (seat == 3) {// 天数
            return DateUtil.getDateNextDay(DateUtil.secondTurnDate(timerResolver.timestamp));
        } else if (seat == 4) {// 星期数
            return DateUtil.getDateNextWeekStart(DateUtil.secondTurnDate(timerResolver.timestamp));
        } else if (seat == 5) {// 月数
            return DateUtil.getDateNextMonth(DateUtil.secondTurnDate(timerResolver.timestamp));
        } else if (seat == 6) {// 年数
            return DateUtil.getDateNextYear(DateUtil.secondTurnDate(timerResolver.timestamp));
        }
        return 0;
    }
}
