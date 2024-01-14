package org.huanmin.test.utils.utils_common.base;


import org.huanmin.utils.common.base.SshUtil;

/**
 *
 * @author wangzonghui
 * @date 2022年2月7日 上午9:55:58
 * @Description ssh工具类测试
 */
public final class SshUtilTest {
    
    public static void main(String[] args) throws Exception {
        SshUtil sshUtil=new SshUtil("106.12.174.220",22,"root","hu123456!");
        SshUtil.SshModel run = sshUtil.run("ls -l");
        System.out.println(run.getCode());
        System.out.println(run.getInfo());
        System.out.println(run.getError());
        sshUtil.close();
    }

}
