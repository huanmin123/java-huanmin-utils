package com.utils.common.requestclient.httpclient;

import com.alibaba.fastjson.JSON;
import com.utils.common.base.UniversalException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;


public class HttpClientUtil implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager();
    private static final RequestConfig requestConfig;
    private static final int MAX_TOTAL = 100;
    private static final int MAX_TIMEOUT = 7000;
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int SOCKET_TIMEOUT = 40000;
    public static final int cache = 10 * 1024;
    public static final boolean isWindows;
    public static final String splash;
    public static final String root;

    public HttpClientUtil() {
    }

    static {
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(MAX_TOTAL);
        Builder configBuilder = RequestConfig.custom();
        configBuilder.setConnectTimeout(CONNECT_TIMEOUT);
        configBuilder.setSocketTimeout(SOCKET_TIMEOUT);
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);

        requestConfig = configBuilder.build();

        if (System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("windows")) {
            isWindows = true;
            splash = "\\";
            root = "D:";
        } else {
            isWindows = false;
            splash = "/";
            root = "/search";
        }
    }






    /**
     * 发送http请求  get  post  put delete
     * get  通过url?name=""&&name=""   发送参数
     * pist 和put ,delete 通过请求体发送参数 json格式
     */
    public static String httpClient(String postType, String url, String jsonStr) {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = createSSLClientDefault(); //跳过证书验证
        CloseableHttpResponse httpResponse = null;
        String result = null;
        try {
            if ("Get".equals(postType)) {
                //===== get =====//
                HttpGet httpGet = new HttpGet(url);
                httpGet.setConfig(requestConfig);
                //设置header
                httpGet.setHeader("Content-type", "application/json");
                httpGet.setHeader("DataEncoding", "UTF-8");
                //发送请求
                httpResponse = httpClient.execute(httpGet);
            } else if("Post".equals(postType)) {
                //===== post =====//
                HttpPost httpPost = new HttpPost(url);
                httpPost.setConfig(requestConfig);
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("DataEncoding", "UTF-8");
                //增加参数
                httpPost.setEntity(new StringEntity(jsonStr.toString(),"UTF-8"));
                httpResponse = httpClient.execute(httpPost);
            } else if("Put".equals(postType)) {
                //===== put =====//
                HttpPut httpPut = new HttpPut(url);
                httpPut.setConfig(requestConfig);
                httpPut.setHeader("Content-type", "application/json");
                httpPut.setHeader("DataEncoding", "UTF-8");
                httpPut.setEntity(new StringEntity(jsonStr.toString(),"UTF-8"));
                httpResponse = httpClient.execute(httpPut);
            } else if ("Delete".equals(postType)) {
                //===== delete =====  不能有参数 //
                HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
                httpDelete.setConfig(requestConfig);
                httpDelete.setHeader("Content-type", "application/json");
                httpDelete.setHeader("DataEncoding", "UTF-8");
                StringEntity input = new StringEntity(jsonStr.toString(), ContentType.APPLICATION_JSON);
                httpDelete.setEntity(input);
                httpResponse = httpClient.execute(httpDelete);


            }else{
                result=null;
                System.out.println("没有找到你要的请求");
            }
            //返回结果
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (IOException e) {
             UniversalException.logError(e);
        }
        return result;
    }
// 可以实现 delete 请求 body 传输数据
    static class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
        public static final String METHOD_NAME = "DELETE";

        @Override
        public String getMethod() {
            return METHOD_NAME;
        }

        public HttpDeleteWithBody(final String uri) {
            super();
            setURI(URI.create(uri));
        }

        public HttpDeleteWithBody(final URI uri) {
            super();
            setURI(uri);
        }

        public HttpDeleteWithBody() {
            super();
        }
    }

// ------------------------------进阶  get  post  put  delete      map集合作为参数 的 请发送



    public static String doGet(String url, Map<String, Object> params) throws Exception {
        String result = null;
        if (StringUtils.isEmpty(url)) {

            return result;
        } else {
            params=params==null?new HashMap<>():params;
            List<NameValuePair> pairList = new ArrayList(params.size());
            Iterator var4 = params.entrySet().iterator();

            while(var4.hasNext()) {
                Entry<String, Object> entry = (Entry)var4.next();
                NameValuePair pair = new BasicNameValuePair((String)entry.getKey(), entry.getValue().toString());
                pairList.add(pair);
            }

            CloseableHttpResponse response = null;
            InputStream instream = null;
//        CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpClient httpClient = createSSLClientDefault(); //跳过证书验证
            try {
                URIBuilder URIBuilder = new URIBuilder(url);
                System.out.println("doGet"+pairList);
                URIBuilder.addParameters(pairList);
                URI uri = URIBuilder.build();
                System.out.println("doGet"+uri);
                HttpGet httpGet = new HttpGet(uri);
                httpGet.setConfig(requestConfig);
                response = httpClient.execute(httpGet);

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = IOUtils.toString(instream, StandardCharsets.UTF_8);
                }
            } catch (IOException | URISyntaxException ignored) {

            } finally {
                if (null != instream) {
                    instream.close();
                }

                if (null != response) {
                    response.close();
                }

                if (null != httpClient) {
                    httpClient.close();
                }

            }

            return result;
        }
    }



    //    post请求
    public static String doPost(String url, Map<String, Object> map) throws Exception {
        String result = null;
        if (StringUtils.isEmpty(url)) {

            return result;
        } else {

            //        CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpClient httpClient = createSSLClientDefault(); //跳过证书验证
            HttpPost httpPost = new HttpPost(url);
            CloseableHttpResponse response = null;
            InputStream instream = null;

            try {

                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("DataEncoding", "UTF-8");

                httpPost.setConfig(requestConfig);
                Object o = JSON.toJSON(map==null?new HashMap<>():map);
                httpPost.setEntity( new StringEntity(o.toString(), "UTF-8"));
                response = httpClient.execute(httpPost);

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = IOUtils.toString(instream, "UTF-8");

                }
            } catch (IOException var14) {

            } finally {
                if (null != instream) {
                    instream.close();
                }

                if (null != response) {
                    response.close();
                }

                if (null != httpClient) {
                    httpClient.close();
                }
            }

            return result;
        }
    }



    //    Put请求
    public static String doPut(String url, Map<String, Object> map) throws Exception {
        String result = null;
        if (StringUtils.isEmpty(url)) {

            return result;
        } else {



            //        CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpClient httpClient = createSSLClientDefault(); //跳过证书验证
            HttpPut HttpPut = new HttpPut(url);
            CloseableHttpResponse response = null;
            InputStream instream = null;

            try {

                HttpPut.setHeader("Content-type", "application/json");
                HttpPut.setHeader("DataEncoding", "UTF-8");

                HttpPut.setConfig(requestConfig);
                Object o = JSON.toJSON(map==null?new HashMap<>():map);
                HttpPut.setEntity( new StringEntity(o.toString(), "UTF-8"));
                response = httpClient.execute(HttpPut);

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = IOUtils.toString(instream, StandardCharsets.UTF_8);

                }
            } catch (IOException ignored) {
                ignored.printStackTrace();
            } finally {
                if (null != instream) {
                    instream.close();
                }

                if (null != response) {
                    response.close();
                }

                if (null != httpClient) {
                    httpClient.close();
                }


            }

            return result;
        }
    }

    // Delete 请求
    public static String doDelete(String url, Map<String, Object> map) throws Exception {
        String result = null;
        if (StringUtils.isEmpty(url)) {

            return result;
        } else {



            //        CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpClient httpClient = createSSLClientDefault(); //跳过证书验证
            HttpDeleteWithBody HttpDelete = new HttpDeleteWithBody(url);
            CloseableHttpResponse response = null;
            InputStream instream = null;

            try {

                HttpDelete.setHeader("Content-type", "application/json");
                HttpDelete.setHeader("DataEncoding", "UTF-8");

                HttpDelete.setConfig(requestConfig);
                Object o = JSON.toJSON(map==null?new HashMap<>():map);
                HttpDelete.setEntity( new StringEntity(o.toString(), "UTF-8"));
                response = httpClient.execute(HttpDelete);

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = IOUtils.toString(instream, StandardCharsets.UTF_8);

                }
            } catch (IOException ignored) {
                ignored.printStackTrace();
            } finally {
                if (null != instream) {
                    instream.close();
                }

                if (null != response) {
                    response.close();
                }

                if (null != httpClient) {
                    httpClient.close();
                }


            }

            return result;
        }
    }


    //前端提供MultipartFile[]  我们内置文件上传   多个文件同时上传  配合 controller 层使用 使用
    public static String  httpClientUploadFile(String url, MultipartFile[] files) {
        //        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = createSSLClientDefault(); //跳过证书验证
        String result = "";
        try {
            //文件名
//            String fileName = file.getName();
            HttpPost httpPost = new HttpPost(url);

            //HttpEntity builder
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            //字符编码
            builder.setCharset(StandardCharsets.UTF_8);
            //模拟浏览器
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            for (MultipartFile file : files) {
                InputStream in = file.getInputStream();
                builder.addBinaryBody("file",in,ContentType.MULTIPART_FORM_DATA, file.getOriginalFilename());
            }

            //HttpEntity
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            // 执行提交
            HttpResponse response = httpClient.execute(httpPost);
            //响应
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                // 将响应内容转换为字符串
                result = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);

            }
        } catch (Exception e) {
             UniversalException.logError(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                 UniversalException.logError(e);
            }
        }
        return result;
    }

  // //前端提供MultipartFile 单个文件上传   配合 controller 层使用 使用
    public static String httpClientUploadFile(String url, MultipartFile file) {
        //        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = createSSLClientDefault(); //跳过证书验证
        String result = "";
        try {
            //文件名
//            String fileName = file.getName();
            HttpPost httpPost = new HttpPost(url);
            //HttpEntity builder
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            //字符编码
            builder.setCharset(StandardCharsets.UTF_8);
            //模拟浏览器
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                InputStream in = file.getInputStream();
                builder.addBinaryBody("file",in,ContentType.MULTIPART_FORM_DATA, file.getOriginalFilename());
            //HttpEntity
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            // 执行提交
            HttpResponse response = httpClient.execute(httpPost);
            //响应
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                // 将响应内容转换为字符串
                result = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
             UniversalException.logError(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                 UniversalException.logError(e);
            }
        }
        return result;
    }


    //本地物理文件上传  (单个)  接口必须是这样的格式    public String httpUpload(@RequestParam("file") MultipartFile file)
    public static String httpClientUploadFile(String url, File file) {


        //        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = createSSLClientDefault(); //跳过证书验证
        String result = "";
        try {
            //文件名
//            String fileName = file.getName();
            HttpPost httpPost = new HttpPost(url);
            //HttpEntity builder
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            //字符编码
            builder.setCharset(StandardCharsets.UTF_8);
            //模拟浏览器
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            InputStream in = new FileInputStream(file);
            builder.addBinaryBody("file",in,ContentType.MULTIPART_FORM_DATA, file.getName());
            //HttpEntity
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            // 执行提交
            HttpResponse response = httpClient.execute(httpPost);
            //响应
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                // 将响应内容转换为字符串
                result = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
             UniversalException.logError(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                 UniversalException.logError(e);
            }
        }
        return result;
    }

    //本地物理文件上传  (多个)    接口必须是这样的格式    public String httpUpload(@RequestParam("files") MultipartFile[] files)
    public static String httpClientUploadFiles(String url, File[] files) {

        //        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = createSSLClientDefault(); //跳过证书验证
        String result = "";
        try {
            //文件名
//            String fileName = file.getName();
            HttpPost httpPost = new HttpPost(url);
            //HttpEntity builder
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            //字符编码
            builder.setCharset(StandardCharsets.UTF_8);
            //模拟浏览器
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            for (File file : files) {
                InputStream in = new FileInputStream(file);
                builder.addBinaryBody("files",in,ContentType.MULTIPART_FORM_DATA, file.getName());
            }
            //HttpEntity
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            // 执行提交
            HttpResponse response = httpClient.execute(httpPost);
            //响应
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                // 将响应内容转换为字符串
                result = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
             UniversalException.logError(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                 UniversalException.logError(e);
            }
        }
        return result;
    }




    /**
     * 根据url下载文件，保存到filepath中  本地路径   (一次下载一个)
     *
     * @param url
     * @param filepath
     * @return
     */
    public static String httpClientDownloadFile(String url, String filepath) {
        try {
            //        CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpClient httpClient = createSSLClientDefault(); //跳过证书验证
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpget);

            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            if (filepath == null){
                filepath = getFilePath(response);
            }

            File file = new File(filepath);
            file.getParentFile().mkdirs();
            FileOutputStream fileout = new FileOutputStream(file);
            /**
             * 根据实际运行效果 设置缓冲区大小
             */
            byte[] buffer = new byte[cache];
            int ch = 0;
            while ((ch = is.read(buffer)) != -1) {
                fileout.write(buffer, 0, ch);
            }
            is.close();
            fileout.flush();
            fileout.close();

        } catch (Exception e) {
             UniversalException.logError(e);
        }
        return "下载成功";
    }

    /**
     * 根据url下载文件，保存到filepath中  本地路径   (一次下载多个)
     *
     * @param url
     * @param filepath
     * @return
     */
    public static String httpClientDownloadFile(String[] url, String filepath) {
        for (String s : url) {
            httpClientDownloadFile(s ,filepath);
        }

        return "下载成功";
    }


    /**
     * 获取response要下载的文件的默认路径
     *
     * @param response
     * @return
     */
    public static String getFilePath(HttpResponse response) {
        String filepath = root + splash;
        String filename = getFileName(response);

        if (filename != null) {
            filepath += filename;
        } else {
            filepath += getRandomFileName();
        }
        return filepath;
    }

    /**
     * 获取response header中Content-Disposition中的filename值
     *
     * @param response
     * @return
     */
    public static String getFileName(HttpResponse response) {
        Header contentHeader = response.getFirstHeader("Content-Disposition");
        String filename = null;
        if (contentHeader != null) {
            HeaderElement[] values = contentHeader.getElements();
            if (values.length == 1) {
                NameValuePair param = values[0].getParameterByName("filename");
                if (param != null) {
                    try {
                        filename = new String(param.getValue().toString().getBytes(), StandardCharsets.UTF_8);
                        //filename=URLDecoder.decode(param.getValue(),"utf-8");
//                        filename = param.getValue();
                    } catch (Exception e) {
                         UniversalException.logError(e);
                    }
                }
            }
        }
        return filename;
    }

    /**
     * 获取随机文件名
     *
     * @return
     */
    public static String getRandomFileName() {
        return String.valueOf(System.currentTimeMillis());
    }


    // 判断请求的资源是否存在
    public boolean existRource(String source) {
        try {
            URL url = new URL(source);
            URLConnection uc = url.openConnection();
            InputStream in = uc.getInputStream();
            if (source.equalsIgnoreCase(uc.getURL().toString())){
                //只要不异常那么就存在
                return true;
            }
            in.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    //跳过证书验证
    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
             UniversalException.logError(e);
        }
        return HttpClients.createDefault();

    }



}
