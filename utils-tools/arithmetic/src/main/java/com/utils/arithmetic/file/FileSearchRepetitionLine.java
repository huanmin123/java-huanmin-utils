package com.utils.arithmetic.file;

import com.utils.common.file.ResourceFileUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 1. 将文件内容通过hash算法取余分别存储到N个小文件中,每个文件大小为1M大概1000行(必须小于可用内存大小)
 * 2. 通过hash算法取余查找对应的小文件
 * 3. 因为会存在hash冲突问题所以,还需要遍历这个小文件判断重复行
 * 4. 通过上述方式查询时间复杂度为O(2),而且也只需1次i/o即可
 */
public class FileSearchRepetitionLine {
    public String cutFilePath= ResourceFileUtil.getCurrentProjectAbsolutePath()+"\\cutFile\\";
    private  String cutSuffix=".cut";
    private  Integer cutNum=100; //将文件分成几个小文件(建议一个小文件大小为1M,需要根据实际情况调整)
    /**
     * 通过hash算法取余查找对应的小文件,并且将行写入到对应的小文件中
     * @param filePath 文件路径
     */
    public void cutFile(String filePath){
        //按行读取文件
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));) {
            String line =null;
            while ((line = br.readLine()) != null) {
                int i = line.hashCode() % cutNum;
                String cutfilePath = cutFilePath + Math.abs(i) + cutSuffix;
                File file = new File(cutfilePath);
                if(!file.exists()){
                    file.getParentFile().mkdirs();
                }
                BufferedWriter bw =
                        new BufferedWriter(
                                new OutputStreamWriter(new FileOutputStream(cutfilePath, true)));
                bw.write(line+System.lineSeparator());
                bw.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    /**
     * 通过hash算法取余查找对应的小文件,并且遍历这个小文件判断重复行
     * @param filePath 原文件路径
     *  @return 重复行 ,返回null表示没有重复行
     */
    
    public  String  getRepetitionLine(String filePath){
        
        String line =null;
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));) {
            while ((line = br.readLine()) != null) {
                int i = line.hashCode() % cutNum;
                String cutfilePath = cutFilePath + Math.abs(i) + cutSuffix;
                BufferedReader cutBr =
                        new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(cutfilePath))));
                String cutLine =null;
                int count=0;
                while ((cutLine = cutBr.readLine()) != null) {
                    if(line.equals(cutLine)){
                        count++;
                        if(count>1){//重复行
                            return line;
                        }
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {

    }
}
