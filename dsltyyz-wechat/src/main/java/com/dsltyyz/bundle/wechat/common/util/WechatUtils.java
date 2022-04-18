package com.dsltyyz.bundle.wechat.common.util;

import com.alibaba.fastjson.TypeReference;
import com.dsltyyz.bundle.common.util.HttpUtils;
import com.dsltyyz.bundle.wechat.common.constant.WechatAccessUrl;
import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import com.dsltyyz.bundle.wechat.common.model.openid.WechatMiniOpenId;
import com.dsltyyz.bundle.wechat.common.model.openid.WechatOpenId;
import com.dsltyyz.bundle.wechat.common.model.phone.WechatPhone;
import com.dsltyyz.bundle.wechat.common.model.template.WechatTemplate;
import com.dsltyyz.bundle.wechat.common.model.template.WechatTemplateSend;
import com.dsltyyz.bundle.wechat.common.model.token.WechatToken;
import com.dsltyyz.bundle.wechat.common.model.user.WechatUser;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * 微信服务号工具类
 *
 * @author: dsltyyz
 * @date: 2019-11-07
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
     * 【服务器】获取模板列表 未测试
     *
     * @param wechatToken 访问口令
     * @return
     */
    public static List<WechatTemplate> getAllPrivateTemplate(WechatToken wechatToken) {
        String url = WechatAccessUrl.TEMPLATE_GET_ALL_PRIVATE_TEMPLATE_MESSAGE_URL.replace("ACCESS_TOKEN", wechatToken.getAccess_token());
        return HttpUtils.doGet(url, null, null, new TypeReference<List<WechatTemplate>>() {
        });
    }

    /**
     * 【服务器】获取模板列表 未测试
     *
     * @param wechatToken        访问口令
     * @param wechatTemplateSend 模板信息
     * @return
     */
    public static WechatResult sendTemplate(WechatToken wechatToken, WechatTemplateSend wechatTemplateSend) {
        String url = WechatAccessUrl.TEMPLATE_SEND_MESSAGE_URL.replace("ACCESS_TOKEN", wechatToken.getAccess_token());
        return HttpUtils.doPostJson(url, null, wechatTemplateSend, new TypeReference<WechatResult>() {
        });
    }

    /**
     * 【网站】【服务号】通过code换取网页授权openid
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
     * 【服务号】获取用户信息
     * 2021年12月27日之后，不再输出头像、昵称信息
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
     *
     * @param appId     第三方用户唯一凭证
     * @param appSecret 第三方用户唯一凭证密钥
     * @param jsCode    请求码
     * @return
     */
    public static WechatMiniOpenId getMiniOpenId(String appId, String appSecret, String jsCode) {
        String url = WechatAccessUrl.CODE2SESSION_URL.replace("APPID", appId).replace("APPSECRET", appSecret).replace("JSCODE", jsCode);
        return HttpUtils.doGet(url, null, null, new TypeReference<WechatMiniOpenId>() {
        });
    }


    /**
     * 获取用户手机号
     *
     * @param token
     * @param code
     * @return
     */
    public static WechatPhone getUserPhone(String token, String code) {
        String url = WechatAccessUrl.USER_PHONE_URL.replace("ACCESS_TOKEN", token);
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        return HttpUtils.doPostJson(url, null, null, param, new TypeReference<WechatPhone>() {
        });
    }
}
