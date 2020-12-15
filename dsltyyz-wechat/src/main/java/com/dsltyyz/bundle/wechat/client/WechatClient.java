package com.dsltyyz.bundle.wechat.client;

import com.dsltyyz.bundle.common.cache.client.CacheClient;
import com.dsltyyz.bundle.wechat.common.model.openid.WechatMiniOpenId;
import com.dsltyyz.bundle.wechat.common.model.openid.WechatOpenId;
import com.dsltyyz.bundle.wechat.common.model.token.WechatToken;
import com.dsltyyz.bundle.wechat.common.model.user.WechatUser;
import com.dsltyyz.bundle.wechat.common.property.WechatProperties;
import com.dsltyyz.bundle.wechat.common.util.WechatUtils;

import javax.annotation.Resource;

/**
 * Description:
 * 微信客户端
 *
 * @author: dsltyyz
 * @date: 2019/11/22
 */
public class WechatClient {

    @Resource
    private WechatProperties wechatProperties;

    @Resource
    private CacheClient cacheClient;

    /**
     * 【服务器】获取access_token
     *
     * @return
     */
    public WechatToken getWechatToken() {
        WechatToken wechatToken = cacheClient.getEntity(wechatProperties.getOauth().getAppId(), WechatToken.class);
        if (null != wechatToken && !wechatToken.isExpired()) {
            return wechatToken;
        }
        wechatToken = WechatUtils.getAccessToken(wechatProperties.getOauth().getAppId(), wechatProperties.getOauth().getAppSecret());
        cacheClient.putEntity(wechatProperties.getOauth().getAppId(), wechatToken);
        return wechatToken;
    }

    /**
     * 【网站】【公众号】根据用户授权码获取用户信息
     *
     * @param code
     * @return
     */
    public WechatUser getUserInfoByCode(String code) {
        //code换取openid及token
        WechatOpenId openId = WechatUtils.getOpenId(wechatProperties.getOauth().getAppId(), wechatProperties.getOauth().getAppSecret(), code);
        //openid及token获取用户信息
        return WechatUtils.getUserInfo(openId.getAccess_token(), openId.getOpenid());
    }

    /**
     * 【网站】【公众号】根据用户openid获取用户信息
     *
     * @param openId
     * @return
     */
    public WechatUser getUserInfo(String openId) {
        WechatToken wechatToken = getWechatToken();
        //openid及token获取用户信息
        return WechatUtils.getUserInfo(wechatToken.getAccess_token(), openId);
    }

    /**
     * 【小程序】通过code换取openid
     *
     * @param jsCode 请求码
     * @return
     */
    public WechatMiniOpenId getMiniOpenId(String jsCode) {
        return WechatUtils.getMiniOpenId(wechatProperties.getOauth().getAppId(), wechatProperties.getOauth().getAppSecret(), jsCode);
    }

}
