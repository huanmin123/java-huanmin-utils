package com.huanmin.utils.common.base;



import javax.net.ServerSocketFactory;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.List;
import java.util.Random;

/**
 *
 * 端口工具类
 *
 */
public class PortUtil {

    /**
     查找可用套接字端口时使用的端口范围的默认最小值。
     */
    public static final int PORT_RANGE_MIN = 1024;

    /**
     查找可用套接字端口时使用的端口范围的默认最大值。
     */
    public static final int PORT_RANGE_MAX = 65535;

    private static final Random random = new Random(System.currentTimeMillis());

    private PortUtil() {
    }

    //获取端口号对应的进程号
    public static long findTcpListenProcess(int port) {
        try {
            if (OSUtil.isWindows()) {
                String[] command = { "netstat", "-ano", "-p", "TCP" };
                List<String> lines = CommandUtil.runCmd(command);
                for (String line : lines) {
                    if (line.contains("LISTENING")) {
                        // TCP 0.0.0.0:49168 0.0.0.0:0 LISTENING 476
                        String[] strings = line.trim().split("\\s+");
                        if (strings.length == 5) {
                            if (strings[1].endsWith(":" + port)) {
                                return Long.parseLong(strings[4]);
                            }
                        }
                    }
                }
            }

            if (OSUtil.isLinux() || OSUtil.isMac()) {
                String pid = CommandUtil.getFirstCmd("lsof -t -s TCP:LISTEN -i TCP:" + port);
                if (!pid.trim().isEmpty()) {
                    return Long.parseLong(pid);
                }
            }
        } catch (Throwable e) {
            // ignore
        }

        return -1;
    }
    //判断端口是否可用
    public static boolean isTcpPortAvailable(int port) {
        try {
            ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(port, 1,
                    InetAddress.getByName("localhost"));
            serverSocket.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     查找从范围 [1024， 65535] 中随机选择的可用 TCP 端口。
     返回值:
     可用的 TCP 端口号
     抛出:
     IllegalStateException – 如果找不到可用端口
     */
    public static int findAvailableTcpPort() {
        return findAvailableTcpPort(PORT_RANGE_MIN);
    }

    /**
     查找从范围 [minPort， 65535] 中随机选择的可用 TCP 端口。
     形参:
     minPort – 最小端口号
     返回值:
     可用的 TCP 端口号
     抛出:
     IllegalStateException – 如果找不到可用端口
     */
    public static int findAvailableTcpPort(int minPort) {
        return findAvailableTcpPort(minPort, PORT_RANGE_MAX);
    }

    /**
     查找从范围 [， maxPort]minPort 中随机选择的可用 TCP 端口。
     形参:
     minPort – 最小端口号 maxPort – 最大端口号
     返回值:
     可用的 TCP 端口号
     抛出:
     IllegalStateException – 如果找不到可用端口
     */
    public static int findAvailableTcpPort(int minPort, int maxPort) {
        return findAvailablePort(minPort, maxPort);
    }

    /**
     为此找到 SocketType一个可用的端口，从范围 [minPort， maxPort] 中随机选择。
     形参:
     minPort – 最小端口号 maxPort – 最大端口号
     返回值:
     此套接字类型的可用端口号
     抛出:
     IllegalStateException – 如果找不到可用端口
     */
    private static int findAvailablePort(int minPort, int maxPort) {

        int portRange = maxPort - minPort;
        int candidatePort;
        int searchCounter = 0;
        do {
            if (searchCounter > portRange) {
                throw new IllegalStateException(
                        String.format("Could not find an available tcp port in the range [%d, %d] after %d attempts",
                                minPort, maxPort, searchCounter));
            }
            candidatePort = findRandomPort(minPort, maxPort);
            searchCounter++;
        } while (!isTcpPortAvailable(candidatePort));

        return candidatePort;
    }

    /**
     在 [minPort， maxPort] 范围内查找伪随机端口号
     形参:
     minPort – 最小端口号 maxPort – 最大端口号
     返回值:
     指定范围内的随机端口号
     */
    private static int findRandomPort(int minPort, int maxPort) {
        int portRange = maxPort - minPort;
        return minPort + random.nextInt(portRange + 1);
    }
}
