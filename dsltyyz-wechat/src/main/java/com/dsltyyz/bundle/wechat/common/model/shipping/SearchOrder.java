package com.dsltyyz.bundle.wechat.common.model.shipping;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 * 搜索订单
 *
 * @author: dsltyyz
 * @date: 2026-3-5
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SearchOrder implements Serializable {

    /**
     * 原支付交易对应的微信订单号
     */
    private String transaction_id;

    /**
     *  支付下单商户的商户号，由微信支付生成并下发
     */
    private String merchant_id;

    /**
     *  二级商户号
     */
    private String sub_merchant_id;

    /**
     *  商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一
     */
    private String merchant_trade_no;


}
