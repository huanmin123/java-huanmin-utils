package com.huanmin.test.exercises;

import java.util.Arrays;

/**
 * 最长公共前缀:
 *
 * 编写一个函数来查找字符串数组中的最长公共前缀。如果不存在公共前缀，返回空字符串 “”。
 * 输入: ["flower","flow","flight"]
 * 输出: "fl"
 *
 * 输入: ["dog","racecar","car"]
 * 输出: ""
 * 解释: 输入不存在公共前缀。
 * 将字符串数组的元素按照长度进行排序，取最短的字符串，然后遍历这个字符串，
 * 将这个字符串的每个字符与其他字符串的对应位置的字符进行比较，如果相等则继续比较，否则返回结果
 * @Author: huanmin
 * @Date: 2022/10/9 15:49
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class Str_demo_3{
    //最长公共前缀,如果没有返回空
    public static String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) {
            return "";
        }
        //将字符串数组按照长度进行排序
        Arrays.sort(strs);
        //取最短的字符串
        String firstStr = strs[0];
        //遍历最短的字符串
        for (int i = 0; i < firstStr.length(); i++) {
            //取最短字符串的每个字符
            char c = firstStr.charAt(i);
            //遍历其他字符串
            for (int j = 1; j < strs.length; j++) {
                //如果其他字符串的长度小于最短字符串的长度，或者其他字符串的对应位置的字符不等于最短字符串的对应位置的字符
                if (strs[j].length() < i || strs[j].charAt(i) != c) {
                    //返回最短字符串的前i个字符
                    return firstStr.substring(0, i);
                }
            }
        }
        //如果所有字符串的对应位置的字符都相等，那么返回最短字符串
        return firstStr;
    }


    public static void main(String[] args) {
        String s = longestCommonPrefix(new String[]{"flower", "flow", "flight"});
        System.out.println(s);
        String s1 = longestCommonPrefix(new String[]{"1flower", "2flow", "3light"});
        System.out.println(s1);
    }
}
