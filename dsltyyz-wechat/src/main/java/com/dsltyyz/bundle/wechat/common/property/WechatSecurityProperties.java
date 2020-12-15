package com.dsltyyz.bundle.wechat.common.property;

import lombok.Data;

/**
 * Description:
 * 微信账号属性
 *
 * @author: dsltyyz
 * @date: 2019/11/19
 */
@Data
public class WechatSecurityProperties {

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 应用密码
     */
    private String appSecret;

}
