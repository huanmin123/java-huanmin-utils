package org.huanmin.test.utils.exercises;

/**
 * 验证回文串:
 *
 输入: "A man, a plan, a canal: Panama"
 输出: true
 我们可以构造的最长的回文串是"dccaccd", 它的长度是 7。
 输入: "race a car"
 输出: false
 * @Author: huanmin
 * @Date: 2022/10/9 16:00
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class Str_demo_4 {

    //验证回文串
    public static boolean isPalindrome(String s) {
        //1.将字符串转换为小写
        String str = s.toLowerCase();
        //2.去除字符串中的空格
        str = str.replaceAll("\\s", "");
        //3.去除字符串中的标点符号
        str = str.replaceAll("\\pP", "");
        //4.将字符串转换为字符数组
        char[] chars = str.toCharArray();
        //5.定义两个指针，分别指向字符串的首尾
        int i = 0;
        int j = chars.length - 1;
        //6.循环遍历字符串，判断首尾字符是否相等
        while (i < j) {
            if (chars[i] != chars[j]) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }
}
