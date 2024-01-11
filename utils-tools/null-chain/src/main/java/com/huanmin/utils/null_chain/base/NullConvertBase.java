package com.huanmin.utils.null_chain.base;

import com.huanmin.utils.common.enums.DateEnum;
import com.huanmin.utils.common.json.JsonJacksonUtil;
import com.huanmin.utils.common.obj.serializable.SerializeUtil;
import com.huanmin.utils.common.string.StringUtil;
import com.huanmin.utils.null_chain.NullFun;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author huanmin
 * @date 2024/1/11
 */
public  class NullConvertBase<T extends Serializable> extends NullFinalityBase<T > implements NullConvert<T> {



    @Override
    public Stream<T> stream() {
        return !isNull ? Stream.of(value) : Stream.empty();
    }

    @Override
    public Optional<T> optional() {
        return !isNull ? Optional.of(value) : Optional.empty();
    }


    @Override
    public String toStr() {
        if (isNull) {
            throw new NullPointerException(linkLog.toString());
        }
        if (value instanceof String) {
            if (((String) value).isEmpty()) {
                throw new NullPointerException(linkLog.toString() + "是''空的字符串长度为0(这个不是null)");
            }
            return  value.toString();
        }
        throw new NullPointerException(linkLog.toString() + " 注意上级调用不是字符串无法使用isEmpty方法");
    }

    @Override
    public Integer toInt() {
        if (isNull) {
            throw new NullPointerException(linkLog.toString());
        }
        if (value instanceof Number || value instanceof String) {
            if (value.toString().isEmpty()) {
                throw new NullPointerException(linkLog.toString() + "转换为Integer失败,因为是''空的字符串长度为0(这个不是null)");
            }
            try {
                return Integer.parseInt(value.toString());
            } catch (NumberFormatException e) {
                throw new NumberFormatException(linkLog.toString() + " [" + e.getMessage() + "] " + "请检查字符串中是纯数字吗?,一定不能包含其他字符的。");
            }
        }
        throw new NullPointerException(linkLog.toString() + " 注意上级调用不是Number或String无法使用intValue方法");
    }

    //将时间类型(Date,LocalDate,LocalDateTime), 10或13位时间戳, 转换为指定格式的时间字符串
    @Override
    public String toDateFormat(DateEnum dateEnum) {
        if (isNull) {
            throw new NullPointerException(linkLog.toString());
        }
        if (value instanceof String) {
            //判断是否是纯数字,并且>=10<=13位
            if (StringUtil.isPositiveNumeric(value.toString()) && value.toString().length() >= 10 && value.toString().length() <= 13) {
                String timeString = value.toString();
                //长度不为13位的话,添加3个0
                if (timeString.length() != 13) {
                    timeString = timeString + "000";
                }
                long time = Long.parseLong(timeString);
                Date parse = new Date(time);
                SimpleDateFormat sdf = new SimpleDateFormat(dateEnum.getValue());
                return sdf.format(parse);
            }
            return value.toString();
        }
        if (value instanceof Long || value instanceof Integer) {
            String timeString = value.toString();
            //长度不为13位的话,添加3个0
            if (timeString.length() != 13) {
                timeString = timeString + "000";
            }
            long time = Long.parseLong(timeString);
            Date parse = new Date(time);
            SimpleDateFormat sdf = new SimpleDateFormat(dateEnum.getValue());
            return sdf.format(parse);
        }
        if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat(dateEnum.getValue());
            return sdf.format((Date) value);
        }
        if (value instanceof LocalDate) {
            LocalDate localDate = (LocalDate) value;
            long epochMilli = localDate.atStartOfDay(ZoneOffset.of("+8")).toInstant().toEpochMilli(); //毫秒
            Date parse = new Date(epochMilli);
            SimpleDateFormat sdf = new SimpleDateFormat(dateEnum.getValue());
            return sdf.format(parse);
        }
        if (value instanceof LocalDateTime) {
            LocalDateTime localDate = (LocalDateTime) value;
            long epochMilli = localDate.toInstant(ZoneOffset.of("+8")).toEpochMilli();
            Date parse = new Date(epochMilli);
            SimpleDateFormat sdf = new SimpleDateFormat(dateEnum.getValue());
            return sdf.format(parse);
        }
        throw new NullPointerException(linkLog.toString() + " 注意上级调用不是时间对象(Date,LocalDate,LocalDateTime)或者10或13位时间戳(数值或字符串),无法使用toDateFormat方法");
    }

    @Override
    public byte[] toBytes() {
        byte[] serialize = SerializeUtil.serialize(value);
        if (serialize == null) {
            throw new NullPointerException(linkLog.toString());
        }
        return serialize;
    }

    //使用的是Jackson转换,如果要使用fastjson,请自行修改
    @Override
    public String toJson() {
        if (isNull) {
            throw new NullPointerException(linkLog.toString());
        }
        return JsonJacksonUtil.toJson(value);
    }
}
