package com.utils.common.base;

/**
 * Obj转各种类型大全
 *
 * @Author: huanmin
 * @Date: 2022/6/18 21:23
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */

import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.temporal.Temporal;
import java.util.Date;

/**

 * 一个简单的数据类型转换工具类
 */
public class ObjConverter {

    public static String getAsString(final Object obj) {
        if (obj != null) {
            return obj.toString();
        }
        return null;
    }


    public static String getAsString(final Object obj, final String defaultValue) {
        String answer = getAsString(obj);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }


    public static Number getAsNumber(final Object obj) {
        if (obj != null) {
            if (obj instanceof Number) {
                return (Number) obj;
            } else if (obj instanceof Boolean) {
                return ((Boolean) obj) ? 1 : 0;
            } else if (obj instanceof String) {
                try {
                    return NumberFormat.getInstance().parse((String) obj);
                } catch (final ParseException e) {
                    throw new NumberFormatException("For input string: \"" + obj + "\"");
                }
            } else {
                throw new UnsupportedOperationException();
            }
        }
        return null;
    }



    public static Boolean getAsBoolean(final Object obj) {
        if (obj != null) {
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            } else if (obj instanceof String) {
                return Boolean.valueOf((String) obj);
            } else if (obj instanceof Number) {
                final Number n = (Number) obj;
                return (n.intValue() != 0) ? Boolean.TRUE : Boolean.FALSE;
            } else {
                throw new UnsupportedOperationException();
            }
        }
        return null;
    }


    public static Boolean getAsBoolean(final Object obj, final Boolean defaultValue) {
        Boolean answer = getAsBoolean(obj);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }

    public static boolean getAsBooleanValue(final Object obj) {
        final Boolean booleanObject = getAsBoolean(obj);
        if (booleanObject == null) {
            return false;
        }
        return booleanObject.booleanValue();
    }

    public static boolean getAsBooleanValue(final Object obj, final boolean defaultValue) {
        final Boolean booleanObject = getAsBoolean(obj);
        if (booleanObject == null) {
            return defaultValue;
        }
        return booleanObject.booleanValue();
    }

    public static Byte getAsByte(final Object obj) {
        final Number answer = getAsNumber(obj);
        if (answer == null) {
            return null;
        } else if (answer instanceof Byte) {
            return (Byte) answer;
        }
        return Byte.valueOf(answer.byteValue());
    }


    public static Byte getAsByte(final Object obj, final Byte defaultValue) {
        Byte answer = getAsByte(obj);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }

    public static byte getAsByteValue(final Object obj) {
        final Byte byteObject = getAsByte(obj);
        if (byteObject == null) {
            return 0;
        }
        return byteObject.byteValue();
    }


    public static byte getAsByteValue(final Object obj, final byte defaultValue) {
        final Byte byteObject = getAsByte(obj);
        if (byteObject == null) {
            return defaultValue;
        }
        return byteObject.byteValue();
    }


    public static Short getAsShort(final Object obj) {
        final Number answer = getAsNumber(obj);
        if (answer == null) {
            return null;
        } else if (answer instanceof Short) {
            return (Short) answer;
        }
        return Short.valueOf(answer.shortValue());
    }

    public static Short getAsShort(final Object obj, final Short defaultValue) {
        Short answer = getAsShort(obj);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }


    public static short getAsShortValue(final Object obj) {
        final Short shortObject = getAsShort(obj);
        if (shortObject == null) {
            return 0;
        }
        return shortObject.shortValue();
    }

    public static short getAsShortValue(final Object obj, final short defaultValue) {
        final Short shortObject = getAsShort(obj);
        if (shortObject == null) {
            return defaultValue;
        }
        return shortObject.shortValue();
    }


    public static Integer getAsInteger(final Object obj) {
        final Number answer = getAsNumber(obj);
        if (answer == null) {
            return null;
        } else if (answer instanceof Integer) {
            return (Integer) answer;
        }
        return Integer.valueOf(answer.intValue());
    }


    public static Integer getAsInteger(final Object obj, final Integer defaultValue) {
        Integer answer = getAsInteger(obj);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }


    public static int getAsIntValue(final Object obj) {
        final Integer integerObject = getAsInteger(obj);
        if (integerObject == null) {
            return 0;
        }
        return integerObject.intValue();
    }


    public static int getAsIntValue(final Object obj, final int defaultValue) {
        final Integer integerObject = getAsInteger(obj);
        if (integerObject == null) {
            return defaultValue;
        }
        return integerObject.intValue();
    }


    public static Long getAsLong(final Object obj) {
        final Number answer = getAsNumber(obj);
        if (answer == null) {
            return null;
        } else if (answer instanceof Long) {
            return (Long) answer;
        }
        return Long.valueOf(answer.longValue());
    }

    public static Long getAsLong(final Object obj, final Long defaultValue) {
        Long answer = getAsLong(obj);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }


    public static long getAsLongValue(final Object obj) {
        final Long longObject = getAsLong(obj);
        if (longObject == null) {
            return 0L;
        }
        return longObject.longValue();
    }



    public static Float getAsFloat(final Object obj) {
        final Number answer = getAsNumber(obj);
        if (answer == null) {
            return null;
        } else if (answer instanceof Float) {
            return (Float) answer;
        }
        return Float.valueOf(answer.floatValue());
    }


    public static float getAsFloatValue(final Object obj) {
        final Float floatObject = getAsFloat(obj);
        if (floatObject == null) {
            return 0f;
        }
        return floatObject.floatValue();
    }




    public static Double getAsDouble(final Object obj) {
        final Number answer = getAsNumber(obj);
        if (answer == null) {
            return null;
        } else if (answer instanceof Double) {
            return (Double) answer;
        }
        return Double.valueOf(answer.doubleValue());
    }


    public static double getAsDoubleValue(final Object obj) {
        final Double doubleObject = getAsDouble(obj);
        if (doubleObject == null) {
            return 0d;
        }
        return doubleObject.doubleValue();
    }

    public static Character getAsChar( Object obj) {
        if (obj == null) {
            return Character.MIN_VALUE;
        } else  if (obj instanceof Character) {
           return  (Character) obj;
        }
        return null;
    }
    public static char getAsCharValue( Object obj) {
        Character asChar = getAsChar(obj);
        return asChar.charValue();
    }



    public static BigInteger getAsBigInteger(final Object obj) {
        if (obj != null) {
            if (obj instanceof BigInteger) {
                return (BigInteger) obj;
            } else if (obj instanceof String) {
                return new BigInteger((String) obj);
            } else if (obj instanceof Number || obj instanceof Boolean) {
                final Number answer = getAsNumber(obj);
                if (answer != null) {
                    return BigInteger.valueOf(answer.longValue());
                }
            } else {
                throw new UnsupportedOperationException();
            }
        }
        return null;
    }




    public static BigDecimal getAsBigDecimal(final Object obj) {
        if (obj != null) {
            if (obj instanceof BigDecimal) {
                return (BigDecimal) obj;
            } else if (obj instanceof String) {
                return new BigDecimal((String) obj);
            } else if (obj instanceof Number || obj instanceof Boolean) {
                final Number answer = getAsNumber(obj);
                if (answer != null) {
                    return BigDecimal.valueOf(answer.doubleValue());
                }
            } else {
                throw new UnsupportedOperationException();
            }
        }
        return null;
    }
    public static Date getAsDate(final Object obj) {
        if(obj==null){
            return  null;
        }else  if(obj instanceof Date){
            return (Date) obj;
        }
        return null;
    }

    // 转换LocalDate的时间
    public static Temporal getAsTemporal(final Object obj) {
        if(obj==null){
            return  null;
        }else  if(obj instanceof Temporal){
            return (Temporal) obj;
        }
        return null;
    }

    //通过泛型转换类型
    public static <R> R cast(final Object obj, final Class<R> clz) {
        if (obj == null) { throw new IllegalArgumentException("'obj' must not be null"); }
        if (clz == null) { throw new IllegalArgumentException("'clz' must not be null"); }
        return (R)obj;
    }
    //通过类型然后值copy进行转换类型
    @SneakyThrows
    public static Object cast(final Class clz, final Object obj) {
        if (obj == null) { throw new IllegalArgumentException("'obj' must not be null"); }
        if (clz == null) { throw new IllegalArgumentException("'clz' must not be null"); }
        Object o = clz.newInstance();
        BeanUtils.copyProperties(o ,obj);
        return o;
    }


    public static Object cast(final Object obj) {
        if (obj == null) { throw new IllegalArgumentException("'obj' must not be null"); }
        Object result = null;
        if (obj instanceof Boolean) {
            result =  getAsBoolean(obj);
        } else if (obj instanceof  Byte) {
            result =  getAsByte(obj);
        } else if (obj instanceof Short) {
            result =  getAsShort(obj);
        } else if (obj instanceof Integer) {
            result =  getAsInteger(obj);
        } else if (obj instanceof Long) {
            result =  getAsLong(obj);
        } else if (obj instanceof Float) {
            result =  getAsFloat(obj);
        } else if (obj instanceof Double) {
            result =  getAsDouble(obj);
        } else if (obj instanceof String) {
            result =  getAsString(obj);
        } else if (obj instanceof BigInteger) {
            result =  getAsBigInteger(obj);
        } else if (obj instanceof BigDecimal) {
            result =  getAsBigDecimal(obj);
        } else if (obj instanceof Number) {
            result =  getAsNumber(obj);
        } else if (obj instanceof Character) {
            result =  getAsChar(obj);
        } else if (obj instanceof Date) {
            result =  getAsDate(obj);
        } else if (obj instanceof Temporal) {
            result =  getAsTemporal(obj);
        } else {
           return null;
        }
        return result;
    }


    @SneakyThrows
    public static Object cast( Object value,String fieldType) {
        Object result = null;
        switch (fieldType) {
            case "int":
            case "java.lang.Integer":
                result = getAsInteger(value);
                break;
            case "long":
            case "java.lang.Long":
                result = getAsLong(value);
                break;
            case "double":
            case "java.lang.Double":
                result =getAsDouble(value);
                break;
            case "byte":
            case "java.lang.Byte":
                result =getAsByte(value);
                break;
            case "boolean":
            case "java.lang.Boolean":
                result =getAsBoolean(value);
                break;
            case "char":
            case "java.lang.Character":
                result =getAsChar(value);
                break;
            case "float":
            case "java.lang.Float":
                result =getAsFloat(value);
                break;
            case "short":
            case "java.lang.Short":
                result =getAsShort(value);
                break;
           case "java.lang.Number":
                result =getAsNumber(value);
                break;
            case "java.lang.String":
                result =getAsString(value);
                break;
            case "java.math.BigDecimal":
                result =getAsBigDecimal(value);
                break;
            case "java.math.BigInteger":
                result =getAsBigInteger(value);
                break;
            case "java.util.Date":
                result =getAsDate(value);
                break;
           case "java.time.temporal.Temporal":
                result =getAsTemporal(value);
                break;
            default:
                return null;
        }
        return result;
    }


}
