package com.dsltyyz.bundle.wechat.common.constant;

/**
 * Description:
 * 发货模式，发货模式枚举值：1、UNIFIED_DELIVERY（统一发货）2、SPLIT_DELIVERY（分拆发货）
 *
 * @author: dsltyyz
 * @date: 2020-12-9
 */
public interface WechatOrderNumberType {

    /**
     * 1 使用下单商户号和商户侧单号
     */
    Integer OUT_TRADE_NO = 1;

    /**
     * 2 使用微信支付单号
     */
    Integer TRANSACTION_ID = 2;



}
