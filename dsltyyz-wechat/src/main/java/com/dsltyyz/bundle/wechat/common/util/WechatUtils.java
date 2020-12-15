package com.dsltyyz.bundle.wechat.common.util;

import com.alibaba.fastjson.TypeReference;
import com.dsltyyz.bundle.common.util.HttpUtils;
import com.dsltyyz.bundle.wechat.common.constant.WechatAccessUrl;
import com.dsltyyz.bundle.wechat.common.model.openid.WechatMiniOpenId;
import com.dsltyyz.bundle.wechat.common.model.openid.WechatOpenId;
import com.dsltyyz.bundle.wechat.common.model.token.WechatToken;
import com.dsltyyz.bundle.wechat.common.model.user.WechatUser;
import lombok.extern.slf4j.Slf4j;

/**
 * Description:
 * 微信公众号工具类
 *
 * @author: dsltyyz
 * @date: 2019/11/07
 */
@Slf4j
public class WechatUtils {

    /**
     * 【服务器】获取access_token
     *
     * @param appId     第三方用户唯一凭证
     * @param appSecret 第三方用户唯一凭证密钥
     * @return
     */
    public static WechatToken getAccessToken(String appId, String appSecret) {
        String url = WechatAccessUrl.TOKEN_URL.replace("APPID", appId).replace("APPSECRET", appSecret);
        return HttpUtils.doGet(url, null, null, new TypeReference<WechatToken>() {
        });
    }

    /**
     * 【网站】【公众号】通过code换取网页授权openid
     *
     * @param appId     第三方用户唯一凭证
     * @param appSecret 第三方用户唯一凭证密钥
     * @param code      请求码
     * @return
     */
    public static WechatOpenId getOpenId(String appId, String appSecret, String code) {
        String url = WechatAccessUrl.ACCESS_TOKEN_URL.replace("APPID", appId).replace("APPSECRET", appSecret).replace("CODE", code);
        return HttpUtils.doGet(url, null, null, new TypeReference<WechatOpenId>() {
        });
    }

    /**
     * 【网站】【公众号】获取用户信息
     *
     * @param token  访问口令
     * @param openid 唯一标识
     * @return
     */
    public static WechatUser getUserInfo(String token, String openid) {
        String url = WechatAccessUrl.USER_INFO_URL.replace("ACCESS_TOKEN", token).replace("OPENID", openid);
        return HttpUtils.doGet(url, null, null, new TypeReference<WechatUser>() {
        });
    }

    /**
     * 【小程序】通过code换取openid
     * @param appId 第三方用户唯一凭证
     * @param appSecret 第三方用户唯一凭证密钥
     * @param jsCode 请求码
     * @return
     */
    public static WechatMiniOpenId getMiniOpenId(String appId, String appSecret, String jsCode){
        String url = WechatAccessUrl.CODE2SESSION_URL.replace("APPID", appId).replace("APPSECRET", appSecret).replace("JSCODE", jsCode);
        return HttpUtils.doGet(url, null, null, new TypeReference<WechatMiniOpenId>() {
        });
    }

}
