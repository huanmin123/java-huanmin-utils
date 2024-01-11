package com.utils.null_chain;

import com.utils.common.enums.DateEnum;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public interface NullConvert<T>   {

    //将一个类型转换为另外一个类型, 万能转换,啥都能做
    <U> U convert(NullFun<? super T, ? extends U> mapper);

    Stream<T> stream();

    Optional<T> optional();



    //=====================个性化处理, 针对日常最常用的几种类型转换=====================

    //判断是否为空(包括空值,长度是0的字符串), 如果为空, 则抛出异常,如果不是空, 则返回值
    //如果非字符串类型, 则抛出异常
    String toStr();
    //将字符串或者数值类型转换为Integer, 为啥不是Long, 因为Long类型用的不多, 一般都是Integer
    //如果是非字符串或者数值类型, 则抛出异常
    Integer toInt();
    //将时间类型(Date,LocalDate,LocalDateTime), 10或13位时间戳(数值或字符串), 转换为指定格式化后的时间字符串
    String toDateFormat(DateEnum dateEnum);

}
