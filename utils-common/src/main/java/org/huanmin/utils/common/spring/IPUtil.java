package org.huanmin.utils.common.spring;

import org.huanmin.utils.common.base.UniversalException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @Description：IP工具类
 * @author zrt
 * @date 2018年9月22日 上午10:38:13
 */
public class IPUtil {

    private static final Logger logger = LoggerFactory.getLogger(IPUtil.class);


    /**
     * 获取访问用户的客户端IP（适用于公网与局域网）.
     */
    public static  String getIpAddr(final HttpServletRequest request)
            throws Exception {
        if (request == null) {
            throw (new Exception("getIpAddr method HttpServletRequest Object is null"));
        }
        String ipString = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("X-Real-IP");
        }

        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getRemoteAddr();
            if("127.0.0.1".equals(ipString)||"0:0:0:0:0:0:0:1".equals(ipString)){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                     UniversalException.logError(e);
                }
                assert inet != null;
                ipString= inet.getHostAddress();
            }
        }

        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ipString.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ipString = str;
                break;
            }
        }

        return ipString;
    }

    //判断ip、端口是否可连接
    public static boolean isHostConnectable(String host, int port) {
        return  isHostConnectable(host,port,3000);
    }
    public static boolean isHostConnectable(String host, int port,int timeOut) {
        Socket socket = new Socket();
        try {
            socket.setSoTimeout(timeOut);
            socket.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
             UniversalException.logError(e);
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                 UniversalException.logError(e);
            }
        }
        return true;
    }

    //判断ip是否可以连接 timeOut是超时时间
    public static boolean isHostReachable(String host, int timeOut) {
        try {
            return InetAddress.getByName(host).isReachable(timeOut);
        } catch (UnknownHostException e) {
             UniversalException.logError(e);
        } catch (IOException e) {
             UniversalException.logError(e);
        }
        return false;
    }

}