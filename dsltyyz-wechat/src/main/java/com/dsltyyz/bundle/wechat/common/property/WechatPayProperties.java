package com.dsltyyz.bundle.wechat.common.property;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * 微信授权属性
 *
 * @author: dsltyyz
 * @date: 2019-11-19
 */
@Data
public class WechatPayProperties implements Serializable {

    /**
     * 新支付版本
     */
    public static final String V3 = "v3";

    /**
     * 旧支付版本
     */
    public static final String V2 = "v2";

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商户API私钥
     */
    private String mchPrivateKey;

    /**
     * 支付版本 默认V2
     */
    private String version = V2;

    /**
     * 版本为非V3 微信支付平台证书路径
     */
    private String certUrl;

    /**
     * 版本为V3 商户API证书的证书序列号
     */
    private String mchSerialNo;

    /**
     * 版本为V3 apiV3秘钥
     */
    private String apiV3Key;
}
