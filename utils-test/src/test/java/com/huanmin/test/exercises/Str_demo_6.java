package com.huanmin.test.exercises;

/**
 * 括号匹配深度
 *
 * @Author: huanmin
 * @Date: 2022/10/10 11:18
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class Str_demo_6 {
    //括号匹配深度
    public static int maxDepth(String s) {
        //1.将字符串转换为字符数组
        char[] chars = s.toCharArray();
        //2.定义一个变量，用于记录左括号的个数
        int count = 0;
        //3.定义一个变量，用于记录最大的左括号个数
        int max = 0;
        //4.循环遍历字符数组
        for (int i = 0; i < chars.length; i++) {
            //5.判断字符是否为左括号
            if (chars[i] == '(') {
                //6.如果是左括号，左括号个数加1
                count++;
                //7.判断左括号个数是否大于最大左括号个数
                if (count > max) {
                    //8.如果大于，将左括号个数赋值给最大左括号个数
                    max = count;
                }
            }
            //9.判断字符是否为右括号
            if (chars[i] == ')') {
                //10.如果是右括号，左括号个数减1
                count--;
            }
        }
        //11.返回最大左括号个数
        return max;
    }

    public static void main(String[] args) {
        String s = "(1+(2*3)+(  (8)/4)  )+1";
        int max = maxDepth(s);
        System.out.println(max);
    }
}
