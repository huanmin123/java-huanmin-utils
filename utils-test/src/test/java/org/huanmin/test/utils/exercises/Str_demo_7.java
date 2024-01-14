package org.huanmin.test.utils.exercises;

/**
 * 把字符串转换成整数 (要求不能使用字符串转换整数的库函数)
 *
 * @Author: huanmin
 * @Date: 2022/10/10 11:25
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class Str_demo_7 {

    //把字符串转换成整数 (要求不能使用字符串转换整数的库函数)
    public static int strToInt(String str) {
        //1.将字符串转换为字符数组
        char[] chars = str.toCharArray();
        //2.判断是否存在符号位
        int flag = 0;
        if (chars[0] == '+') {
            flag = 1;
        } else if (chars[0] == '-') {
            flag = 2;
        }
        int start = flag > 0 ? 1 : 0;
        int result = 0;//保存结果
        for (int i = start; i < chars.length; i++) {
            //3.判断字符是否为数字
            if (chars[i] >= '0' && chars[i] <= '9') {
                //4.如果是数字，将数字转换为整数
                int num = chars[i] - '0';
                //5.将整数累加到结果中
                result = result * 10 + num;
            } else {
                //6.如果不是数字，抛出异常
                throw new RuntimeException("字符串中包含非数字字符");
            }
        }
        return  flag == 1 ? result : -result;

    }

    public static void main(String[] args) {
        String str = "-123";
        int num = strToInt(str);
        System.out.println(num);
    }


}
