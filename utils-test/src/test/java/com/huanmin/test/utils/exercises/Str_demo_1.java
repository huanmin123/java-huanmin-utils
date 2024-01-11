package com.huanmin.test.utils.exercises;

import java.util.Arrays;

/**
 * 循环右移位数n，例如，s = 'abc',n=1 返回 ‘cab’ ，解释：abc每个字母往右移动移1位，c移动过后超出长度，回到第一位。
 * @Author: huanmin
 * @Date: 2022/10/9 15:37
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class Str_demo_1 {
    public static void main(String[] args) {
        char[] abcs = strToMoveByNum("abc", 4);
        System.out.println(Arrays.toString(abcs));
    }

    /**
     * @param str 需要移动的字符串
     * @param num 需要移动的位数
     */
    public static char[] strToMoveByNum(String str, int num) {
        char[] chars = str.toCharArray();//获取字符串的字符数组
        char[] newChars = new char[chars.length];//创建一个新的字符数组
        int length = chars.length;//获取字符数组的长度
        int start = length - num;  //从什么位置开始取
        if(start<0){ //如果开始位置小于0,那么表示需要从头开始取
            start = length + start;
        }

        //先取后几个
        for (int i = start; i < length; i++) {//遍历字符数组
            newChars[i - start] = chars[i];//将字符数组中的元素赋值给新的字符数组
        }
        if(num>length){//如果移动的位数大于字符数组的长度
            num = num - length;
        }
        //然后加上前面的
        for (int i = 0; i < start; i++) {
            newChars[i + num] = chars[i];
        }
        return newChars;
    }
}
