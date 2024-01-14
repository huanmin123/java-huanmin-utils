package org.huanmin.utils.common.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 执行linux的命令或者windows命令
 *
 * @Author: huanmin
 * @Date: 2022/10/26 21:30
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 */
public class CommandUtil {

    public  static boolean executeLinuxCmd(String cmd) {
        boolean result=false;
        Runtime run = Runtime.getRuntime();
        try {
            Process process = run.exec(cmd);
            //执行结果 0 表示正常退出
            int exeResult=process.waitFor();
            if(exeResult==0){
                result=true;
            }
        }
        catch (Exception e) {
            ColourLog.trace("Couldn't run command {}:", cmd);
        }
        return result;
    }


    
    /**
     *  在本机命令行上执行命令并返回结果。
     *
     * @param cmd  Command to run
     *
     * @return 结果的字符串列表，或为空
     *
     */
    public static List<String> runCmd(String cmd) {
        String[] cmds = cmd.split(" ");
        return runCmd(cmds);
    }
    /**
     
     * 返回所选命令的第一行响应。
     * @param cmd  要启动的字符串命令
     * @return String or empty string if command failed
     */
    public static String getFirstCmd(String cmd) {
        return getCmdAt(cmd, 0);
    }
    
    /**
     * 在本机命令行上执行命令并返回结果行   new String[] { jps, "-v", "-l" };
     * @param cmd   命令在数组中运行和参数
     * @return 表示命令结果的字符串列表，或为空
     */
    public static List<String> runCmd(String[] cmd) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmd);
        } catch (SecurityException | IOException e) {
            ColourLog.trace("Couldn't run command {}:", Arrays.toString(cmd));
            ColourLog.trace(e);
            return new ArrayList<String>(0);
        }
        
        ArrayList<String> sa = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sa.add(line);
            }
            p.waitFor();
        } catch (IOException e) {
            ColourLog.trace("Problem reading output from {}:", Arrays.toString(cmd));
            ColourLog.trace(e);
            return new ArrayList<String>(0);
        } catch (InterruptedException ie) {
            ColourLog.trace("Problem reading output from {}:", Arrays.toString(cmd));
            ColourLog.trace(ie);
            Thread.currentThread().interrupt();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                ColourLog.trace("Problem reading output from {}:", Arrays.toString(cmd));
            }
        }
        return sa;
    }
    

    
    
    
    /**
     * Return response on selected line index (0-based) after running selected
     * command.
     *
     * @param cmd2launch
     *            String command to be launched
     * @param answerIdx
     *            int index of line in response of the command
     * @return String whole line in response or empty string if invalid index or
     *         running of command fails
     */
    public static String getCmdAt(String cmd2launch, int answerIdx) {
        List<String> sa = CommandUtil.runCmd(cmd2launch);
        
        if (answerIdx >= 0 && answerIdx < sa.size()) {
            return sa.get(answerIdx);
        }
        return "";
    }
    
    
    


    public static void main(String[] args) {

        System.out.println(CommandUtil.runCmd("wmic csproduct get uuid"));
    }
}