package com.dsltyyz.bundle.wechat.common.model.shipping;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 * 订单列表信息
 *
 * @author: dsltyyz
 * @date: 2026-3-5
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderListInfo implements Serializable {

    /**
     * 原支付交易对应的微信订单号
     */
    private String transaction_id;

    /**
     * 支付下单商户的商户号，由微信支付生成并下发
     */
    private String merchant_id;

    /**
     * 二级商户号
     */
    private String sub_merchant_id;

    /**
     * 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一
     */
    private String merchant_trade_no;


    /**
     * 以分号连接的该支付单的所有商品描述，当超过120字时自动截断并以 “...” 结尾。
     */
    private String description;

    /**
     * 支付单实际支付金额，整型，单位：分钱。
     */
    private Integer paid_amount;

    /**
     * 支付者openid。
     */
    private String openid;

    /**
     * 交易创建时间，时间戳形式。
     */
    private Integer trade_create_time;


    /**
     * 当支付时间，时间戳形式。
     */
    private Integer pay_time;

    /**
     * 订单状态枚举：(1) 待发货；(2) 已发货；(3) 确认收货；(4) 交易完成；(5) 已退款；(6) 资金待结算。
     */
    private Integer order_state;

    /**
     * 是否处在交易纠纷中。
     */
    private Boolean in_complaint;

    /**
     * 发货信息。
     */
    private OrderListShippingInfo shipping;



}
