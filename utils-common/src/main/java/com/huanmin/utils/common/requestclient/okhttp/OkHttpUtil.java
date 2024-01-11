package com.huanmin.utils.common.requestclient.okhttp;

import com.alibaba.fastjson.JSON;
import com.huanmin.utils.common.base.UniversalException;
import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author  胡安民 ,自研改造
 */
public class OkHttpUtil {

    private OkHttpClient okHttpClient = null;
    private Map<String, String> headerMap;
    private Map<String, Object> paramMap;
    private String url;
    private Request.Builder request;
    //懒汉模式,单例
    private OkHttpUtil() {
        OkHttpClient getOkHttpClient= createOkHttpClient();
        this.okHttpClient=getOkHttpClient;
    }

    /**
     * 初始化okHttpClient，并且允许https访问
     */
    private   OkHttpClient createOkHttpClient(){
        //设置代理方式
//                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));

        TrustManager[] trustManagers = buildTrustManagers();
        OkHttpClient  okHttpClient = new OkHttpClient.Builder()
                               //设置连接超时时间
                               .connectTimeout(15, TimeUnit.SECONDS)
                               //写入超时时间
                               .writeTimeout(20, TimeUnit.SECONDS)
                               //从连接成功到响应的总时间
                               .readTimeout(20, TimeUnit.SECONDS)
                               //跳过ssl认证(https)
                               .sslSocketFactory(createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0])
                               .hostnameVerifier((hostName, session) -> true)
                               .retryOnConnectionFailure(true)
//                            .proxy(proxy)//代理ip
                               //                                       最大连接数量  , 持续存活的连接时间
                               .connectionPool(new ConnectionPool(500, 10, TimeUnit.MINUTES))
                               .build();
        addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");

        return okHttpClient;
    }

    /**
     * 创建OkHttpUtil
     *
     * @return
     */
    public static OkHttpUtil builder() {
        return new OkHttpUtil();
    }





    /**
     * 添加url
     *
     * @param url
     * @return
     */
    public OkHttpUtil url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 添加参数
     * 注意: 如果是POST请求并且key中存在JsonBody,那么就只会使用JsonBody的值作为参数其他参数会被忽略
     * @param key   参数名
     * @param value 参数值
     * @return
     */
    public OkHttpUtil addParam(String key, Object value) {
        if (paramMap == null) {
            paramMap = new LinkedHashMap<>(16);
        }
        paramMap.put(key, value);
        return this;
    }

    /**
     * 添加请求头
     *
     * @param key   参数名
     * @param value 参数值
     * @return
     */
    public OkHttpUtil addHeader(String key, String value) {
        if (headerMap == null) {
            headerMap = new LinkedHashMap<>(16);
        }
        headerMap.put(key, value);
        return this;
    }

    /**
     * 初始化get方法
     *
     * @return
     */
    public OkHttpUtil get() {
        request = new Request.Builder().get();
        StringBuilder urlBuilder = new StringBuilder(url);
        if (paramMap != null) {
            urlBuilder.append("?");
            try {
                for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                    urlBuilder.append(URLEncoder.encode(entry.getKey(), "utf-8")).
                            append("=").
                            append(URLEncoder.encode((String) entry.getValue(), "utf-8")).
                            append("&");
                }
            } catch (Exception e) {
                 UniversalException.logError(e);
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        request.url(urlBuilder.toString());
        return this;
    }

    /**
     * 初始化post方法
     *
     * @param type json/form/file 三种类型 json:转为json字符串 form:普通表单提交 file:文件上传+表单提交
     * file类型时，需要添加文件参数，key为filePath ，value为文件路径, 或者value为文件字节(如果是文件字节，需要添加文件名参数，key为fileName，value为文件名
     * file类型时，需要添加文件参数，key为filePaths ，value为文件路径,多个路径使用字符串数组, value为文件字节,多个文件字节使用二维byte数组(如果是文件字节，需要添加文件名参数，key为fileNames，value为文件名,多个文件名使用数组方式)
     *
     * @return
     */
    public OkHttpUtil post(String type) {
        RequestBody requestBody = null;
        switch (type) {
            case "json":
                String json = "";
                if (paramMap != null) {
                    Object object = paramMap.get("JsonBody"); //如果有JsonBody参数，那么就直接使用JsonBody参数
                    if (object != null) {
                        json = JSON.toJSONString(object);
                    } else {
                        json = JSON.toJSONString(paramMap);
                    }
                }
                requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                break;
            case "form":
                FormBody.Builder formBody = new FormBody.Builder();
                if (paramMap != null) {
                    for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        formBody.add(key, (String) value);
                    }
                }
                requestBody = formBody.build();
                break;
            case "file":
                String fileKey="file";
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                for (Map.Entry<String, Object> field: paramMap.entrySet()) { // 添加表单信息
                    //排除文件参数
                    if("filePath".equals(field.getKey()) || "filePaths".equals(field.getKey()) ||
                               "fileByte".equals(field.getKey()) || "fileByteName".equals(field.getKey())||
                               "fileBytes".equals(field.getKey()) || "fileBytesNames".equals(field.getKey())||
                               "fileKey".equals(field.getKey())
                    ){
                            if ("fileKey".equals(field.getKey())){
                                fileKey = (String) field.getValue();
                            }
                        continue;
                    }
                    builder.addFormDataPart(field.getKey(), (String) field.getValue());
                }
                //单文件上传
                String filePath = (String) paramMap.get("filePath");
                if(filePath != null){
                    File file = new File(filePath);
                    builder.addFormDataPart(fileKey, file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
                }
                //多文件上传
                String[] filePaths = (String[]) paramMap.get("filePaths");
                if(filePaths != null){
                    for (String filePath1 : filePaths) {
                        File file = new File(filePath1);
                        builder.addFormDataPart(fileKey, file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
                    }
                }
                //上传字节流
                byte[] fileByte = (byte[]) paramMap.get("fileByte");
                String fileByteName = (String) paramMap.get("fileByteName");//字节流文件名
                if(fileByte != null){
                    builder.addFormDataPart(fileKey, fileByteName, RequestBody.create(MediaType.parse("application/octet-stream"), fileByte));
                }
                //上传字节流数组
                byte[][] fileBytes = (byte[][]) paramMap.get("fileBytes");
                String[] fileBytesNames = (String[]) paramMap.get("fileBytesNames");//字节流文件名数组
                if(fileBytes != null){
                    for (int i = 0; i < fileBytes.length; i++) {
                        builder.addFormDataPart(fileKey, fileBytesNames[i], RequestBody.create(MediaType.parse("application/octet-stream"), fileBytes[i]));
                    }
                }
                requestBody = builder.build();
                break;
            default:
                throw new RuntimeException("type参数错误");
        }

        request = new Request.Builder().post(requestBody).url(url);
        return this;
    }

    public OkHttpUtil put() {
        String json = "";
        if (paramMap != null) {
            json = JSON.toJSONString(paramMap);
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        request = new Request.Builder().put(requestBody).url(url);
        return this;
    }

    public OkHttpUtil del() {
        String json = "";
        if (paramMap != null) {
            json = JSON.toJSONString(paramMap);
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        request = new Request.Builder().delete(requestBody).url(url);
        return this;
    }


    //下载文件,到指定路径  ,注意这种方式是异步的,不用等待下载完成就可以执行后面的代码
    public void downloadFileAsync(String filePath) {
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("下载失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                InputStream inputStream = response.body().byteStream();
                File file = new File(filePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            }
        });
    }
    public void downloadFile(String filePath) {
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            assert response.body() != null;
            InputStream inputStream = response.body().byteStream();
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //下载文件返回字节流
    public byte[] downloadFile() {

        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            assert response.body() != null;
            return response.body().bytes();
        } catch (IOException e) {
             UniversalException.logError(e);
            return null;
        }
    }

    //下载文件返回inputStream
    public InputStream downloadFileInputStream() {
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            assert response.body() != null;
            return response.body().byteStream();
        } catch (IOException e) {
             UniversalException.logError(e);
            return null;
        }
    }

    /**
     * 同步请求
     *
     * @return
     */
    public String sync() {
        setHeader(request);
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            assert response.body() != null;
            return response.body().string();
        } catch (IOException e) {
             UniversalException.logError(e);
            return "请求失败：" + e.getMessage();
        }
    }

    /**
     * 异步请求，有返回值
     */
    public String async() {
        StringBuilder buffer = new StringBuilder("");
        setHeader(request);
        CountDownLatch countDownLatch=new CountDownLatch(1);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                buffer.append("请求出错：").append(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                buffer.append(response.body().string());
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
             UniversalException.logError(e);
        }
        return buffer.toString();
    }

    /**
     * 异步请求，带有接口回调
     *
     * @param callBack
     */
    public void async(ICallBack callBack) {
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(call, e.getMessage());
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                callBack.onSuccessful(call, response.body().string());
            }
        });
    }
    
    /**
     * 为request添加请求头
     *
     * @param request
     */
    private void setHeader(Request.Builder request) {
        if (headerMap != null) {
            try {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                 UniversalException.logError(e);
            }
        }
    }
    
    
    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     *
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
             UniversalException.logError(e);
        }
        return ssfFactory;
    }
    
    private static TrustManager[] buildTrustManagers() {
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
    
    /**
     * 自定义一个接口回调
     */
    public interface ICallBack {
        
        void onSuccessful(Call call, String data);
        
        void onFailure(Call call, String errorMsg);
        
    }
}

