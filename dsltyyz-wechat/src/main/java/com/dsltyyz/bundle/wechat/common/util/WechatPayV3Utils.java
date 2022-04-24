package com.dsltyyz.bundle.wechat.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dsltyyz.bundle.common.util.StreamUtils;
import com.dsltyyz.bundle.wechat.common.model.pay.WechatPayOrder;
import com.dsltyyz.bundle.wechat.common.property.WechatPayProperties;
import com.dsltyyz.bundle.wechat.common.property.WechatProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.wxpay.sdk.WXPayUtil;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Map;

/**
 * Description:
 * 【未测试】微信支付工具类 v3
 * https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/pages/index.shtml
 *
 * @author: dsltyyz
 * @date: 2019-11-12
 */
@Slf4j
public class WechatPayV3Utils {

    private static String SUCCESS = "SUCCESS";
    private static String RETURN_CODE = "return_code";
    private static String RETURN_MSG = "return_msg";
    private static String PREPAY_ID = "prepay_id";
    private static String CODE_URL = "code_url";

    public static CloseableHttpClient getHttpClient(WechatProperties wechatProperties) {
        try {
            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(
                    new ByteArrayInputStream(wechatProperties.getPay().getMchPrivateKey().getBytes("utf-8")));
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
                .put("total", wechatPayOrder.getFee());
        rootNode.putObject("payer")
                .put("openid", wechatPayOrder.getOpenid());

        try {
            objectMapper.writeValue(bos, rootNode);
            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            Map<String, String> result = JSONObject.parseObject(StreamUtils.inputStreamToString(response.getEntity().getContent()), new TypeReference<Map<String, String>>() {
            });
            if (result == null || !SUCCESS.equals(result.get(RETURN_CODE))) {
                log.error(result.get(RETURN_MSG));
                return null;
            }
            WechatPayProperties wechatPayConfig = wechatProperties.getPay();
            if (WXPayUtil.isSignatureValid(result, wechatPayConfig.getMchPrivateKey())) {
                //生成签名
                return WechatPayUtils.generateSignature(wechatProperties.getOauth().getAppId(), result.get(PREPAY_ID), wechatPayConfig.getMchPrivateKey());
            }
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
                .put("total", wechatPayOrder.getFee());
        rootNode.putObject("payer")
                .put("openid", wechatPayOrder.getOpenid());

        try {
            objectMapper.writeValue(bos, rootNode);
            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            Map<String, String> result = JSONObject.parseObject(StreamUtils.inputStreamToString(response.getEntity().getContent()), new TypeReference<Map<String, String>>() {
            });
            if (result == null || !SUCCESS.equals(result.get(RETURN_CODE))) {
                log.error(result.get(RETURN_MSG));
                return null;
            }
            if (WXPayUtil.isSignatureValid(result, wechatProperties.getPay().getMchPrivateKey())) {
                return result.get(CODE_URL);
            }
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
    public static Map<String, String> getUnifiedOrderById(WechatProperties wechatProperties, String id) {
        CloseableHttpClient httpClient = getHttpClient(wechatProperties);
        Assert.notNull(httpClient, "请查看微信配置");

        try {
            URIBuilder uriBuilder = new URIBuilder("https://api.mch.weixin.qq.com/v3/pay/transactions/id/ID?mchid=MCHID".replace("ID",id).replace("MCHID", wechatProperties.getPay().getMchId()));
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.addHeader("Accept", "application/json");

            CloseableHttpResponse response = httpClient.execute(httpGet);
            return JSONObject.parseObject(StreamUtils.inputStreamToString(response.getEntity().getContent()), new TypeReference<Map<String, String>>() {
            });
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
    public static Map<String, String> getUnifiedOrderByOutTradeNo(WechatProperties wechatProperties, String outTradeNo) {
        CloseableHttpClient httpClient = getHttpClient(wechatProperties);
        Assert.notNull(httpClient, "请查看微信配置");

        try {
            URIBuilder uriBuilder = new URIBuilder("https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/ID?mchid=MCHID".replace("ID",outTradeNo).replace("MCHID", wechatProperties.getPay().getMchId()));
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.addHeader("Accept", "application/json");

            CloseableHttpResponse response = httpClient.execute(httpGet);
            return JSONObject.parseObject(StreamUtils.inputStreamToString(response.getEntity().getContent()), new TypeReference<Map<String, String>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 申请退款
     *
     * @param wechatProperties 支付配置
     * @param id               订单号
     * @param totalFee         总金额
     * @param refundFee        退款金额
     * @return
     */
    public static Map<String, String> applyRefund(WechatProperties wechatProperties, String id, String totalFee, String refundFee, String notifyUrl) {
        CloseableHttpClient httpClient = getHttpClient(wechatProperties);
        Assert.notNull(httpClient, "请查看微信配置");

        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("mchid", wechatProperties.getPay().getMchId())
                .put("funds_account","AVAILABLE")
                .put("out_trade_no", id)
                .put("out_refund_no", id);
        if(!StringUtils.isEmpty(notifyUrl)){
            rootNode.put("notify_url",notifyUrl);
        }
        rootNode.putObject("amount")
                .put("refund", refundFee)
                .put("total", totalFee)
                .put("currency", "CNY");

        try {
            objectMapper.writeValue(bos, rootNode);
            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            return JSONObject.parseObject(StreamUtils.inputStreamToString(response.getEntity().getContent()), new TypeReference<Map<String, String>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
