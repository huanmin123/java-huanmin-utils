package org.huanmin.utils.arithmetic.file;


import org.huanmin.utils.common.base.DataGroup;
import org.huanmin.utils.common.base.NullUtil;
import org.huanmin.utils.common.file.FileTool;
import org.huanmin.utils.common.file.FileUtil;
import org.huanmin.utils.common.file.ReadFileLineUtil;
import org.huanmin.utils.common.file.ResourceFileUtil;
import org.huanmin.utils.common.multithreading.executor.ThreadFactoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 1. 将文件内容通过hash算法取余分别存储到N个小文件中,每个文件大小为1M大概1000行(必须小于可用内存大小)
 * 2. 通过hash算法取余查找对应的小文件
 * 3. 因为会存在hash冲突问题所以,还需要遍历这个小文件判断重复行
 * 4. 通过上述方式查询时间复杂度为O(2),而且也只需1次i/o即可
 */
public class FileNumberSortSection {
    private static final Logger logger = LoggerFactory.getLogger(FileNumberSortSection.class);
    public String cutFilePath = ResourceFileUtil.getCurrentProjectAbsolutePath() + "\\cutFile\\";
    private String cutSuffix = ".cut";
    private Integer maxVaue = 10_000_000; //已知文件内最大值
    private Integer cutSection = 10_000;  //递增区间
    private String numSplit = ",";  //数值分隔符
    private  List<long[]> longs;
    private AtomicInteger busyThreadNum=new AtomicInteger(0); //忙碌线程数
    private  Map<String,BufferedWriter> bufferedWriterMap=new ConcurrentHashMap<>(); //文件写入对象
    
    public FileNumberSortSection() {
         longs = DataGroup.dataGroupOnce(maxVaue, cutSection);//将数据分区
         new File(cutFilePath).mkdirs();
    }
    
    /**
     * 通过hash算法取余查找对应的小文件,并且将行写入到对应的小文件中
     *
     * @param filePath 文件路径
     */
    public void cutFile(String filePath) {
       
        List<Integer> nums = new ArrayList<>();
        FileTool.readByteNumSplit(Paths.get(filePath).toFile(), numSplit, (num) -> {
            nums.add(num);
            if (nums.size() == 1000) {//每1000数值处理一次
                Integer[] newNums = nums.toArray(new Integer[nums.size()]);
                nums.clear();
                writerNumber(newNums);
            }
        });
        if (!nums.isEmpty()) {//处理剩余的行
            Integer[] newNums = nums.toArray(new Integer[nums.size()]);
            nums.clear();
            writerNumber(newNums);
        }
        while (busyThreadNum.get()!=0){//等待所有线程执行完毕,不然文件可能还没写完就关闭了,会导致报错或者异常情况
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("busyThreadNum:"+busyThreadNum.get());
        }
        bufferedWriterMap.forEach((k,v)->{
            try {
                v.flush();
                v.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bufferedWriterMap.clear();
    }
    
    private void writerNumber(Integer[] nums) {
        busyThreadNum.incrementAndGet();
        ThreadPoolExecutor executor = ThreadFactoryUtil.getExecutor(ThreadFactoryUtil.ThreadConfig.FileNumberSortSection);
        executor.execute(()->{
            for (Integer num : nums) {
                Integer vSection = DataGroup.getVSection(longs, num);
                String cutfilePath = cutFilePath + vSection + cutSuffix;
                synchronized (longs.get(vSection)) {//锁住对应的文件,防止多线程写入,导致追加写入,内容错乱
                    try {
                        BufferedWriter bufferedWriter = bufferedWriterMap.get(cutfilePath);
                        if (bufferedWriter==null){
                            bufferedWriter= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cutfilePath, true)));
                            bufferedWriterMap.put(cutfilePath,bufferedWriter);
                        }
                        bufferedWriter.write(num + numSplit);
                        bufferedWriter.flush();//每次写入都刷新一次,防止数据丢失

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            busyThreadNum.decrementAndGet();
        });
    }
    
    /**
     * 通过hash算法取余查找对应的小文件,并且遍历这个小文件判断重复行
     *
     * @param filePath 原文件路径
     * @param colCount     列个数
     * @return 重复行 ,返回null表示没有重复行
     */
    
    public String getSort(String filePath,int colCount) {
        try (BufferedWriter bw =
                     new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true)));) {
            List<String> filesAll = FileUtil.getFilesAll(cutFilePath);
            //按文件序号排序(从小到大)
            List<String> collect = filesAll.stream().sorted((a, b) -> fileId(a) - fileId(b)).collect(Collectors.toList());
            StringJoiner sj = new StringJoiner(",");
            int count = colCount;
            for (String file : collect) {
                String strAll = ReadFileLineUtil.readFileStrAll(new File(file));

                List<Integer> nums=new ArrayList<>();
                Arrays.stream(strAll.split(numSplit)).forEach(v->{
                    if(NullUtil.notEmpty(v)){
                        nums.add(Integer.parseInt(v));
                    }
                });
                //排序,从小到大
                Collections.sort(nums);
                for (Integer v : nums) {
                    sj.add(v.toString());
                    count--;
                    if (count==0){
                        bw.write(sj.toString());
                        bw.newLine();
                        count = colCount;
                        sj = new StringJoiner(",");
                    }
                }
            }
            if (NullUtil.notEmpty(sj.toString())){
                bw.write(sj.toString());
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    //获取文件序号
    private int fileId(String fileSliceName) {
        String name = new File(fileSliceName).getName();
        int index=name.indexOf(cutSuffix);
        String substring = name.substring(0, index);
        return Integer.parseInt(substring);
    }

 
}
