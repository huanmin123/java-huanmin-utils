package com.utils.common.multithreading.timer.timerwheel;

import com.utils.common.multithreading.timer.timerwheel.timerannotation.TimerResolver;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/11/30 19:51
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public interface TimerResolverModeMethod {
    long modeMethod(String item, int seat, TimerResolverCore timerResolver);
}
