package com.utils.common.enums;

public enum DateEnum {

    /**
     * 显示年月日时分秒，例如 2015-08-11 09:51:53.
     */
    DATETIME_PATTERN("yyyy-MM-dd HH:mm:ss"),
    DATETIME_T_PATTERN("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"),
    /**
     * 仅显示年月日，例如 2015-08-11.
     */
    DATE_PATTERN("yyyy-MM-dd"),

    /**
     * 仅显示时分秒，例如 09:51:53.
     */
    TIME_PATTERN("HH:mm:ss"),
    /**
     * 显示年月日时分秒(无符号)，例如 20150811095153.
     */
    UNSIGNED_DATETIME_PATTERN("yyyyMMddHHmmss"),
    /**
     * 仅显示年月日(无符号)，例如 20150811.
     */
    UNSIGNED_DATE_PATTERN ("yyyyMMdd");
    /**
     * 显示年月日时分秒，例如 2015-08-11 09:51:53.
     */

    private  String value;

    DateEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
