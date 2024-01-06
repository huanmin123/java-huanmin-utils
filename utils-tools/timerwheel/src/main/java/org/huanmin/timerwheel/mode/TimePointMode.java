package org.huanmin.timerwheel.mode;



import com.utils.common.base.DateUtil;
import org.huanmin.timerwheel.TimerResolverCore;
import org.huanmin.timerwheel.TimerResolverModeMethod;

import java.util.Date;

/**
 * 获取以当前时间后指定年月日时分秒
 *
 * @Author: huanmin
 * @Date: 2022/11/30 19:53
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class TimePointMode implements TimerResolverModeMethod {

    @Override
    public long modeMethod(String item, int seat, TimerResolverCore timerResolver) {
        //解析point(?)模式,取出?的值
        int i = item.indexOf("(");
        item = item.substring( i + 1, item.length() - 1);
        if (seat == 0) { //加秒数
            Date date = DateUtil.addDate(DateUtil.secondTurnDate(timerResolver.timestamp), 0, 0, 0, 0, 0, Integer.parseInt(item));
            return date.getTime();
        } else if (seat == 1) { //分钟数
            Date date = DateUtil.addDate(DateUtil.secondTurnDate(timerResolver.timestamp), 0, 0, 0, 0, Integer.parseInt(item), 0);
            return date.getTime();
        } else if (seat == 2) { //小时数
            Date date = DateUtil.addDate(DateUtil.secondTurnDate(timerResolver.timestamp), 0, 0, 0, Integer.parseInt(item),0 , 0);
            return  date.getTime();
        } else if (seat == 3) { //天数
            Date date = DateUtil.addDate(DateUtil.secondTurnDate(timerResolver.timestamp), 0, 0, Integer.parseInt(item),0, 0 , 0);
            return  date.getTime();
        } else if (seat == 4) { //周n的日期
            return DateUtil.getDateWeek(DateUtil.secondTurnDate(timerResolver.timestamp), Integer.parseInt(item));
        } else if (seat == 5) { //月数
            Date date = DateUtil.addDate(DateUtil.secondTurnDate(timerResolver.timestamp), 0,  Integer.parseInt(item),0,0, 0 , 0);
            return  date.getTime();
        } else if (seat == 6) { //年数
            Date date = DateUtil.addDate(DateUtil.secondTurnDate(timerResolver.timestamp), Integer.parseInt(item),  0,0,0, 0 , 0);
            return  date.getTime();
        }
        return 0;
    }
}
