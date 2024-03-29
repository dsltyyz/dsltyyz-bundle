package com.dsltyyz.bundle.wechat.common.util;

import com.alibaba.fastjson.JSONObject;
import com.dsltyyz.bundle.common.util.UUIDUtils;
import com.dsltyyz.bundle.wechat.common.constant.WechatPayFeeType;
import com.dsltyyz.bundle.wechat.common.constant.WechatPayType;
import com.dsltyyz.bundle.wechat.common.model.pay.WechatPayConfig;
import com.dsltyyz.bundle.wechat.common.model.pay.WechatPayOrder;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * 微信支付工具类
 * https://pay.weixin.qq.com/wiki/doc/api/index.html
 *
 * @author: dsltyyz
 * @date: 2019-11-12
 */
@Slf4j
public class WechatPayUtils {

    private static String SUCCESS = "SUCCESS";
    private static String RETURN_CODE = "return_code";
    private static String RETURN_MSG = "return_msg";
    private static String PREPAY_ID = "prepay_id";
    private static String CODE_URL = "code_url";
    private static String MWEV_URL = "mweb_url";

    /**
     * 生成jsapi需要的签名
     *
     * @param wechatPayConfig 支付配置
     * @param wechatPayOrder  支付订单信息
     * @return
     */
    public static Map<String, String> unifiedOrderByJsApi(WechatPayConfig wechatPayConfig, WechatPayOrder wechatPayOrder) {
        WXPay wxPay = new WXPay(wechatPayConfig);
        Map<String, String> data = new HashMap<>();
        data.put("appid", wechatPayConfig.getAppID());
        data.put("mch_id", wechatPayConfig.getMchID());
        data.put("device_info", "WEB");
        data.put("nonce_str", UUIDUtils.getUUID());
        data.put("body", wechatPayOrder.getTitle());
        try {
            //对appid,mch_id,device_info,nonce_str,body进行签名
            data.put("sign", WXPayUtil.generateSignature(data, wechatPayConfig.getKey()));
            data.put("sign_type", "MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        data.put("out_trade_no", wechatPayOrder.getOrderId());
        data.put("fee_type", WechatPayFeeType.CNY);
        data.put("total_fee", wechatPayOrder.getFee());
        data.put("spbill_create_ip", wechatPayOrder.getIp());
        data.put("notify_url", wechatPayOrder.getNotifyUrl());
        data.put("trade_type", WechatPayType.JSAPI);
        data.put("openid", wechatPayOrder.getOpenid());
        log.info("预支付：{}", data.toString());
        try {
            Map<String, String> result = wxPay.unifiedOrder(data);
            if (result == null || !SUCCESS.equals(result.get(RETURN_CODE))) {
                log.error(result.get(RETURN_MSG));
                return null;
            }
            if (WXPayUtil.isSignatureValid(result, wechatPayConfig.getKey())) {
                //生成签名
                return WechatPayUtils.generateSignature(wechatPayConfig.getAppID(), result.get(PREPAY_ID), wechatPayConfig.getKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 生成App需要的签名
     *
     * @param wechatPayConfig 支付配置
     * @param wechatPayOrder  支付订单信息
     * @return
     */
    public static Map<String, String> unifiedOrderByApp(WechatPayConfig wechatPayConfig, WechatPayOrder wechatPayOrder) {
        WXPay wxPay = new WXPay(wechatPayConfig);
        Map<String, String> data = new HashMap<>();
        data.put("appid", wechatPayConfig.getAppID());
        data.put("mch_id", wechatPayConfig.getMchID());
        data.put("device_info", "WEB");
        data.put("nonce_str", UUIDUtils.getUUID());
        data.put("body", wechatPayOrder.getTitle());
        try {
            //对appid,mch_id,device_info,nonce_str,body进行签名
            data.put("sign", WXPayUtil.generateSignature(data, wechatPayConfig.getKey()));
            data.put("sign_type", "MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        data.put("out_trade_no", wechatPayOrder.getOrderId());
        data.put("fee_type", wechatPayOrder.getFeeType());
        data.put("total_fee", wechatPayOrder.getFee());
        data.put("spbill_create_ip", wechatPayOrder.getIp());
        data.put("notify_url", wechatPayOrder.getNotifyUrl());
        data.put("trade_type", WechatPayType.APP);
        log.info("预支付：{}", data.toString());
        try {
            Map<String, String> result = wxPay.unifiedOrder(data);
            if (result == null || !SUCCESS.equals(result.get(RETURN_CODE))) {
                log.error(result.get(RETURN_MSG));
                return null;
            }
            if (WXPayUtil.isSignatureValid(result, wechatPayConfig.getKey())) {
                //生成签名
                return WechatPayUtils.generateSignatureApp(wechatPayConfig.getAppID(), wechatPayConfig.getMchID(), result.get(PREPAY_ID), wechatPayConfig.getKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 生成微信native链接
     *
     * @param wechatPayConfig 支付配置
     * @param wechatPayOrder  支付订单信息
     * @return
     */
    public static String unifiedOrderByNative(WechatPayConfig wechatPayConfig, WechatPayOrder wechatPayOrder) {
        WXPay wxPay = new WXPay(wechatPayConfig);
        Map<String, String> data = new HashMap<>();
        data.put("appid", wechatPayConfig.getAppID());
        data.put("mch_id", wechatPayConfig.getMchID());
        data.put("device_info", "WEB");
        data.put("nonce_str", UUIDUtils.getUUID());
        data.put("body", wechatPayOrder.getTitle());
        try {
            //对appid,mch_id,device_info,nonce_str,body进行签名
            data.put("sign", WXPayUtil.generateSignature(data, wechatPayConfig.getKey()));
            data.put("sign_type", "MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        data.put("out_trade_no", wechatPayOrder.getOrderId());
        data.put("fee_type", WechatPayFeeType.CNY);
        data.put("total_fee", wechatPayOrder.getFee());
        data.put("spbill_create_ip", wechatPayOrder.getIp());
        data.put("notify_url", wechatPayOrder.getNotifyUrl());
        data.put("trade_type", WechatPayType.NATIVE);
        log.info("预支付：{}", data.toString());
        try {
            Map<String, String> result = wxPay.unifiedOrder(data);
            if (result == null || !SUCCESS.equals(result.get(RETURN_CODE))) {
                log.error(result.get(RETURN_MSG));
                return null;
            }
            if (WXPayUtil.isSignatureValid(result, wechatPayConfig.getKey())) {
                return result.get(CODE_URL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 生成微信支付H5外部跳转链接
     *
     * @param wechatPayConfig 支付配置
     * @param wechatPayOrder  支付订单信息
     * @return
     */
    public static String unifiedOrderByH5(WechatPayConfig wechatPayConfig, WechatPayOrder wechatPayOrder) {
        WXPay wxPay = new WXPay(wechatPayConfig);
        Map<String, String> data = new HashMap<>();
        data.put("appid", wechatPayConfig.getAppID());
        data.put("mch_id", wechatPayConfig.getMchID());
        data.put("device_info", "WEB");
        data.put("nonce_str", UUIDUtils.getUUID());
        data.put("body", wechatPayOrder.getTitle());
        try {
            //对appid,mch_id,device_info,nonce_str,body进行签名
            data.put("sign", WXPayUtil.generateSignature(data, wechatPayConfig.getKey()));
            data.put("sign_type", "MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        data.put("out_trade_no", wechatPayOrder.getOrderId());
        data.put("fee_type", WechatPayFeeType.CNY);
        data.put("total_fee", wechatPayOrder.getFee());
        data.put("spbill_create_ip", wechatPayOrder.getIp());
        data.put("notify_url", wechatPayOrder.getNotifyUrl());
        data.put("trade_type", WechatPayType.H5);
        data.put("scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://www.dsltyyz.com\",\"wap_name\": \"微信支付\"}}");
        log.info("预支付：{}", data.toString());
        try {
            Map<String, String> result = wxPay.unifiedOrder(data);
            if (result == null || !SUCCESS.equals(result.get(RETURN_CODE))) {
                log.error(result.get(RETURN_MSG));
                return null;
            }
            if (WXPayUtil.isSignatureValid(result, wechatPayConfig.getKey())) {
                return result.get(MWEV_URL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 查看订单信息
     *
     * @param wechatPayConfig
     * @param id              微信的订单号
     * @return
     */
    public static JSONObject getUnifiedOrderById(WechatPayConfig wechatPayConfig, String id) {
        try {
            WXPay wxPay = new WXPay(wechatPayConfig);
            HashMap<String, String> data = new HashMap<>();
            data.put("transaction_id", id);
            return JSONObject.parseObject(JSONObject.toJSONString(wxPay.orderQuery(data)));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 查看订单信息
     *
     * @param wechatPayConfig
     * @param outTradeNo      商户系统内部订单号
     * @return
     */
    public static JSONObject getUnifiedOrderByOutTradeNo(WechatPayConfig wechatPayConfig, String outTradeNo) {
        try {
            WXPay wxPay = new WXPay(wechatPayConfig);
            HashMap<String, String> data = new HashMap<>();
            data.put("out_trade_no", outTradeNo);
            return JSONObject.parseObject(JSONObject.toJSONString(wxPay.orderQuery(data)));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 申请退款
     *
     * @param wechatPayConfig 支付配置
     * @param id              微信订单号
     * @param totalFee        总金额
     * @param refundFee       退款金额
     * @param notifyUrl       通知URL
     * @return
     */
    public static JSONObject applyRefundById(WechatPayConfig wechatPayConfig, String id, String totalFee, String refundFee, String notifyUrl) {
        try {
            WXPay wxPay = new WXPay(wechatPayConfig);
            HashMap<String, String> data = new HashMap<>();
            data.put("transaction_id", id);
            data.put("out_refund_no", id);
            data.put("total_fee", totalFee);
            data.put("refund_fee", refundFee);
            data.put("refund_fee_type", WechatPayFeeType.CNY);
            data.put("op_user_id", wechatPayConfig.getMchID());
            if (!StringUtils.isEmpty(notifyUrl)) {
                data.put("notify_url", notifyUrl);
            }
            return JSONObject.parseObject(JSONObject.toJSONString(wxPay.refund(data)));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 申请退款
     *
     * @param wechatPayConfig 支付配置
     * @param outTradeNo      系统订单号
     * @param totalFee        总金额
     * @param refundFee       退款金额
     * @param notifyUrl       通知URL
     * @return
     */
    public static JSONObject applyRefundByOutTradeNo(WechatPayConfig wechatPayConfig, String outTradeNo, String totalFee, String refundFee, String notifyUrl) {
        try {
            WXPay wxPay = new WXPay(wechatPayConfig);
            HashMap<String, String> data = new HashMap<>();
            data.put("out_trade_no", outTradeNo);
            data.put("out_refund_no", outTradeNo);
            data.put("total_fee", totalFee);
            data.put("refund_fee", refundFee);
            data.put("refund_fee_type", WechatPayFeeType.CNY);
            data.put("op_user_id", wechatPayConfig.getMchID());
            if (!StringUtils.isEmpty(notifyUrl)) {
                data.put("notify_url", notifyUrl);
            }
            return JSONObject.parseObject(JSONObject.toJSONString(wxPay.refund(data)));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 生成服务号支付签名
     *
     * @param appId    服务号id
     * @param prepayId 预支付id
     * @param key      支付秘钥
     * @return
     */
    public static Map<String, String> generateSignature(String appId, String prepayId, String key) {
        //生成签名
        try {
            Map<String, String> sign = new HashMap<>();
            sign.put("package", "prepay_id=" + prepayId);
            sign.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            sign.put("nonceStr", UUIDUtils.getUUID());
            sign.put("signType", "MD5");
            sign.put("appId", appId);
            sign.put("paySign", WXPayUtil.generateSignature(sign, key));
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
     * @param key      支付秘钥
     * @return
     */
    public static Map<String, String> generateSignatureApp(String appId, String mchId, String prepayId, String key) {
        //生成签名
        try {
            Map<String, String> sign = new HashMap<>();
            sign.put("appid", appId);
            sign.put("partnerid", mchId);
            sign.put("prepayid", prepayId);
            sign.put("package", "Sign=WXPay");
            sign.put("nonceStr", UUIDUtils.getUUID());
            sign.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            sign.put("sign", WXPayUtil.generateSignature(sign, key));
            log.info("APP签名：{}", sign.toString());
            return sign;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }
    }

}
