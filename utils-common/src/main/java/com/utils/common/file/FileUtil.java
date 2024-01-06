package com.utils.common.file;


import com.utils.common.multithreading.executor.ExecutorUtil;
import com.utils.common.multithreading.executor.ThreadFactoryUtil;
import com.utils.common.string.StringUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 文件处理(改名,移动,复制,删除,信息获取)
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * @param dirPath 目录路径
     * @return 返回文件列表
     * 获取指定路径下所有文件, 包括子目录
     */
    public static List<String> getFilesAll(String dirPath) {
        List<String> list = new ArrayList<>();
        getFiles(dirPath, list);
        return list;
    }
    //搜索指定目录下所有正则表达式匹配的文件
    public static List<String> getFilesAll(String dirPath,String regex) {
        List<String> list = new ArrayList<>();
        List<String> filesAll = getFilesAll(dirPath);
        //正则表达式匹配
        for (String s : filesAll) {
            if(s.matches(regex)){
                list.add(s);
            }
        }
        return list;
    }
    //找指定目录下的指定文件
    public static List<String> findName(String dirPath,String fileName) {
        List<String> list = new ArrayList<>();
        List<String> filesAll = getFilesAll(dirPath);
        //正则表达式匹配
        for (String path : filesAll) {
            File   file =new File(path);
            if(file.getName().equals(fileName)){
                list.add(path);
            }
        }
        return list;
    }

    private static void getFiles(String dirPath, List<String> list) {
        File file = new File(dirPath);
        if (!file.exists()) {
            throw new RuntimeException("文件目录不存在");
        }
        //获取当前目录文件集合
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {//判断是否是目录
                getFiles(files[i].getPath(), list);
            } else {
                list.add(files[i].getAbsolutePath());
            }

        }
    }

    /**
     * @param dirPath 目录路径
     * @return 返回文件路径列表
     * 获取指定目录下全部文件, 不包括子目录
     */
    public static List<String> getFiles(String dirPath) {
        List<String> list = new ArrayList<>();
        File file = new File(dirPath);
        if (!file.exists()) {
            throw new RuntimeException("文件目录不存在");
        }
        //获取当前目录文件集合
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {//判断是否是目录
            } else {
                list.add(files[i].getAbsolutePath());
            }
        }
        return list;
    }


    /**
     * 删除指定目录下的全部文件, 包括子目录里的文件   ,直到删除为止
     *
     * @param derPath 文件夹路径
     * @param pd      true 根目录也删除 ,false不删除根目录
     */
    public static void delFilesAllReview(String derPath, boolean pd ) {
        delFilesAll(derPath, pd); //删除

        // 复查
        ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.FileUtil, () -> {
            int count = 0;
            try {
                while (true) {
                    count++;
                    if (count > 100) {
                        System.out.println("删除文件失败,此文件被长期占用,请手动删除");
                        break;
                    }
                    Thread.sleep(1000);
                    System.gc(); //垃圾回收,防止文件被占用,无法删除  一般2~3秒就可以删除完毕 ,如果超过10秒,则说明文件被占用,无法删除
                    File delfile = new File(derPath);
                    if (delfile.exists() && delfile.isFile()) {
                        System.out.println("文件还在:"+delfile.getAbsolutePath()+" ,等待删除");
                        delFilesAll(derPath, pd);
                        continue;
                    }
                    if (delfile.exists() && delfile.isDirectory() && delfile.listFiles().length > 0) {
                        System.out.println("文件夹还在:"+delfile.getAbsolutePath()+" ,等待删除");
                        delFilesAll(derPath, pd);
                        continue;
                    }
                    //都删除了
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static boolean delFilesAll(String derPath, boolean pd) {
        File delfile = new File(derPath);
        if (!delfile.exists()) {
            System.out.println("要删除的文件或文件不存在");
            return false;
        }
        try {
            if (delfile.isFile()) {
                delfile.delete();
                return true;
            } else {
                File[] files = delfile.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        delFilesAll(files[i].getPath(), pd);
                    }
                    files[i].delete();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //根文件夹也 一起删除
        if (pd) {
            delfile.delete();
        }
        return true;
    }
    public static boolean delFilesMultAll(String derPath, boolean pd, ExecutorService executorService,List< Future<?>> list) {
        File delfile = new File(derPath);
        if (!delfile.exists()) {
            System.out.println("要删除的文件或文件不存在");
            return false;
        }
        try {

            if (delfile.isFile()) {
                delfile.delete();
                return true;
            } else {
                File[] files = delfile.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        int finalI = i;
                        Future<?> submit = executorService.submit(() -> {
                            delFilesMultAll(files[finalI].getPath(), pd, executorService,list);
                            files[finalI].delete();
                        });
                        list.add(submit);
                    }else {//文件
                        files[i].delete();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //根文件夹也 一起删除
        if (pd) {
            delfile.delete();
        }
        return true;
    }


    //同步删除文件,一般10~20次就可以删除了,如果还是删除不了,那么就是文件被占用了,无法删除
    public static void delFileGc(File file) {
            int tryCount = 0;
            while (tryCount++ < 20) {
                if (file.delete()) {
                    return ;
                }
                System.gc();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }
    //异步删除文件,一般10~20次就可以删除了,如果还是删除不了,那么就是文件被占用了,无法删除
    public static void delFileGcAsync(File file) {
        ExecutorUtil.create(ThreadFactoryUtil.ThreadConfig.FileUtil, () -> {
            int tryCount = 0;
            while (tryCount++ < 20) {
                if (file.delete()) {
                    return;
                }
                System.gc();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * suffix ,suffixAndSpot   获取文件后缀(加.和不加.),
     * prefixName       获取文件前缀(不加路径,不加后缀) 比如c:\\xx\xx\helloween.text     结果: helloween
     * prefixPath        提取文件的当前所在的目录的路径 ,不包括文件名称
     * prefixPathAndName 提取文件的当前所在的目录的路径 ,包括文件,不包括后缀
     *
     * @param pathFile
     * @return
     */
    @SneakyThrows
    public static String filePartInfo(String pathFile, String type) {
        String result = null;
        File str1 = new File(pathFile);
        String absolutePath = str1.getAbsolutePath();
        int i = absolutePath.lastIndexOf(File.separator) + 1;
        switch (type) {
            case "suffix":
                //获取后缀 不加点 比如 txt   js    png
                result = str1.getPath().substring(str1.getPath().lastIndexOf(".") + 1);
                break;
            case "suffixAndSpot":
                //获取后缀 加点 比如 .txt   .js    .png
                result = str1.getPath().substring(str1.getPath().lastIndexOf("."));
                break;
            case "prefixName":
                //提取文件前缀  ,比如c:\\xx\xx\helloween.text     结果: helloween
                String substring = absolutePath.substring(i);
                result = substring.substring(0, substring.lastIndexOf("."));
                break;
            case "currentDirectory":  //prefixPath
                //提取文件的当前所在的目录的路径 ,不包括文件名称   比如c:\\xx\xx\helloween.text  ,结果:  c:\\xx\xx\
                result = absolutePath.substring(0, i);
                break;
            case "parentDirectory":
                //提取当前文件的父级目录
                result = str1.getParent();
                break;
            case "parentDirectoryName":
                //提取当前文件的父级目录,的名称,不含路径   比如c:\\aaa\bbb\helloween.text  结果bbb
                result = str1.getParent().substring(str1.getParent().lastIndexOf(File.separator) + 1);
                break;
            case "prefixPathAndName":
                //提取文件的当前所在的目录的路径 ,包括文件名称,不包括后缀   比如c:\\xx\xx\helloween.text  ,结果:  c:\\xx\xx\helloween
                String substring1 = absolutePath.substring(i);
                substring1.substring(0, substring1.lastIndexOf("."));
                String substring2 = absolutePath.substring(0, i);
                result = substring2 + substring1;
                break;
            default:
                throw new Exception("类型错误,请输入正确的类型");

        }

        return result;
    }


    /**
     * 复制文件
     *
     * @param oldPath 旧文件         src\main\test\a.txt
     * @param newPaht 新文件地址     src\main\test\bv\a.txt
     * @param pd      true 移动文件  false 复制文件
     */
    public static void copyOrMovefile(String oldPath, String newPaht, boolean pd) {
        File file = new File(oldPath);
        if (!file.exists()) {
            System.out.println("文件路径不存在");
            return;
        }
        if (file.isFile()) {
            try (
                    FileInputStream fis1 = new FileInputStream(oldPath);
                    BufferedInputStream fis = new BufferedInputStream(fis1);
                    FileOutputStream fos1 = new FileOutputStream(newPaht);
                    BufferedOutputStream fos = new BufferedOutputStream(fos1);
            ) {
                byte[] data = new byte[4096];
                int num;
                while ((num = fis.read(data)) != -1) {
                    fos.write(data, 0, num);//写入数据
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("这不是文件类型 ");
        }
        //删除文件
        if (pd) {
            file.delete();
        }
    }

    /**
     * 重命名文件
     *
     * @param oldPath  文件路径            C:\xxx\xx\a.txt         C:\xxx\aaa
     * @param fileName 重命名的文件名称       a.txt ->  b.txt        aaa->bbb
     */
    public static void reFileOrDirectoryName(String oldPath, String fileName) {
        //想命名的原文件的路径
        File file = new File(oldPath);
        //获取源文件路径 不带文件名
        String rootPath = file.getParent();
        //拼接成新的路径(重命名)
        String fileName1 = rootPath + File.separator + fileName;
        //将原文件更改为  fileName
        file.renameTo(new File(fileName1));
    }


    // 复制某个目录及目录下的所有子目录和文件到新文件夹
    private static void copyFolder(String oldPath, String newPath) {
        try {
            // 如果文件夹不存在，则建立新文件夹
            File f = new File(newPath);
            if (!f.exists()) {
                f.mkdirs();
            }
            // 读取整个文件夹的内容到file字符串数组，下面设置一个游标i，不停地向下移开始读这个数组
            File filelist = new File(oldPath);
            String[] file = filelist.list();
            // 要注意，这个temp仅仅是一个临时文件指针
            // 整个程序并没有创建临时文件
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                // 如果oldPath以路径分隔符/或者\结尾，那么则oldPath/文件名就可以了
                // 否则要自己oldPath后面补个路径分隔符再加文件名
                // 谁知道你传递过来的参数是f:/a还是f:/a/啊？
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }
                // 如果遇到文件
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    // 复制并且改名
                    FileOutputStream output = new FileOutputStream(newPath
                            + File.separator + (temp.getName()).toString());
                    byte[] bufferarray = new byte[1024 * 64];
                    int prereadlength;
                    while ((prereadlength = input.read(bufferarray)) != -1) {
                        output.write(bufferarray, 0, prereadlength);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                // 如果遇到文件夹
                if (temp.isDirectory()) {
                    copyFolder(oldPath + File.separator + file[i], newPath + File.separator + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("复制整个文件夹内容操作出错");
            new File(newPath).delete();
        }
    }

    /**
     * 移动文件夹或者复制文件夹
     *
     * @param oldDerPath 原目录地址
     * @param newPath    新目录地址
     * @param pd         true 移动目录   false 复制目录
     */

    public static void copyOrMoveFolder(String oldDerPath, String newPath, boolean pd) {
        // 复制原目录
        copyFolder(oldDerPath, newPath);
        // 则删除源目录文件
        if (pd) {
            delFilesAll(oldDerPath, pd);
        }
    }

    /**
     * 文件随机名称28-34位
     *
     * @return 返回文件名称  类似于 这种格式 : 833-2020mR6pK7sN17zT54zE56aH-229
     */
    public static String getGenerateFileName() {
        char[] ch = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        char[] Ch = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        Calendar cal = Calendar.getInstance();
        Random sun = new Random();
        int num1 = sun.nextInt(1000);//0-1000随机数
        StringBuilder time = new StringBuilder();
        time.append(num1 + "-");//随机数
        time.append(cal.get(Calendar.YEAR)); //年
        time.append(ch[sun.nextInt(ch.length)]);
        time.append(Ch[sun.nextInt(ch.length)]);
        time.append(cal.get(Calendar.MONTH) + 1);//月
        time.append(ch[sun.nextInt(ch.length)]);
        time.append(Ch[sun.nextInt(ch.length)]);
        time.append(cal.get(Calendar.DAY_OF_MONTH));//日
        time.append(ch[sun.nextInt(ch.length)]);
        time.append(Ch[sun.nextInt(ch.length)]);
        time.append(cal.get(Calendar.HOUR_OF_DAY));//时
        time.append(ch[sun.nextInt(ch.length)]);
        time.append(Ch[sun.nextInt(ch.length)]);
        time.append(cal.get(Calendar.MINUTE));//分
        time.append(ch[sun.nextInt(ch.length)]);
        time.append(Ch[sun.nextInt(ch.length)]);
        time.append(cal.get(Calendar.SECOND));//秒
        time.append(ch[sun.nextInt(ch.length)]);
        time.append(Ch[sun.nextInt(ch.length)] + "-");
        int num = sun.nextInt(1000);//0-1000随机数
        time.append(num);//随机数
        return time.toString();
    }

    /**
     * 计算 当前时间和 文件创建时间 差
     *
     * @param file 传入的文件
     * @param num  1.选择时间差毫秒  2.选择时间差天数
     * @return 返回时间差 毫秒
     */
    public long FileDateDifference(String file, int num) {
        File file1 = new File(file);
        long nd = 1000 * 24 * 60 * 60;
        long diff = System.currentTimeMillis() - new Date(file1.lastModified()).getTime();
        switch (num) {
            case 1:
                // 获得两个时间的毫秒时间差异
                return diff;
            case 2:
                return diff / nd;
            default:
                return diff;
        }
    }


    private static final MimetypesFileTypeMap mimeFileTypeMap = new MimetypesFileTypeMap();

    /**
     * 根据文件名称获取  MIME Type   (文件的对应浏览器的类型)
     * Params:
     * fileName – 文件名称 通过文件名然后再创建文件方式
     * Returns:
     */

    public static String getMIMEType(String fileName) {
        if (StringUtils.isNotBlank(fileName) && fileName.indexOf('.') > 0) {
            String suffix = fileName.substring(fileName.lastIndexOf('.') + 1);
            suffix = suffix.toLowerCase();
            if ("pdf".equals(suffix) || "ofd".equals(suffix)) {
                return "application/" + suffix;
            } else {
                File file = new File(fileName);
                return mimeFileTypeMap.getContentType(file);
            }
        }
        return "";
    }


    //计算文件的大小
    public static long getFileByte(String filePath) {
        long size = 0L;
        File f = new File(filePath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            try {
                size = fis.available();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return size;
    }


    //判断文件是否是根目录的文件
    // File 对象自动会把 D:\\a\\  后面的\\给去掉  所以路径中只有一个\的才能算是根路径下的文件
    public static boolean isFilePathRoot(File file) {
        if (file.isFile()) {
            int i = StringUtil.containStrCount(file.getAbsolutePath(), File.separator);
            if (i == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 去掉首个分割符,然后适配当前系统的分割符
     *
     * @param relativePath
     * @return
     */
    public static String cutFirstSeparatorAdaptation(String relativePath) {
        char c = relativePath.charAt(0);
        if (c == '/' || c == '\\') {//判断首个是否是分割符
            relativePath= relativePath.substring(1);
            return cutFirstSeparatorAdaptation(relativePath);//递归
        }
        //适配当前系统的分割符
        relativePath = relativePath.replace("\\*|/*", File.separator);
        return relativePath;
    }

    /**
     * 去掉结尾分割符,然后适配当前系统的分割符
     *
     * @param relativePath
     * @return
     */
    public static String cutLastSeparatorAdaptation(String relativePath) {
        //判断结尾是否是分割符
        char c = relativePath.charAt(relativePath.length() - 1);
        if (c == '/' || c == '\\') {//判断结尾是否是分割符
            relativePath = relativePath.substring(0, relativePath.length() - 1); //去掉第一个/
            return cutLastSeparatorAdaptation(relativePath);//递归
        }
        //适配当前系统的分割符
        relativePath = relativePath.replace("\\*|/*", File.separator);
        return relativePath;
    }


    //判断是否是文件 ,(通过点和点后缀来判断, 不能保证准确性)
    // 因为java内部提供的FIle判断是否是文件,如果本地文件或者文件夹不存在那么是无法判断的
    public static boolean isFile(String path) {
        int i = path.lastIndexOf(".");
        if (i == -1) {
            return false;
        }
        String substring = path.substring(i);
        if (substring.length() > 6) { //那么不是文件,因为目前市面上几乎没有大于6的文件名后缀
            return false;
        }
        return true;

    }
    //创建临时文件

    /**
     *
     * @param prefix 前缀,
     * @param suffix 后缀
     * 比如前缀设置test,后缀设置.txt那么结果如下:
     * C:\Users\huanmin\AppData\Local\Temp\test9169400899030102146.txt
     * @return
     */
    public static File createTempFile(String prefix, String suffix) {
        String tempDirectoryPath = getTempDirectoryPath();
        String filePath= tempDirectoryPath + File.separator + prefix + suffix;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                boolean newFile = file.createNewFile();
                if (newFile) {
                    return  file;
                }else{
                    throw new RuntimeException("创建临时文件失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    //创建临时文件夹

    /**
     *
     * @param dirName 文件夹名称
     *  比如test 那么结果如下: C:\Users\huanmin\AppData\Local\Temp\test7950942266666786431
     * @return
     */
    public static File createTempDirectory(String dirName) {
        String tempDirectoryPath = getTempDirectoryPath();
        File file = new File(tempDirectoryPath + File.separator + dirName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    //在指定临时目录下创建临时文件
    public static File createTempFile(String prefix, String suffix, File directory) {
        try {
            String filePath=directory.getAbsolutePath()+File.separator+prefix+suffix;
            File file=new  File(filePath);
            if ( !file.exists()) {
                boolean newFile = file.createNewFile();
                if (newFile) {
                    return file;
                }else{
                    throw new RuntimeException("创建临时文件失败");
                }
            }else{
                return file;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //获取临时目录地址
    public static String getTempDirectoryPath() {
        return System.getProperty("java.io.tmpdir");
    }
    //通过目录名称prefix判断临时目录是否存在
    public static boolean isTempDirectory(String prefix) {
        File file = new File(getTempDirectoryPath());
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file1 : files) {
                    if (file1.getName().startsWith(prefix)) {
                        return true;
                    }
                }
            }else {
                return false;
            }
        }
        return false;
    }


}
