package com.dsltyyz.bundle.wechat.common.property;

import lombok.Data;

/**
 * Description:
 * 微信授权属性
 *
 * @author: dsltyyz
 * @date: 2019/11/19
 */
@Data
public class WechatPayProperties {

    /**
     * 商家ID
     */
    private String mchId;

    /**
     * 商户密钥
     */
    private String key;

    /**
     * 证书路径
     */
    private String certUrl;
}
