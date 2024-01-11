package com.huanmin.utils.arithmetic.string;

/**
 * 字符串模式匹配的 Brute-Force算法（暴力法）
 * @author huanmin
 *
 */

public class StrMatchBF {

    /**
     * 匹配字符串
     * @param iniStr 原始字符串
     * @param patternStr 模式字符串
     * @return 如果模式字符串在原始字符串中存在，返回模式字符串在原始字符串中第一次出现的索引。
     */
    public int indexOfStr(String iniStr, String patternStr) {
        if(iniStr == null || iniStr.length() <= 0 ||
                patternStr == null || patternStr.length() <=0) {
            return -1;
        }

        int iniLength = iniStr.length();
        int patternLength = patternStr.length();
        int i = 0, j = 0; //两个字符的起始下标
        while(i < iniLength && j < patternLength) {
            //比较两个字符串的同位置的元素
            if(iniStr.charAt(i) == patternStr.charAt(j)) {
                i++;
                j++;
            } else {
                // 失配，回退 下一个索引位置
                i = i - j + 1;
                     //匹配字符串从0开始
                j = 0;
            }
        }
        //检测是否完全匹配完成
        if(j == patternLength) {
            //计算下标(模式字符串在原字符串的开始位置)
            return i - patternLength;
        } else {
            return -1;
        }
    }
}
