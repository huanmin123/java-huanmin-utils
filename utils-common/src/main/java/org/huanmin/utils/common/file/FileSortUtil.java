package org.huanmin.utils.common.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 文件列表排序
 */
public class FileSortUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileSortUtil.class);
    private List<File> folder_list=new ArrayList<>();//文件夹
    private List<File> file_list=new ArrayList<>();//文件

    //排序  规则
    //文件夹 按照名称 排序 从短到长
    private   boolean dir_name=false;
    //文件夹 按照大小 排序 从小到大
    private  boolean dir_size=false;
    //文件夹创建时间  排序  从 最近到最远
    private  boolean dir_time=true;

    //文件 按照名称  排序 从短到长
    private  boolean  file_name=false;
    //文件 按照大小  排序 从小到大
    private   boolean file_size=false;
    //文件创建时间  排序  从 最近到最远
    private  boolean file_time=true;

    //可以同时多开 排序规则  不会冲突


    // 是否包括子文件
    private boolean  pddirZi=true;





    public  void  pathFile(String path){
        File file=new File(path);
        if (!file.exists()){
            System.out.println("找不到路径");
            return;
        }
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.isDirectory()){
                folder_list.add(file1);
                if(pddirZi){
                    pathFile(file1.getPath());
                }

            }
            if (file1.isFile()){
                file_list.add(file1);
            }
        }


    }

    public  void  compare_lis() {
        if (dir_name) {
            //文件夹按文件名称的长度 显示的顺序：  从短到 长
            Collections.sort(folder_list, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    File o11 = (File) o1;
                    File o22 = (File) o2;
                    return (o11.getName().length() - o22.getName().length());
                }

            });

        }

        if (file_name) {
            //按文件名称的长度 显示的顺序：  从短到 长
            Collections.sort(file_list, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    File o11 = (File) o1;
                    File o22 = (File) o2;
                    return (o11.getName().length() - o22.getName().length());
                }

            });

        }


        if (dir_size) {
            //按照文件夹大小 再次排序  最大的文件 放在最后
            Collections.sort(folder_list, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    File o11 = (File) o1;
                    File o22 = (File) o2;
                    return (int) (o11.length() - o22.length());
                }
            });
        }


        if (file_size) {
            //按照文件大小 再次排序  最大的文件 放在最后
            Collections.sort(file_list, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    File o11 = (File) o1;
                    File o22 = (File) o2;
                    return (int) (o11.length() - o22.length());
                }
            });
        }
        if (dir_time) {
            //然后文件夹创建时间 排序 从近到远
            Collections.sort(folder_list, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    File o11 = (File) o1;
                    File o22 = (File) o2;
                    return (int) ( o22.lastModified()-o11.lastModified());
                }
            });
        }

        if(file_time){
            //然后文件大小 再次排序  最大的文件 放在最后
            Collections.sort(file_list, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    File o11 = (File) o1;
                    File o22 = (File) o2;
                    return (int) ( o22.lastModified()-o11.lastModified());
                }
            });
        }





    }


    public void dir_file_list(String path,boolean px){
        pathFile(path);
        if (px){
            compare_lis();//排序
        }
    }

    public List<File> getFolder_list() {
        return folder_list;
    }
    public List<File> getFile_list() {
        return file_list;
    }

    public FileSortUtil() {
    }

    /**
     *
     * @param dir_name   文件夹名称排序
     * @param dir_size   文件夹大小排序
     * @param dir_time   文件夹创建时间排序
     * @param file_name  文件名称排序
     * @param file_size  文件大小排序
     * @param file_time  文件创建时间排序
     */
    public FileSortUtil(boolean dir_name, boolean dir_size, boolean dir_time, boolean file_name, boolean file_size, boolean file_time) {
        this.dir_name = dir_name;
        this.dir_size = dir_size;
        this.dir_time = dir_time;
        this.file_name = file_name;
        this.file_size = file_size;
        this.file_time = file_time;
    }

    //查看指定路径下所有的文件和文件夹名称，并且指定排序规则进行排序
    public static void main(String[] args) {
        //如何排序自己根据构造来修改
        FileSortUtil list=new FileSortUtil();
        String path = "C:\\Users\\12841\\Desktop\\Docker";
        boolean px=true; //开启排序
        list.dir_file_list(path,px);
        System.out.println("所有的文件夹" + list.getFolder_list()); //排序规则:  创建时间  排序  从 最近到最远
        System.out.println("所有的文件" + list.getFile_list()); //排序规则了:   创建时间  排序  从 最近到最远

    }
}
