package com.dsltyyz.bundle.wechat.common.constant;

/**
 * Description:
 * 微信访问地址
 * https://developers.weixin.qq.com/doc/
 * @author: dsltyyz
 * @date: 2020-12-9
 */
public interface WechatAccessUrl {

    /*****************服务器************/
    /**
     * 【服务器】通过appid和appsecret获取token
     */
    String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    /*****************服务号************/
    /**
     * 【服务号】微信浏览器网页授权
     * 默认拥有scope参数中的snsapi_base和snsapi_userinfo
     */
    String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

    /**
     * 【服务号】获取模板列表
     */
    String TEMPLATE_GET_ALL_PRIVATE_TEMPLATE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN";

    /**
     * 【服务号】发送模板消息
     */
    String TEMPLATE_SEND_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    /*****************网站*************/
    /**
     * 【网站】二维码扫码授权
     * scope 应用授权作用域，拥有多个作用域用逗号（,）分隔，网页应用目前仅填写snsapi_login
     */
    String QRCONNECT_URL = "https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

    /**
     * 【网站】通过code换取网页授权openid,access_token
     *  前置: QRCONNECT_URL,AUTHORIZE_URL
     */
    String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=APPSECRET&code=CODE&grant_type=authorization_code";

    /**
     *【服务号】通过access_token和openid获取用户信息
     *
     */
    String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    /*******************小程序***********/
    /**
     *【小程序】通过用户code获取用户openid及session_key
     */
    String CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=APPSECRET&js_code=JSCODE&grant_type=authorization_code";

    /**
     * 【小程序】获取用户手机号
     */
    String USER_PHONE_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=ACCESS_TOKEN";
}
