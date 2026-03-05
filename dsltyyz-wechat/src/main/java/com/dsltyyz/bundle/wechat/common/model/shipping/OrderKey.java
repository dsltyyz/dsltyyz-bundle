package com.dsltyyz.bundle.wechat.common.model.shipping;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 * 订单，需要上传物流信息的订单
 *
 * @author: dsltyyz
 * @date: 2026-3-5
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderKey implements Serializable {

    /**
     * 当订单单号类型，用于确认需要上传详情的订单。
     * 枚举值1，使用下单商户号和商户侧单号； WechatOrderNumberType.OUT_TRADE_NO
     * 枚举值2，使用微信支付单号。 WechatOrderNumberType.TRANSACTION_ID
     */
    private Integer order_number_type;

    /**
     * 当order_number_type=2时 原支付交易对应的微信订单号
     */
    private String transaction_id;

    /**
     * 当order_number_type=1时 支付下单商户的商户号，由微信支付生成并下发。如有支付服务商，传入服务商商户号。
     */
    private String mchid;

    /**
     * 当order_number_type=1时 商户系统内部订单号，只能是数字、大小写字母`_-*`且在同一个商户号下唯一
     */
    private String out_trade_no;


}
