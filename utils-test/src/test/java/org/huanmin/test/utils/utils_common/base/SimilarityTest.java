package org.huanmin.test.utils.utils_common.base;





import org.huanmin.utils.common.base.Similarity;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimilarityTest {
    private static final Logger logger = LoggerFactory.getLogger(SimilarityTest.class);
    @Test
    public  void test1(){
        String str1="晚上大家好";
        String str2="大家晚上好";

        float similarityRatio = Similarity.getSimilarityRatio(str1, str2);
        System.out.println(similarityRatio);  //19.999998
    }


    @Test
    public  void test2(){
        String str1="亲爱的朋友们，大家早上好";
        String str2="亲爱的朋友们，大家早上好";

        float similarityRatio = Similarity.getSimilarityRatio(str1, str2);
        System.out.println(similarityRatio);  //91.66667
    }
    @Test
    public  void test3(){
        String str1="家晚上好，本书作者以娓娓而谈的文笔和行云流水般的故事";
        String str2="亲爱的朋友们，大家晚上好";

        float similarityRatio = Similarity.getSimilarityRatio(str1, str2);
        System.out.println(similarityRatio);  //3.8461566
    }
    @Test
    public  void test4(){
        String str1="家晚";
        String str2="家晚上好";

        float similarityRatio = Similarity.getSimilarityRatio(str1, str2);
        System.out.println(similarityRatio);  //50.0
    }



}
