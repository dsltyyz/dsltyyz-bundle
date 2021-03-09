package com.dsltyyz.bundle.wechat.common.model.openid;

import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import lombok.Data;

/**
 * Description:
 * 微信openid
 *
 * @author: dsltyyz
 * @since: 2019-11-06
 */
@Data
public class WechatOpenId extends WechatResult {

    /**
     * 获取到的凭证
     */
    private String access_token;

    /**
     * 凭证有效时间，单位：秒
     */
    private Integer expires_in;

    /**
     * 刷新凭证
     */
    private String refresh_token;

    /**
     * 微信用户唯一标识
     */
    private String openid;

    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    private String scope;


}
