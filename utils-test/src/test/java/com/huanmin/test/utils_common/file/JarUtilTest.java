package com.huanmin.test.utils_common.file;

import com.utils.common.file.JarUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.jar.JarEntry;

/**
 * 简要描述
 *
 * @Author: huanmin
 * @Date: 2022/11/24 19:16
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class JarUtilTest {
    @Test
    public void  selectAll() throws IOException {
        Map<JarEntry, InputStream> jarEntryInputStreamMap = JarUtil.readJarFile(
                "D:\\project\\InstallationProgram\\out\\artifacts\\InstallationProgram_jar\\InstallationProgram.jar");
        jarEntryInputStreamMap.forEach((k,v)->{
            System.out.println(k.getName());
        });


    }
    @Test
    public void  select() throws Exception {
        Map<JarEntry, InputStream> jarEntryInputStreamMap = JarUtil.readJarFile(
                "D:\\project\\InstallationProgram\\out\\artifacts\\InstallationProgram_jar\\InstallationProgram.jar",".*\\.jar");

        jarEntryInputStreamMap.forEach((k,v)->{
            System.out.println(k.getName());

        });
    }


    @Test
    public void  selectRecursion() throws Exception {

        Map<JarEntry, InputStream> jarEntryInputStreamMap = JarUtil.readJarFileRecursion(
                "D:\\project\\InstallationProgram\\out\\artifacts\\InstallationProgram_jar\\InstallationProgram.jar",".*\\.jar");
        jarEntryInputStreamMap.forEach((k,v)->{
            System.out.println(k.getName());

        });

        Thread.sleep(1000000);


    }

    @Test
    public void  addFileToJar() throws Exception {
        JarUtil.addFileToJar("D:\\project\\InstallationProgram\\out\\artifacts\\InstallationProgram_jar\\InstallationProgram.jar",
                "D:\\project\\InstallationProgram\\out\\artifacts\\InstallationProgram_jar\\a.txt"
                ,"test"
        );

    }
    @Test
    public  void  delFileFromJar() throws Exception {
        JarUtil.delFileFromJar("D:\\project\\InstallationProgram\\out\\artifacts\\InstallationProgram_jar\\InstallationProgram.jar",
                "test\\a.txt"
        );
    }
}
