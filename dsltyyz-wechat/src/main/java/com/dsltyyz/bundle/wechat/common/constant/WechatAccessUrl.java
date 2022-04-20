package com.dsltyyz.bundle.wechat.common.constant;

/**
 * Description:
 * 微信访问地址
 * https://developers.weixin.qq.com/doc/
 * @author: dsltyyz
 * @date: 2020-12-9
 */
public interface WechatAccessUrl {

    /*****************服务号 后台************/
    /**
     * 【后台】通过appid和appsecret获取token
     */
    String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    /**
     *【后台】通过access_token和openid获取用户信息
     *
     */
    String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    /**
     * 【后台】上传素材URL
     */
    String ADD_MATERIAL_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";

    /**
     * 【后台】删除素材URL
     */
    String DEL_MATERIAL_URL = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=ACCESS_TOKEN";

    /**
     * 【后台】新建草稿URL
     */
    String ADD_DRAFT_URL = "https://api.weixin.qq.com/cgi-bin/draft/add?access_token=ACCESS_TOKEN";

    /**
     * 【后台】新建草稿URL
     */
    String DEL_DRAFT_URL = "https://api.weixin.qq.com/cgi-bin/draft/delete?access_token=ACCESS_TOKEN";

    /**
     * 【后台】新建草稿URL
     */
    String GET_DRAFT_URL = "https://api.weixin.qq.com/cgi-bin/draft/get?access_token=ACCESS_TOKEN";

    /**
     * 【后台】新建发布URL
     */
    String ADD_PUBLISH_URL = "https://api.weixin.qq.com/cgi-bin/freepublish/submit?access_token=ACCESS_TOKEN";

    /**
     * 【后台】获取发布URL
     */
    String GET_PUBLISH_URL = "https://api.weixin.qq.com/cgi-bin/freepublish/get?access_token=ACCESS_TOKEN";

    /**
     * 【后台】删除发布URL
     */
    String DEL_PUBLISH_URL = "https://api.weixin.qq.com/cgi-bin/freepublish/delete?access_token=ACCESS_TOKEN";

    /**
     * 【后台】根据标签进行群发
     */
    String MESSAGE_MASS_SENDALL_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";

    /**
     * 【后台】删除群发
     */
    String MESSAGE_MASS_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/delete?access_token=ACCESS_TOKEN";

    /**
     * 【后台】查看群发
     */
    String GET_MESSAGE_MASS_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/get?access_token=ACCESS_TOKEN";

    /**
     * 【后台】获取模板列表
     */
    String TEMPLATE_GET_ALL_PRIVATE_TEMPLATE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN";

    /**
     * 【后台】发送模板消息
     */
    String TEMPLATE_SEND_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    /*****************服务号 前台************/
    /**
     * 【网站】【前台】通过code换取网页授权openid,access_token
     *  前置: QRCONNECT_URL,AUTHORIZE_URL
     */
    String SNS_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=APPSECRET&code=CODE&grant_type=authorization_code";

    /**
     *【网站】【前台】通过access_token和openid获取用户信息
     *
     */
    String SNS_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

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
