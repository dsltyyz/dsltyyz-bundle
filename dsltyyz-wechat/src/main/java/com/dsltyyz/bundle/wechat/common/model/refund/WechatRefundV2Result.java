package com.dsltyyz.bundle.wechat.common.model.refund;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:
 * 微信退款V2结果
 *
 * @author: dsltyyz
 * @date: 2019-11-07
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WechatRefundV2Result implements Serializable {

    /**
     * 返回状态码 SUCCESS/FAIL
     * 此字段是通信标识，表示接口层的请求结果，并非退款状态。
     */
    private String return_code;

    /**
     * 返回信息
     */
    private String return_msg;

    /**
     * 公众账号ID
     */
    private String appid;

    /**
     * 商户号
     */
    private String mch_id;

    /**
     * 随机字符串
     */
    private String nonce_str;

    /**
     * 签名
     */
    private String sign;

    /**
     * 业务结果 SUCCESS/FAIL
     */
    private String result_code;

    /**
     * 微信支付订单号
     */
    private String transaction_id;

    /**
     *
     */
    private String out_trade_no;

    /**
     * 商户订单号
     */
    private String out_refund_no;

    /**
     * 微信退款单号
     */
    private String refund_id;

    /**
     * 退款金额
     */
    private String refund_fee;

    /**
     * 现金费用
     */
    private String cash_fee;

    /**
     * 优惠券退款费用
     */
    private String coupon_refund_fee;

    /**
     * 退款渠道
     */
    private String refund_channel;

    /**
     * 总费用
     */
    private String total_fee;

    /**
     * 优惠券退款数量
     */
    private String coupon_refund_count;

    /**
     * 现金退款费用
     */
    private String cash_refund_fee;

}