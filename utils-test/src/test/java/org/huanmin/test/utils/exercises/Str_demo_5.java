package org.huanmin.test.utils.exercises;

import java.util.Arrays;

/**
 * 获取最长回文子串:
 * 输入: "babad"
 * 输出: "bab"
 * 输入: "cbbd"
 * 输出: "bb"
 * @Author: huanmin
 * @Date: 2022/10/9 16:05
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class Str_demo_5 {
    //求字符串中的最长回文子串 (中心扩散法)
    public  String longestPalindrome(String s) {
        if (s.length()<1){//防止数组越界，如果是空字符串，直接返回空字符串
            return "";
        }
        char[] c = s.toCharArray();
        int start=0,end=0;//记录回文串的开始和结束下标
        for (int i = 0; i < c.length; i++) {
            //以为中心点可能是以一个或者两个字符为中心，所以要判断一个和两个中心的，并取最大长度
            int l1 = ce(s,i,i);//一个字符为中心
            int l2 = ce(s,i,i+1);//两个字符为中心
            int l3 = Math.max(l1,l2);//取最大长度
            if (l3>(end-start)){
                //如果是一个字符为中心，那么长度必定是奇数，长度是否-1并不影响结果
                //如果是两个字符为中心，那么长度必定是偶数，而i是左边的中心字符，所以长度-1再除2能得到开始坐标
                start = i-(l3-1)/2;
                end = i+l3/2;
            }

        }
        //因为substring是左闭右开的，所以end+1
        return s.substring(start,end+1);
    }

    private int ce(String s,int l,int r){//以l和r为中心向两边扩散
        //相等时向两边扩散，要注意边界
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
            l--;
            r++;
        }
        //因为r和l不相等时才结束循环，所以r需要-1，l需要+1
        return (r-1)-(l+1)+1;
    }



    //动态规划法
    public  String longestPalindrome1(String s){
        int n = s.length();
        //db[i][j]:表示从下标为i到j的字符串是否为回文子串
        boolean[][] dp=new boolean[n][n];
        //start记录开始的下标，end记录结束的下标
        int start=0,end=0;
        //初始化为true
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i],true);
        }
        //因为dp[i][j]需要用到dp[i+1][j-1]的结果，所以要倒序遍历
        for (int i=n-1;i>=0;i--){
            for (int j=i+1;j<n;j++){
                dp[i][j]=(s.charAt(i)==s.charAt(j))&&dp[i+1][j-1];
                if (dp[i][j] &&(j-i)>(end-start)){
                    end=j;
                    start=i;
                }
            }
        }
        return s.substring(start,end+1);
    }

    //获取所有子串,然后判断是否是回文串 (效率最低)
    // 下标截取的运动: 01 012  0123 ... 01234  然后在 12 123 ....1234  以此类推
    public static String longestPalindrome2(String s) {
        if (s.length()<1){//防止数组越界，如果是空字符串，直接返回空字符串
            return "";
        }
        char[] c = s.toCharArray();
        int start=0,end=0;//记录回文串的开始和结束下标
        for (int i = 0; i < c.length; i++) {
            for (int j = i+1; j <= c.length; j++) {
                String str = s.substring(i,j);
                if (isHuiWen(str) && str.length()>(end-start)){
                    start=i;
                    end=j;
                }
            }
        }
        return s.substring(start,end);
    }
    public static boolean isHuiWen(String str){
        char[] c = str.toCharArray();
        int i=0,j=c.length-1;
        while (i<j){
            if (c[i]!=c[j]){
                return false;
            }
            i++;
            j--;
        }
        return true;
    }

    public static void main(String[] args) {
        String s = "babad";
        String s1 = longestPalindrome2(s);
        System.out.println(s1);

    }

}
