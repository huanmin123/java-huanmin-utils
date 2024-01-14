package org.huanmin.utils.timerwheel;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/11/30 17:16
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
@Data
@AllArgsConstructor
public class TimerNodeData {
    String key;// 用于排序和搜索等
    TimerNode data; //实际数据存储


}
