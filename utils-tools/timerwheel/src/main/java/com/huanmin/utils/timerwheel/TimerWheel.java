package com.huanmin.utils.timerwheel;

import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/11/30 17:05
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class TimerWheel {
    int slot_num; //时间轮槽数量
    int type; //时间轮类型(0毫秒,1秒,2分,3时,4天,5月,6年)
    int slot_index; //时间轮槽索引
    List<TimerNodeData> slot_array; //时间轮插槽数组
    List<Lock> locks; //插槽锁
}
