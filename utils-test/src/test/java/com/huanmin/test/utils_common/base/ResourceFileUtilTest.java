package com.huanmin.test.utils_common.base;

import com.utils.common.base.ResourceFileUtil;
import org.junit.Test;

import java.io.File;

public class ResourceFileUtilTest {

    @Test
    public void testGetResourceFile() {
        String currentProjectAbsolutePath = ResourceFileUtil.getCurrentProjectAbsolutePath();
        System.out.println(currentProjectAbsolutePath);
    }
    @Test
    public void getCurrentProjectResourcesAbsolutePath() {
        String currentProjectAbsolutePath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath();
        System.out.println(currentProjectAbsolutePath);
    }
    @Test
    public void getCurrentProjectResourcesFileAbsolutePath() {
        String currentProjectAbsolutePath = ResourceFileUtil.getCurrentProjectResourcesAbsolutePath("/abc/database.properties");
        System.out.println(currentProjectAbsolutePath);
    }
    @Test
    public void getCurrentProjectResourcesFileAbsoluteFile() {
        File currentProjectResourcesFileAbsoluteFile = ResourceFileUtil.getCurrentProjectResourcesAbsoluteFile("/abc/database.properties");
        System.out.println(currentProjectResourcesFileAbsoluteFile);
    }

    @Test
    public void getCurrentProjectTargetClassAbsolutePath(){
        String currentProjectClassAbsolutePath = ResourceFileUtil.getCurrentProjectTargetClassAbsolutePath("/abc111/database.properties");

        System.out.println(currentProjectClassAbsolutePath);
    }
    @Test
    public void getCurrentProjectTargetTestClassAbsolutePath()  {
        String currentProjectClassAbsolutePath = ResourceFileUtil.getCurrentProjectTargetTestClassAbsolutePath("/abc111/database.properties");

        System.out.println(currentProjectClassAbsolutePath);
    }
}
