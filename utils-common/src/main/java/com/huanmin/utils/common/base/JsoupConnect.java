package com.huanmin.utils.common.base;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * 简要描述
 * @Author: huanmin
 * @Date: 2022/7/17 18:47
 * @Version: 1.0
 * @Description: 文件作用详细描述....
 * Document execute = JsoupConnect.build("http://www.weather.com.cn/textFC/hb.shtml").getExecute();
 */
public class JsoupConnect {

   private final Connection connect;

    public static   JsoupConnect build(String url) {
        return new JsoupConnect(url);
    }
    public  Document  getExecute() {
        Document document = null;
        try {
            document = connect.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  document;
    }
    public  Document  postExecute() {
        Document document = null;
        try {
            document = connect.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  document;
    }


    public JsoupConnect(String url) {
        Connection connect1 = Jsoup.connect(url);
        TrustManager[] trustManagers = buildTrustManagers();
        connect1.timeout(30000);//超时时间 30秒
        connect1.sslSocketFactory(createSSLSocketFactory(trustManagers));
        connect1.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        this.connect =connect1;
    }
    //设置代理
    //        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));
    public  JsoupConnect proxy(Proxy.Type type,String ip,int port) {
        Proxy proxy = new Proxy(type, new InetSocketAddress(ip, port));
        this.connect.proxy(proxy);
        return this;
    }

    public  JsoupConnect cookie(String name, String value){
        connect.cookie(name,value);
        return this;
    }
    public  JsoupConnect header(String name, String value){
        connect.header(name,value);
        return this;
    }

    //get 和 post
    public  JsoupConnect addParameter(String key, String value){
        connect.data(key,value);
        return this;
    }

    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     *
     * @return
     */

    private SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }

    private  TrustManager[] buildTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
    }

}
