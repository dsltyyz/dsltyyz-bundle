package com.dsltyyz.bundle.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dsltyyz.bundle.common.constant.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
public class HttpUtils {

    /**
     * 发送get请求
     *
     * @param url
     * @param header
     * @param params
     * @param typeReference
     * @return
     */
    public static <T> T doGet(String url, Map<String, String> header, Map<String, Object> params, TypeReference<T> typeReference) {
        String result = doGet(url, header, params);
        if (null == result) {
            return null;
        }
        return JSONObject.parseObject(result, typeReference);
    }

    /**
     * 发送get请求
     *
     * @param url
     * @param header
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, String> header, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 由客户端执行(发送)Get请求
        try {
            // 创建Get请求
            HttpGet httpGet = new HttpGet(url + buildUrlParam(params));
            if (null != header) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
            // 响应模型
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            System.out.println(result);
            if (HttpStatus.OK == response.getStatusLine().getStatusCode()) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeHttpClient(httpClient);
        }
        return null;
    }

    /**
     * 发送get请求InputStream
     *
     * @param url
     * @param header
     * @param params
     * @return
     */
    public static InputStream doGetInputStream(String url, Map<String, String> header, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 由客户端执行(发送)Get请求
        try {
            // 创建Get请求
            HttpGet httpGet = new HttpGet(url + buildUrlParam(params));
            if (null != header) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
            // 响应模型
            CloseableHttpResponse response = httpClient.execute(httpGet);
            return response.getEntity().getContent();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeHttpClient(httpClient);
        }
        return null;
    }

    /**
     * 发送post请求
     *
     * @param url
     * @param header
     * @param params
     * @param typeReference
     * @return
     */
    public static <T> T doPost(String url, Map<String, String> header, Map<String, Object> params, TypeReference<T> typeReference) {
        String result = doPost(url, header, params);
        if (null == result) {
            return null;
        }
        return JSONObject.parseObject(result, typeReference);
    }

    /**
     * 发送post请求
     *
     * @param url
     * @param header
     * @param params
     * @return
     */
    public static String doPost(String url, Map<String, String> header, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 由客户端执行(发送)Post请求
        try {
            // 创建Post请求
            HttpPost httpPost = new HttpPost(url);
            if (null != header) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            httpPost.setEntity(buildFormEntity(params));
            // 响应模型
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            System.out.println(result);
            if (HttpStatus.OK == response.getStatusLine().getStatusCode()) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeHttpClient(httpClient);
        }
        return null;
    }

    /**
     * 发送json数据 post请求
     *
     * @param url
     * @param header
     * @param object
     * @param typeReference
     * @return
     */
    public static <T> T doPostJson(String url, Map<String, String> header, Object object, TypeReference<T> typeReference) {
        String result = doPostJson(url, header, object);
        if (null == result) {
            return null;
        }
        return JSONObject.parseObject(result, typeReference);
    }

    /**
     * 发送json数据 post请求
     *
     * @param url
     * @param header
     * @param object
     * @return
     */
    public static String doPostJson(String url, Map<String, String> header, Object object) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 由客户端执行(发送)Post请求
        try {
            // 创建Post请求
            HttpPost httpPost = new HttpPost(url);
            if (null != header) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            //第三步：给httpPost设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(JSONObject.toJSONString(object), "utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(requestEntity);

            // 响应模型
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            System.out.println(result);
            if (HttpStatus.OK == response.getStatusLine().getStatusCode()) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeHttpClient(httpClient);
        }
        return null;
    }

    /**
     * post提交文件流
     *
     * @param url
     * @param header
     * @param param
     * @param typeReference
     * @return
     */
    public static <T> T doPostInputStream(String url, Map<String, String> header, Map<String, InputStream> param, TypeReference<T> typeReference) {
        String result = doPostInputStream(url, header, param);
        if (null == result) {
            return null;
        }
        return JSONObject.parseObject(result, typeReference);
    }

    /**
     * post提交文件流
     *
     * @param url
     * @param header
     * @param param
     * @return
     */
    public static String doPostInputStream(String url, Map<String, String> header, Map<String, InputStream> param) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 由客户端执行(发送)Post请求
        try {
            // 创建Post请求
            HttpPost httpPost = new HttpPost(url);
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
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            System.out.println(result);
            if (HttpStatus.OK == response.getStatusLine().getStatusCode()) {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeHttpClient(httpClient);
        }
        return null;
    }

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

    /**
     * 关闭HttpClient
     *
     * @param httpClient
     */
    private static void closeHttpClient(CloseableHttpClient httpClient) {
        try {
            // 释放资源
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
