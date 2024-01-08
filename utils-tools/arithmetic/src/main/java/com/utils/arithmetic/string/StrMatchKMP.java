package com.utils.arithmetic.string;

/**
 * 字符串匹配的KMP算法 (最快匹配法)
 *
 * @author huanmin
 */
public class StrMatchKMP {

    public static int indexOfStrKMP(String iniStr, String patternStr) {
        if (iniStr == null || iniStr.length() <= 0 ||
                patternStr == null || patternStr.length() <= 0) {
            return -1;
        }

        // 得到模式串的next数组
        int[] nextArr = getNextArray(patternStr);
        int iniLength = iniStr.length();
        int patternLength = patternStr.length();
        int i = 0;  // 原始串索引
        int j = 0;  // 模式串索引
        while (i < iniLength && j < patternLength) {
            if (j == -1 || iniStr.charAt(i) == patternStr.charAt(j)) {
                i++;
                j++;
            } else {
                //匹配失败,取下一个匹配位置(i不动,调整j的匹配位置)
                j = nextArr[j];
            }
        }
        if (j == patternLength) {
            return i - patternLength;
        } else {
            return -1;
        }
    }

    /**
     * 获取模式字符串的next数组
     *
     * @param str
     * @return
     */
    private static int[] getNextArray(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        int length = str.length();
        int[] nextArr = new int[length];
        nextArr[0] = -1;//跳过第一个

        //j>k  永远k比j少1
        int j = 0;
        int k = -1;
        while (j < length - 1) {
            //等于-1  或者 j=k
            if (k == -1 || str.charAt(j) == str.charAt(k)) {
                // 匹配，移动
                j++;
                k++;
                if (str.charAt(j) == str.charAt(k)) {
                    //判断移动之后,的字符串是否相等,如果相等那么j从上一个k位置进行匹配(k<j)
                    nextArr[j] = nextArr[k];
                } else {
                    //如果不相等那么j,从k的位置开始匹配
                    nextArr[j] = k;
                }
            } else {
                //算出最大长度
                k = nextArr[k];
            }
        }
        return nextArr;
    }

    // BF 方式
    // hrabcpp abcdefg   hrabcpp abcdefg   hrabcpp abcdefg  hrabcpp abcdefg       .....       hrabcpp abcdefg
    // abcdefg            abcdefg             abcdefg           abcdefg           .....               abcdefg




    //KMP方式    i指针永远不回头 ,j指针回头
    // j>k  永远k比j少1
    //  abcdefg   abcdefg         abcdefg        abcdefg      abcdefg         abcdefg          (j)
    //   abcdefg    abcdefg          abcdefg         abcdefg       abcdefg          abcdefg    (k)

    //abcdefg  编码 [-1, 0, 0, 0, 0, 0, 0]

    //  aaabbcd   aaabbcd         aaabbcd        aaabbcd      aaabbcd         aaabbcd          (j)
    //   aaabbcd    aaabbcd          aaabbcd         aaabbcd       aaabbcd          aaabbcd    (k)

    //aaabbcd  编码 [-1, -1, -1, 2, 0, 0, 0]

    //hello aaabbcd word   hello aaabbcd word   hello aaabbcd word   hello aaabbcd word    hello aaabbcd word  hello aaabbcd word  hello aaabbcd word
    //aaabbcd               aaabbcd               aaabbcd               aaabbcd                aaabbcd              aaabbcd              aaabbcd


    //使用编码表进行匹配

    //aaahello aaabbcd    aaahello aaabbcd   aaahello aaabbcd   aaahello aaabbcd    aaahello aaabbcd
    //aaabbcd              aaabbcd             aaabbcd             aaabbcd           aaabbcd

    //aaahello aaabbcd     aaahello aaabbcd   aaahello aaabbcd   aaahello aaabbcd   aaahello aaabbcd   aaahello aaabbcd
    //    aaabbcd               aaabbcd             aaabbcd             aaabbcd             aaabbcd             aaabbcd





}