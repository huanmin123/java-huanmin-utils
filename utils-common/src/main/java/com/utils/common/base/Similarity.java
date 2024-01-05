package com.utils.common.base;


/**
 * 对比字符串相似度
 * @Description:
 * 这个相似度判断的是,两个句子中相同的字的百分比,而不是句子的意思(中文博大精深没法比较意思)
 * 比如 句子1:  1234  句子2: 12  那么 他们相似度 是百分之50
 * 相似度大于 10 那么就有可能     是相同类型的句子
 * 相似度 在50以上 那么基本可以认定 是相同类型的句子
 * 相似度 在90以上那么  基本可以认定 是相同类型的句子  而且只差几个字    长度不同 差的字数也不同
 * 相似度在百分之
 * 注意 比的是类型而不是意思 ,比如: 你好  和  你真好   这两个句子的意思就不一样 但是类型是一样的
 */




public class Similarity {



    public static float getSimilarityRatio(String str, String target) {

        int[][] d; // 矩阵
        int n = str.length();
        int m = target.length();
        int i; // 遍历str的
        int j; // 遍历target的
        char ch1; // str的
        char ch2; // target的
        int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0 || m == 0) {
            return 0;
        }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++) { // 初始化第一列
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) { // 初始化第一行
            d[0][j] = j;
        }

        for (i = 1; i <= n; i++) { // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2 + 32 || ch1 + 32 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1), d[i - 1][j - 1] + temp);
            }
        }

        return (1 - (float) d[n][m] / Math.max(str.length(), target.length())) * 100F;
    }

}
