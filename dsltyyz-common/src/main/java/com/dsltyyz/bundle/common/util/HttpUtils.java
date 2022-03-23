package com.dsltyyz.bundle.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dsltyyz.bundle.common.constant.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Http请求
 *
 * @author: dsltyyz
 * @date: 2020-9-24
 */
@Slf4j
public class HttpUtils {

    private static final String HTTPS = "https";

    /****************GET 方法***************/
    /**
     * 发送get请求
     *
     * @param url 访问url
     * @param header 头部参数
     * @param query 指该参数需在请求URL传参
     * @param typeReference
     * @return
     */
    public static <T> T doGet(String url, Map<String, String> header, Map<String, Object> query, TypeReference<T> typeReference) {
        String result = doGet(url, header, query);
        if (null == result) {
            return null;
        }
        return JSONObject.parseObject(result, typeReference);
    }

    /**
     * 发送get请求
     *
     * @param url 访问url
     * @param header 头部参数
     * @param query 指该参数需在请求URL传参
     * @return
     */
    public static String doGet(String url, Map<String, String> header, Map<String, Object> query) {
        HttpClient httpClient = getHttpclient(url);
        // 由客户端执行(发送)Get请求
        try {
            // 创建Get请求
            log.info(url);
            HttpGet httpGet = new HttpGet(url + buildUrlParam(query));
            if (null != header) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
            // 响应模型
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            log.info(result);
            if (HttpStatus.OK == response.getStatusLine().getStatusCode()) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送get请求InputStream
     *
     * @param url 访问url
     * @param header 头部参数
     * @param query 指该参数需在请求URL传参
     * @return
     */
    public static InputStream doGetInputStream(String url, Map<String, String> header, Map<String, Object> query) {
        HttpClient httpClient = getHttpclient(url);
        // 由客户端执行(发送)Get请求
        try {
            // 创建Get请求
            log.info(url);
            HttpGet httpGet = new HttpGet(url + buildUrlParam(query));
            if (null != header) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
            // 响应模型
            HttpResponse response = httpClient.execute(httpGet);
            return response.getEntity().getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /****************POST 方法***************/

    /**
     * 发送post请求
     *
     * @param url 访问url
     * @param header 头部参数
     * @param param 主体参数
     * @param typeReference
     * @return
     */
    public static <T> T doPost(String url, Map<String, String> header, Map<String, Object> param, TypeReference<T> typeReference) {
        return doPost(url, header, null, param, typeReference);
    }

    /**
     * 发送post请求
     *
     * @param url 访问url
     * @param header 头部参数
     * @param query 指该参数需在请求URL传参
     * @param param 主体参数
     * @param typeReference
     * @return
     */
    public static <T> T doPost(String url, Map<String, String> header, Map<String, Object> query, Map<String, Object> param, TypeReference<T> typeReference) {
        String result = doPost(url, header, query, param);
        if (null == result) {
            return null;
        }
        return JSONObject.parseObject(result, typeReference);
    }

    /**
     * 发送post请求
     *
     * @param url 访问url
     * @param header 头部参数
     * @param query 指该参数需在请求URL传参
     * @param param 主体参数
     * @return
     */
    public static String doPost(String url, Map<String, String> header, Map<String, Object> query, Map<String, Object> param) {
        HttpClient httpClient = getHttpclient(url);

        // 由客户端执行(发送)Post请求
        try {
            // 创建Post请求
            log.info(url);
            HttpPost httpPost = new HttpPost(url + buildUrlParam(query));
            if (null != header) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            httpPost.setEntity(buildFormEntity(param));
            // 响应模型
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            log.info(result);
            if (HttpStatus.OK == response.getStatusLine().getStatusCode()) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送json数据 post请求
     *
     * @param url 访问url
     * @param header 头部参数
     * @param param 主体参数
     * @param typeReference
     * @return
     */
    public static <T> T doPostJson(String url, Map<String, String> header, Object param, TypeReference<T> typeReference) {
       return doPostJson(url, header, null, param, typeReference);
    }

    /**
     * 发送json数据 post请求
     *
     * @param url 访问url
     * @param header 头部参数
     * @param query 指该参数需在请求URL传参
     * @param param 主体参数
     * @param typeReference
     * @return
     */
    public static <T> T doPostJson(String url, Map<String, String> header, Map<String, Object> query, Object param, TypeReference<T> typeReference) {
        String result = doPostJson(url, header, query, param);
        if (null == result) {
            return null;
        }
        return JSONObject.parseObject(result, typeReference);
    }

    /**
     * 发送json数据 post请求
     *
     * @param url 访问url
     * @param header 头部参数
     * @param query 指该参数需在请求URL传参
     * @param param 主体参数
     * @return
     */
    public static String doPostJson(String url, Map<String, String> header, Map<String, Object> query, Object param) {
        HttpClient httpClient = getHttpclient(url);

        // 由客户端执行(发送)Post请求
        try {
            // 创建Post请求
            log.info(url);
            HttpPost httpPost = new HttpPost(url+ buildUrlParam(query));
            if (null != header) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            //第三步：给httpPost设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(JSONObject.toJSONString(param), "utf-8");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(requestEntity);

            // 响应模型
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            log.info(result);
            if (HttpStatus.OK == response.getStatusLine().getStatusCode()) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * post提交文件流
     *
     * @param url 访问url
     * @param header 头部参数
     * @param param 主体参数
     * @param typeReference
     * @return
     */
    public static <T> T doPostInputStream(String url, Map<String, String> header, Map<String, InputStream> param, TypeReference<T> typeReference) {
      return doPostInputStream(url, header, null, param, typeReference);
    }

    /**
     * post提交文件流
     *
     * @param url 访问url
     * @param header 头部参数
     * @param query 指该参数需在请求URL传参
     * @param param 主体参数
     * @param typeReference
     * @return
     */
    public static <T> T doPostInputStream(String url, Map<String, String> header, Map<String, Object> query, Map<String, InputStream> param, TypeReference<T> typeReference) {
        String result = doPostInputStream(url, header, query, param);
        if (null == result) {
            return null;
        }
        return JSONObject.parseObject(result, typeReference);
    }

    /**
     * post提交文件流
     *
     * @param url 访问url
     * @param header 头部参数
     * @param query 指该参数需在请求URL传参
     * @param param 主体参数
     * @return
     */
    public static String doPostInputStream(String url, Map<String, String> header, Map<String, Object> query, Map<String, InputStream> param) {
        HttpClient httpClient = getHttpclient(url);

        // 由客户端执行(发送)Post请求
        try {
            // 创建Post请求
            log.info(url);
            HttpPost httpPost = new HttpPost(url+buildUrlParam(query));
            if (null != header) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            for (Map.Entry<String, InputStream> entry : param.entrySet()) {
                builder.addBinaryBody(entry.getKey(), entry.getValue());
            }
            httpPost.setEntity(builder.build());

            // 响应模型
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            log.info(result);
            if (HttpStatus.OK == response.getStatusLine().getStatusCode()) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /****************PUT 方法***************/
    /**
     * 发送put请求
     *
     * @param url 访问url
     * @param header 头部参数
     * @param param 主体参数
     * @param typeReference
     * @return
     */
    public static <T> T doPut(String url, Map<String, String> header, Map<String, Object> param, TypeReference<T> typeReference) {
        return doPut(url, header, null, param, typeReference);
    }

    /**
     * 发送put请求
     *
     * @param url 访问url
     * @param header 头部参数
     * @param query 指该参数需在请求URL传参
     * @param param 主体参数
     * @param typeReference
     * @return
     */
    public static <T> T doPut(String url, Map<String, String> header, Map<String, Object> query, Map<String, Object> param, TypeReference<T> typeReference) {
        String result = doPut(url, header, query, param);
        if (null == result) {
            return null;
        }
        return JSONObject.parseObject(result, typeReference);
    }

    /**
     * 发送post请求
     *
     * @param url 访问url
     * @param header 头部参数
     * @param query 指该参数需在请求URL传参
     * @param param 主体参数
     * @return
     */
    public static String doPut(String url, Map<String, String> header, Map<String, Object> query, Map<String, Object> param) {
        HttpClient httpClient = getHttpclient(url);

        // 由客户端执行(发送)Put请求
        try {
            // 创建Put请求
            log.info(url);
            HttpPut httpPut = new HttpPut(url+buildUrlParam(query));
            if (null != header) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpPut.setHeader(entry.getKey(), entry.getValue());
                }
            }
            httpPut.setEntity(buildFormEntity(param));
            // 响应模型
            HttpResponse response = httpClient.execute(httpPut);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            log.info(result);
            if (HttpStatus.OK == response.getStatusLine().getStatusCode()) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送json数据 put请求
     *
     * @param url 访问url
     * @param header 头部参数
     * @param param 主体参数
     * @param typeReference
     * @return
     */
    public static <T> T doPutJson(String url, Map<String, String> header, Object param, TypeReference<T> typeReference) {
        return doPutJson(url, header, null, param, typeReference);
    }

    /**
     * 发送json数据 put请求
     *
     * @param url 访问url
     * @param header 头部参数
     * @param query 指该参数需在请求URL传参
     * @param param 主体参数
     * @param typeReference
     * @return
     */
    public static <T> T doPutJson(String url, Map<String, String> header, Map<String, Object> query, Object param, TypeReference<T> typeReference) {
        String result = doPutJson(url, header, query, param);
        if (null == result) {
            return null;
        }
        return JSONObject.parseObject(result, typeReference);
    }

    /**
     * 发送json数据 put请求
     *
     * @param url 访问url
     * @param header 头部参数
     * @param query 指该参数需在请求URL传参
     * @param param 主体参数
     * @return
     */
    public static String doPutJson(String url, Map<String, String> header, Map<String, Object> query, Object param) {
        HttpClient httpClient = getHttpclient(url);

        // 由客户端执行(发送)Put请求
        try {
            // 创建Put请求
            log.info(url);
            HttpPut httpPut = new HttpPut(url+buildUrlParam(query));
            if (null != header) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpPut.setHeader(entry.getKey(), entry.getValue());
                }
            }
            //第三步：给httpPut设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(JSONObject.toJSONString(param), "utf-8");
            httpPut.setHeader("Content-type", "application/json");
            httpPut.setEntity(requestEntity);

            // 响应模型
            HttpResponse response = httpClient.execute(httpPut);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            log.info(result);
            if (HttpStatus.OK == response.getStatusLine().getStatusCode()) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /****************DELETE 方法***************/
    /**
     * 发送delete请求
     *
     * @param url 访问url
     * @param header 头部参数
     * @param query 指该参数需在请求URL传参
     * @param typeReference
     * @return
     */
    public static <T> T doDelete(String url, Map<String, String> header, Map<String, Object> query, TypeReference<T> typeReference) {
        String result = doDelete(url, header, query);
        if (null == result) {
            return null;
        }
        return JSONObject.parseObject(result, typeReference);
    }

    /**
     * 发送delete请求
     *
     * @param url 访问url
     * @param header 头部参数
     * @param query 指该参数需在请求URL传参
     * @return
     */
    public static String doDelete(String url, Map<String, String> header, Map<String, Object> query) {
        HttpClient httpClient = getHttpclient(url);
        // 由客户端执行(发送)Delete请求
        try {
            // 创建Delete请求
            log.info(url);
            HttpDelete httpDelete = new HttpDelete(url + buildUrlParam(query));
            if (null != header) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpDelete.setHeader(entry.getKey(), entry.getValue());
                }
            }
            // 响应模型
            HttpResponse response = httpClient.execute(httpDelete);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            log.info(result);
            if (HttpStatus.OK == response.getStatusLine().getStatusCode()) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /****************通用方法***************/
    /**
     * 构建url参数
     *
     * @param params
     * @return
     */
    private static UrlEncodedFormEntity buildFormEntity(Map<String, Object> params) throws UnsupportedEncodingException {
        if (null == params) {
            return null;
        }
        List<NameValuePair> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            list.add(new BasicNameValuePair(entry.getKey(), formatObjectToString(entry.getValue())));
        }
        return new UrlEncodedFormEntity(list, "UTF-8");
    }


    /**
     * 构建url参数
     *
     * @param params
     * @return
     */
    private static String buildUrlParam(Map<String, Object> params) {
        if (null == params) {
            return "";
        }
        StringBuilder sb = new StringBuilder("?");
        for (Map.Entry<String, Object> m : params.entrySet()) {
            if (sb.length() != 1) {
                sb.append("&");
            }
            try {
                String encodeStr = URLEncoder.encode(formatObjectToString(m.getValue()), "UTF-8");
                sb.append(m.getKey()).append("=").append(encodeStr);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 将object转string 仅将Date转为指定类型
     *
     * @param object
     * @return
     */
    private static String formatObjectToString(Object object) {
        if (object instanceof Date) {
            return DateUtils.format((Date) object);
        }
        return String.valueOf(object);
    }

    @SuppressWarnings("deprecation")
    public static HttpClient getHttpclient(String url) {
        Assert.isTrue(!StringUtils.isEmpty(url), "请求URL不能为空");
        HttpClient httpClient = new DefaultHttpClient();
        if (url.indexOf(HTTPS) > -1) {
            sslClient(httpClient);
        }
        return httpClient;
    }

    @SuppressWarnings("deprecation")
    private static void sslClient(HttpClient httpClient) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
                    // ignore
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
                    // ignore
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(sslContext);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (Exception e) {
            throw new IllegalStateException("Create SSLContext error", e);
        }
    }

}
