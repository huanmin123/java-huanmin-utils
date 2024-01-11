package com.huanmin.test.utils.utils_common.base;


import jline.console.ConsoleReader;

public class ConsoleTest {

    public static void main(String[] args) throws Exception {
 
        final ConsoleReader consoleReader = new ConsoleReader(System.in, System.out);
        consoleReader.setPrompt("demo > "); // 输入命令提示信息

        // 用while死循环，可以在执行完命令后不退出程序，可以继续输入命令
        while (true) {
            String line = consoleReader.readLine(); // 获取输入的信息
            System.out.println("输入的命令是：" + line); // 输出输入的信息
        }
    
    }
}
