package com.huanmin.test.utils.mine;

import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Java实现鼠标随机移动
 */
public class MouseController implements Runnable {

    private Robot robot;
    private boolean isStop = false;

    public MouseController() {
        try {
            ControllerFrame frame = new ControllerFrame("Prevent Locking");
            frame.setVisible(true);
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @Override
    public void run() {
        int x;
        int y;
        Random random = new Random();
        while (!isStop) {
//            //随机生成坐标。
//            x = random.nextInt(10)+1000;  // 1000
//            y = random.nextInt(10)+600;  //1000
//            //开始移动鼠标
//            robot.mouseMove(x, y);

//            robot.mousePress(KeyEvent.BUTTON3_DOWN_MASK);     // 模拟按下鼠标右键
//            robot.mouseRelease(KeyEvent.BUTTON3_DOWN_MASK);   // 模拟释放鼠标右键

            //鼠标点击
            robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
            //鼠标抬起
            robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);

            //每5秒一次操作
            Thread.sleep(5000);

        }

    }

    /**
     * GUI Frame 生成一个button，控制程序
     *
     * @author max
     */
    private class ControllerFrame extends JFrame {
        private static final long serialVersionUID = 1L;

        private JButton close = new JButton("close");

        public ControllerFrame(String title) {
            this();
            setTitle(title);
        }

        public ControllerFrame() {
            setLayout(new FlowLayout(FlowLayout.LEADING));
            setSize(100, 100);
            setResizable(false);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            Dimension preferredSize = new Dimension(100, 60);
            Font font = new Font("", 1, 14);

            //设置button 大小，文字等属性
            close.setPreferredSize(preferredSize);
            close.setFont(font);
            close.setBorderPainted(true);
            close.setFocusable(false);

            add(close);

            //点击button后，程序终止。
            close.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    isStop = true;
                    dispose();
                }
            });

        }

    }

    public static void main(String[] args) {
        MouseController m = new MouseController();
        m.run();
    }

}
