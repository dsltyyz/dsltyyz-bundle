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
public class WechatRefundV2Detail implements Serializable {

    /**
     *微信支付订单号
     */
    private String transaction_id;

    /**
     * 商家事务号
     */
    private String out_trade_no;

    /**
     *商户订单号
     */
    private String out_refund_no;

    /**
     *微信退款单号
     */
    private String refund_id;

    /**
     *退款金额
     */
    private String refund_fee;

    /**
     * 退款状态
     */
    private String refund_status;

    /**
     * 清账退款费用
     */
    private String settlement_refund_fee;

    /**
     * 退款时间
     */
    private String success_time;

    /**
     * 退款接收账户
     */
    private String refund_recv_accout;

    /**
     * 退款账户
     */
    private String refund_account;

    /**
     * 总费用
     */
    private String total_fee;

    /**
     * 清账退款总费用
     */
    private String settlement_total_fee;

    /**
     * 退款请求源
     */
    private String refund_request_source;

}