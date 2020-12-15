package com.dsltyyz.bundle.wechat.common.model.pay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Description:
 * 微信数据
 *
 * @author: dsltyyz
 * @date: 2019/11/07
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WechatPayOrder {

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
     */
    private String openid;

}