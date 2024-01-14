package org.huanmin.utils.common.file;

import org.huanmin.utils.common.base.UniversalException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/11/24 19:50
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class ExeUtil {
    // 修改exe文件图标
    public static void changeIcon(String exePath, String iconPath) {
        String[] cmd = new String[4];
        cmd[0] = "cmd.exe";
        cmd[1] = "/C";
        cmd[2] = "start";
        cmd[3] = " /b " + exePath + " " + iconPath;
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
             UniversalException.logError(e);
        }
    }
    //exe生成快捷方式
    public static void createShortCut(String exePath, String shortCutPath) {
        String[] cmd = new String[4];
        cmd[0] = "cmd.exe";
        cmd[1] = "/C";
        cmd[2] = "start";
        cmd[3] = " /b " + exePath + " " + shortCutPath;
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
             UniversalException.logError(e);
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool customThreadPool = new ForkJoinPool();
        List<Integer> list=new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
        long actualTotal = customThreadPool.submit(() -> list.parallelStream().reduce(0, Integer::sum)).get();
        System.out.println(actualTotal);

//        changeIcon("D:\\test\\test.exe", "D:\\test\\test.ico");
//        createShortCut("D:\\test\\test.exe", "D:\\test\\test.lnk");
    }




}
