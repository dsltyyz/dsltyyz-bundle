package com.dsltyyz.bundle.wechat.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dsltyyz.bundle.common.util.Base64Utils;
import com.dsltyyz.bundle.common.util.FileUtils;
import com.dsltyyz.bundle.common.util.StreamUtils;
import com.dsltyyz.bundle.common.util.UUIDUtils;
import com.dsltyyz.bundle.wechat.common.model.pay.WechatPayOrder;
import com.dsltyyz.bundle.wechat.common.property.WechatPayProperties;
import com.dsltyyz.bundle.wechat.common.property.WechatProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * 微信支付工具类 v3
 * https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/pages/index.shtml
 *
 * @author: dsltyyz
 * @date: 2022-5-6
 */
@Slf4j
public class WechatPayV3Utils {

    private static String RETURN_CODE = "code";
    private static String RETURN_MSG = "message";
    private static String PREPAY_ID = "prepay_id";
    private static String CODE_URL = "code_url";

    public static CloseableHttpClient getHttpClient(WechatProperties wechatProperties) {
        try {
            InputStream inputStream = FileUtils.fileToInputStream(wechatProperties.getPay().getMchPrivateKeyCert());;
            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(inputStream);
            // 获取证书管理器实例
            CertificatesManager certificatesManager = CertificatesManager.getInstance();
            // 向证书管理器增加需要自动更新平台证书的商户信息
            certificatesManager.putMerchant(wechatProperties.getPay().getMchId(), new WechatPay2Credentials(wechatProperties.getPay().getMchId(),
                    new PrivateKeySigner(wechatProperties.getPay().getMchSerialNo(), merchantPrivateKey)), wechatProperties.getPay().getApiV3Key().getBytes(StandardCharsets.UTF_8));
            Verifier verifier = certificatesManager.getVerifier(wechatProperties.getPay().getMchId());
            WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                    .withMerchant(wechatProperties.getPay().getMchId(), wechatProperties.getPay().getMchSerialNo(), merchantPrivateKey)
                    .withValidator(new WechatPay2Validator(verifier));
            return builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 生成jsapi需要的签名
     *
     * @param wechatProperties 微信配置
     * @param wechatPayOrder   支付订单信息
     * @return
     */
    public static Map<String, String> unifiedOrderByJsApi(WechatProperties wechatProperties, WechatPayOrder wechatPayOrder) {
        CloseableHttpClient httpClient = getHttpClient(wechatProperties);
        Assert.notNull(httpClient, "请查看微信配置");

        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("mchid", wechatProperties.getPay().getMchId())
                .put("appid", wechatProperties.getOauth().getAppId())
                .put("description", wechatPayOrder.getTitle())
                .put("notify_url", wechatPayOrder.getNotifyUrl())
                .put("out_trade_no", wechatPayOrder.getOrderId());
        rootNode.putObject("amount")
                .put("total", Integer.valueOf(wechatPayOrder.getFee()))
                .put("currency", wechatPayOrder.getFeeType());
        rootNode.putObject("payer")
                .put("openid", wechatPayOrder.getOpenid());

        try {
            objectMapper.writeValue(bos, rootNode);
            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            Map<String, String> result = JSONObject.parseObject(StreamUtils.inputStreamToString(response.getEntity().getContent()), new TypeReference<Map<String, String>>() {
            });
            if(result.get(RETURN_CODE)!=null){
                log.error(result.get(RETURN_MSG));
                return null;
            }
            WechatPayProperties wechatPayConfig = wechatProperties.getPay();
            //生成签名
            return generateSignature(wechatProperties.getOauth().getAppId(), result.get(PREPAY_ID), wechatPayConfig.getMchPrivateKeyCert());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成jsapi需要的签名
     *
     * @param wechatProperties 微信配置
     * @param wechatPayOrder   支付订单信息
     * @return
     */
    public static Map<String, String> unifiedOrderByApp(WechatProperties wechatProperties, WechatPayOrder wechatPayOrder) {
        CloseableHttpClient httpClient = getHttpClient(wechatProperties);
        Assert.notNull(httpClient, "请查看微信配置");

        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/app");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("mchid", wechatProperties.getPay().getMchId())
                .put("appid", wechatProperties.getOauth().getAppId())
                .put("description", wechatPayOrder.getTitle())
                .put("notify_url", wechatPayOrder.getNotifyUrl())
                .put("out_trade_no", wechatPayOrder.getOrderId());
        rootNode.putObject("amount")
                .put("total", Integer.valueOf(wechatPayOrder.getFee()))
                .put("currency", wechatPayOrder.getFeeType());

        try {
            objectMapper.writeValue(bos, rootNode);
            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            Map<String, String> result = JSONObject.parseObject(StreamUtils.inputStreamToString(response.getEntity().getContent()), new TypeReference<Map<String, String>>() {
            });
            if(result.get(RETURN_CODE)!=null){
                log.error(result.get(RETURN_MSG));
                return null;
            }
            WechatPayProperties wechatPayConfig = wechatProperties.getPay();
            //生成签名
            return generateSignatureApp(wechatProperties.getOauth().getAppId(),wechatProperties.getPay().getMchId(), result.get(PREPAY_ID), wechatPayConfig.getMchPrivateKeyCert());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成微信native链接
     *
     * @param wechatProperties 微信配置
     * @param wechatPayOrder   支付订单信息
     * @return
     */
    public static String unifiedOrderByNative(WechatProperties wechatProperties, WechatPayOrder wechatPayOrder) {
        CloseableHttpClient httpClient = getHttpClient(wechatProperties);
        Assert.notNull(httpClient, "请查看微信配置");

        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/native");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("mchid", wechatProperties.getPay().getMchId())
                .put("appid", wechatProperties.getOauth().getAppId())
                .put("description", wechatPayOrder.getTitle())
                .put("notify_url", wechatPayOrder.getNotifyUrl())
                .put("out_trade_no", wechatPayOrder.getOrderId());
        rootNode.putObject("amount")
                .put("total", Integer.valueOf(wechatPayOrder.getFee()))
                .put("currency", wechatPayOrder.getFeeType());

        try {
            objectMapper.writeValue(bos, rootNode);
            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            Map<String, String> result = JSONObject.parseObject(StreamUtils.inputStreamToString(response.getEntity().getContent()), new TypeReference<Map<String, String>>() {
            });
            if(result.get(RETURN_CODE)!=null){
                log.error(result.get(RETURN_MSG));
                return null;
            }
            return result.get(CODE_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查看订单信息
     *
     * @param wechatProperties
     * @param id 商户系统内部订单号
     * @return
     */
    public static JSONObject getUnifiedOrderById(WechatProperties wechatProperties, String id) {
        CloseableHttpClient httpClient = getHttpClient(wechatProperties);
        Assert.notNull(httpClient, "请查看微信配置");

        try {
            URIBuilder uriBuilder = new URIBuilder("https://api.mch.weixin.qq.com/v3/pay/transactions/id/ID?mchid=MCH".replace("ID",id).replace("MCH", wechatProperties.getPay().getMchId()));
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.addHeader("Accept", "application/json");

            CloseableHttpResponse response = httpClient.execute(httpGet);
            return JSONObject.parseObject(StreamUtils.inputStreamToString(response.getEntity().getContent()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查看订单信息
     *
     * @param wechatProperties
     * @param outTradeNo 商户系统内部订单号
     * @return
     */
    public static JSONObject getUnifiedOrderByOutTradeNo(WechatProperties wechatProperties, String outTradeNo) {
        CloseableHttpClient httpClient = getHttpClient(wechatProperties);
        Assert.notNull(httpClient, "请查看微信配置");

        try {
            URIBuilder uriBuilder = new URIBuilder("https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/ID?mchid=MCH".replace("ID",outTradeNo).replace("MCH", wechatProperties.getPay().getMchId()));
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.addHeader("Accept", "application/json");

            CloseableHttpResponse response = httpClient.execute(httpGet);
            return JSONObject.parseObject(StreamUtils.inputStreamToString(response.getEntity().getContent()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 申请退款
     *
     * @param wechatProperties 支付配置
     * @param id               微信订单号
     * @param totalFee         总金额
     * @param refundFee        退款金额
     * @return
     */
    public static JSONObject applyRefundById(WechatProperties wechatProperties, String id, String totalFee, String refundFee, String notifyUrl) {
        CloseableHttpClient httpClient = getHttpClient(wechatProperties);
        Assert.notNull(httpClient, "请查看微信配置");

        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("funds_account","AVAILABLE")
                .put("transaction_id", id)
                .put("out_refund_no", id);
        if(!StringUtils.isEmpty(notifyUrl)){
            rootNode.put("notify_url",notifyUrl);
        }
        rootNode.putObject("amount")
                .put("refund", Integer.valueOf(refundFee))
                .put("total", Integer.valueOf(totalFee))
                .put("currency", "CNY");

        try {
            objectMapper.writeValue(bos, rootNode);
            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            return JSONObject.parseObject(StreamUtils.inputStreamToString(response.getEntity().getContent()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 申请退款
     *
     * @param wechatProperties 支付配置
     * @param outTradeNo       商户订单号
     * @param totalFee         总金额
     * @param refundFee        退款金额
     * @return
     */
    public static JSONObject applyRefundByOutTradeNo(WechatProperties wechatProperties, String outTradeNo, String totalFee, String refundFee, String notifyUrl) {
        CloseableHttpClient httpClient = getHttpClient(wechatProperties);
        Assert.notNull(httpClient, "请查看微信配置");

        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("funds_account","AVAILABLE")
                .put("out_trade_no", outTradeNo)
                .put("out_refund_no", outTradeNo);
        if(!StringUtils.isEmpty(notifyUrl)){
            rootNode.put("notify_url",notifyUrl);
        }
        rootNode.putObject("amount")
                .put("refund", Integer.valueOf(refundFee))
                .put("total", Integer.valueOf(totalFee))
                .put("currency", "CNY");

        try {
            objectMapper.writeValue(bos, rootNode);
            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            return JSONObject.parseObject(StreamUtils.inputStreamToString(response.getEntity().getContent()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 微信回调解密V3
     * @param apiV3Key
     * @param associatedData
     * @param nonce
     * @param ciphertext
     * @return
     */
    public static String decryptToStringV3(String apiV3Key, String associatedData, String nonce, String ciphertext){
        AesUtil aesUtil = new AesUtil(apiV3Key.getBytes());
        try {
            return aesUtil.decryptToString(associatedData.getBytes(),nonce.getBytes(),ciphertext);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成服务号支付签名
     *
     * @param appId    服务号id
     * @param prepayId 预支付id
     * @param certUrl  证书私钥路径
     * @return
     */
    public static Map<String, String> generateSignature(String appId, String prepayId, String certUrl) {
        //生成签名
        try {
            Map<String, String> sign = new HashMap<>();
            sign.put("appId", appId);
            sign.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            sign.put("nonceStr", UUIDUtils.getUUID());
            sign.put("package", "prepay_id=" + prepayId);
            sign.put("paySign", createSign(Arrays.asList(sign.get("appId"),sign.get("timeStamp"),sign.get("nonceStr"),sign.get("package")), certUrl));
            sign.put("signType", "RSA");
            log.info("签名：{}", sign.toString());
            return sign;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 生成APP支付签名
     *
     * @param appId    应用id
     * @param mchId    商户ID
     * @param prepayId 预支付id
     * @param certUrl  证书私钥路径
     * @return
     */
    public static Map<String, String> generateSignatureApp(String appId, String mchId, String prepayId, String certUrl) {
        //生成签名
        try {
            Map<String, String> sign = new HashMap<>();
            sign.put("appid", appId);
            sign.put("partnerid", mchId);
            sign.put("prepayid", prepayId);
            sign.put("package", "Sign=WXPay");
            sign.put("nonceStr", UUIDUtils.getUUID());
            sign.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            sign.put("paySign", createSign(Arrays.asList(sign.get("appid"),sign.get("timeStamp"),sign.get("nonceStr"),sign.get("prepayid")), certUrl));
            log.info("APP签名：{}", sign.toString());
            return sign;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 对字符串列表进行签名
     * @param list
     * @param certUrl
     * @return
     */
    public static String createSign(List<String> list, String certUrl){
        String s = list.stream().collect(Collectors.joining("\n", "", "\n"));
        InputStream inputStream = FileUtils.fileToInputStream(certUrl);;
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(inputStream);
        Signature sign = null;
        try {
            sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(merchantPrivateKey);
            sign.update(s.getBytes(StandardCharsets.UTF_8));
            return Base64Utils.encode(sign.sign());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
