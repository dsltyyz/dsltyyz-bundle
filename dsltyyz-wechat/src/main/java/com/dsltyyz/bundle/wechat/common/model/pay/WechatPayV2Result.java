package com.dsltyyz.bundle.wechat.common.model.pay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:
 * 微信支付V2结果
 *
 * @author: dsltyyz
 * @date: 2019-11-07
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WechatPayV2Result implements Serializable {

    /**
     *小程序ID
     */
    private String appid;

    /**
     * 商家数据包
     */
    private String attach;

    /**
     *付款银行
     */
    private String bank_type;

    /**
     *货币种类
     */
    private String fee_type;

    /**
     *是否关注公众账号
     */
    private String is_subscribe;

    /**
     *商户号
     */
    private String mch_id;

    /**
     *随机字符串
     */
    private String nonce_str;

    /**
     *用户标识
     */
    private String openid;

    /**
     *商户订单号
     */
    private String out_trade_no;

    /**
     *业务结果 SUCCESS/FAIL
     */
    private String result_code;

    /**
     *返回状态码 SUCCESS/FAIL
     * 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
     */
    private String return_code;

    /**
     *签名
     */
    private String sign;

    /**
     *支付完成时间
     */
    private String time_end;

    /**
     *订单金额
     */
    private String total_fee;

    /**
     *总代金券金额
     */
    private String coupon_fee;

    /**
     *代金券使用数量
     */
    private String coupon_count;

    /**
     *代金券类型
     */
    private String coupon_type;

    /**
     *代金券ID
     */
    private String coupon_id;

    /**
     *交易类型 JSAPI、NATIVE、APP
     */
    private String trade_type;

    /**
     *微信支付订单号
     */
    private String transaction_id;

}