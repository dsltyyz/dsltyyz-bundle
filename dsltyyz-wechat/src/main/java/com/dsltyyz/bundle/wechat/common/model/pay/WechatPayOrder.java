package com.dsltyyz.bundle.wechat.common.model.pay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

/**
 * Description:
 * 微信支付订单
 *
 * @author: dsltyyz
 * @date: 2019-11-07
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WechatPayOrder implements Serializable {

    /**
     * 订单id
     */
    @NonNull
    private String orderId;

    /**
     * 订单标题
     */
    @NonNull
    private String title;

    /**
     * 支付费用 单位 分
     */
    @NonNull
    private String fee;

    /**
     * 支付ip
     */
    private String ip;

    /**
     * 通知
     */
    @NonNull
    private String notifyUrl;

    /**
     * 支付者openid
     * trade_type=JSAPI时（即JSAPI支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识。
     */
    private String openid;

}