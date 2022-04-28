package com.dsltyyz.bundle.aliyun.client.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.dsltyyz.bundle.aliyun.common.bean.AlipayNotifyResult;
import com.dsltyyz.bundle.aliyun.common.bean.AlipayOrder;
import com.dsltyyz.bundle.aliyun.common.properties.PayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * Description:
 * 阿里云Pay客户端
 *
 * @author: dsltyyz
 * @date: 2020-08-27
 */
@Slf4j
public class AliyunPayClient {

    private AlipayClient alipayClient;

    @Resource
    private PayProperties payProperties;

    public AliyunPayClient() {
        log.info("阿里云Pay客户端已加载");
    }

    /**************************************初始化客户端*******************************************/
    @PostConstruct
    public void init() throws AlipayApiException {
        if (PayProperties.KEY_MODE.equals(payProperties.getMode())) {
            alipayClient = new DefaultAlipayClient(payProperties.getServerUrl(), payProperties.getAppId(), payProperties.getPrivateKey(), payProperties.getFormat(), payProperties.getCharset(), payProperties.getAlipayPublicKey(), payProperties.getSignType());
        } else {
            CertAlipayRequest certParams = new CertAlipayRequest();
            BeanUtils.copyProperties(payProperties, certParams);
            alipayClient = new DefaultAlipayClient(certParams);
        }
    }

    /**************************************数据处理*******************************************/
    /**
     * 数据验签
     *
     * @param map
     * @return
     */
    public boolean verifyData(Map<String, String> map) {
        try {
            if (PayProperties.KEY_MODE.equals(payProperties.getMode())) {
                return AlipaySignature.rsaCheckV1(map, payProperties.getAlipayPublicKey(), payProperties.getCharset(), payProperties.getSignType());
            } else {
                return AlipaySignature.rsaCertCheckV1(map, payProperties.getAlipayPublicCertPath(), payProperties.getCharset(), payProperties.getSignType());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 回调数据处理
     *
     * @param map
     * @return
     */
    public AlipayNotifyResult dealData(Map<String, String> map) {
        //1.数据验签
        Assert.isTrue(verifyData(map), "数据验签失败");
        //2.数据组装
        return JSONObject.parseObject(JSONObject.toJSONString(map),AlipayNotifyResult.class);
    }

    /**************************************创建订单*******************************************/
    /**
     * 预创建订单
     *
     * @param alipayOrder 订单
     * @param notifyUrl   服务器异步通知路径
     * @return
     * @throws AlipayApiException
     */
    public String createAlipayTradePrecreate(AlipayOrder alipayOrder, String notifyUrl) throws AlipayApiException {
        // 2、设置请求参数
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        // 服务器异步通知路径
        request.setNotifyUrl(notifyUrl);
        // 封装参数
        request.setBizContent(JSON.toJSONString(alipayOrder));
        // 3、请求支付宝获取结果
        String result;
        if (PayProperties.KEY_MODE.equals(payProperties.getMode())) {
            result = alipayClient.execute(request).getBody();
        } else {
            result = alipayClient.certificateExecute(request).getBody();
        }
        System.out.println(result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject alipayTradePrecreateResponse = jsonObject.getJSONObject("alipay_trade_precreate_response");
        Assert.isTrue(alipayTradePrecreateResponse.getString("code").equals("10000"), alipayTradePrecreateResponse.getString("msg"));
        return alipayTradePrecreateResponse.getString("qr_code");
    }

    /**
     * 创建页面订单
     *
     * @param alipayOrder 订单
     * @param returnUrl   同步通知页面路径
     * @param notifyUrl   服务器异步通知路径
     * @return
     * @throws AlipayApiException
     */
    public String createAlipayTradePagePay(AlipayOrder alipayOrder, String returnUrl, String notifyUrl) throws AlipayApiException {
        alipayOrder.setProduct_code(AlipayOrder.FAST_INSTANT_TRADE_PAY);
        // 2、设置请求参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        // 页面跳转同步通知页面路径
        request.setReturnUrl(returnUrl);
        // 服务器异步通知路径
        request.setNotifyUrl(notifyUrl);
        // 封装参数
        request.setBizContent(JSON.toJSONString(alipayOrder));
        // 3、请求支付宝进行付款，并获取支付结果
        return alipayClient.pageExecute(request).getBody();
    }

    /**
     * 创建APP订单
     *
     * @param alipayOrder 订单
     * @param notifyUrl   服务器异步通知路径
     * @return
     * @throws AlipayApiException
     */
    public String createAlipayTradeAppPay(AlipayOrder alipayOrder, String notifyUrl) throws AlipayApiException {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        // 服务器异步通知路径
        request.setNotifyUrl(notifyUrl);
        // 封装参数
        request.setBizContent(JSON.toJSONString(alipayOrder));
        // 3、请求支付宝进行付款，并获取支付结果
        return alipayClient.sdkExecute(request).getBody();
    }

    /**
     * 创建WAP订单
     *
     * @param alipayOrder 订单
     * @param returnUrl   同步通知页面路径
     * @param notifyUrl   服务器异步通知路径
     * @return
     * @throws AlipayApiException
     */
    public String createAlipayTradeWapPay(AlipayOrder alipayOrder, String returnUrl, String notifyUrl) throws AlipayApiException {
        alipayOrder.setProduct_code(AlipayOrder.QUICK_WAP_WAY);
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        // 页面跳转同步通知页面路径
        request.setReturnUrl(returnUrl);
        // 服务器异步通知路径
        request.setNotifyUrl(notifyUrl);
        // 封装参数
        request.setBizContent(JSON.toJSONString(alipayOrder));
        // 3、请求支付宝进行付款，并获取支付结果
        return alipayClient.pageExecute(request).getBody();
    }

    /*******************************订单查询**************************************/
    /**
     * 根据业务订单号查询
     * @param outTradeNo
     * @return
     */
    public JSONObject getAlipayTradeQuery(String outTradeNo) throws AlipayApiException {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);
        request.setBizContent(bizContent.toString());
        // 3、请求支付宝获取结果
        String result;
        if (PayProperties.KEY_MODE.equals(payProperties.getMode())) {
            result = alipayClient.execute(request).getBody();
        } else {
            result = alipayClient.certificateExecute(request).getBody();
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject alipayTradeQueryResponse = jsonObject.getJSONObject("alipay_trade_query_response");
        Assert.isTrue(alipayTradeQueryResponse.getString("code").equals("10000"), alipayTradeQueryResponse.getString("msg"));
        return alipayTradeQueryResponse;
    }

    /*******************************订单退款**************************************/
    /**
     * 退款
     * @param outTradeNo 订单号
     * @param refundAmount 退款金额
     * @return
     * @throws AlipayApiException
     */
    public JSONObject createAlipayTradeRefund(String outTradeNo, String refundAmount) throws AlipayApiException {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);
        bizContent.put("refund_amount", refundAmount);
        bizContent.put("out_request_no", outTradeNo);
        request.setBizContent(bizContent.toString());
        request.setBizContent(bizContent.toString());
        // 3、请求支付宝获取结果
        String result;
        if (PayProperties.KEY_MODE.equals(payProperties.getMode())) {
            result = alipayClient.execute(request).getBody();
        } else {
            result = alipayClient.certificateExecute(request).getBody();
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject alipayTradeQueryResponse = jsonObject.getJSONObject("alipay_trade_refund_response");
        Assert.isTrue(alipayTradeQueryResponse.getString("code").equals("10000"), alipayTradeQueryResponse.getString("msg"));
        return alipayTradeQueryResponse;
    }

    /**
     * 退款查询
     * @param outTradeNo 订单号
     * @return
     * @throws AlipayApiException
     */
    public JSONObject getAlipayTradeFastpayRefundQuery(String outTradeNo) throws AlipayApiException {
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);
        bizContent.put("out_request_no", outTradeNo);
        request.setBizContent(bizContent.toString());
        request.setBizContent(bizContent.toString());
        // 3、请求支付宝获取结果
        String result;
        if (PayProperties.KEY_MODE.equals(payProperties.getMode())) {
            result = alipayClient.execute(request).getBody();
        } else {
            result = alipayClient.certificateExecute(request).getBody();
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject alipayTradeQueryResponse = jsonObject.getJSONObject("alipay_trade_fastpay_refund_query_response");
        Assert.isTrue(alipayTradeQueryResponse.getString("code").equals("10000"), alipayTradeQueryResponse.getString("msg"));
        return alipayTradeQueryResponse;
    }
}
