package org.huanmin.utils.timerwheel.mode;

import org.huanmin.utils.common.base.DateUtil;
import org.huanmin.utils.timerwheel.TimerResolverCore;
import org.huanmin.utils.timerwheel.TimerResolverModeMethod;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/12/1 1:21
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class TimeEndMode implements TimerResolverModeMethod {
    @Override
    public long modeMethod(String item, int seat, TimerResolverCore timerResolver) {
        if (seat == 0) { //秒数
            return timerResolver.timestamp+59;
        } else if (seat == 1) { //分钟数(如果当前时间是59秒,则返回下一分钟的59秒)
            return DateUtil.getDateNextMinuteEnd(DateUtil.secondTurnDate(timerResolver.timestamp)) ;
        } else if (seat == 2) { //小时数
            return DateUtil.getDateNextDayEnd(DateUtil.secondTurnDate(timerResolver.timestamp)) ;
        } else if (seat == 3) { //天数
            return DateUtil.getDateNextDayEnd(DateUtil.secondTurnDate(timerResolver.timestamp)) ;
        } else if (seat == 4) { //星期数(周末开始时间)
            return DateUtil.getDateNextWeekEnd(DateUtil.secondTurnDate(timerResolver.timestamp));
        } else if (seat == 5) { //月数
            return DateUtil.getDateNextMonthEnd(DateUtil.secondTurnDate(timerResolver.timestamp)) ;
        } else if (seat == 6) { //年数
            return DateUtil.getDateNextYearEnd(DateUtil.secondTurnDate(timerResolver.timestamp)) ;
        }
        return 0;
    }
}
