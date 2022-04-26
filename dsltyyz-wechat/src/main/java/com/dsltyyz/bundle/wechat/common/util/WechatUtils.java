package com.dsltyyz.bundle.wechat.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dsltyyz.bundle.common.util.HttpUtils;
import com.dsltyyz.bundle.wechat.common.constant.WechatAccessUrl;
import com.dsltyyz.bundle.wechat.common.constant.WechatMsgType;
import com.dsltyyz.bundle.wechat.common.model.article.WechatArticle;
import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import com.dsltyyz.bundle.wechat.common.model.draft.WechatDraftDetailVO;
import com.dsltyyz.bundle.wechat.common.model.material.WechatMaterial;
import com.dsltyyz.bundle.wechat.common.model.material.WechatMaterialSend;
import com.dsltyyz.bundle.wechat.common.model.menu.WechatMenu;
import com.dsltyyz.bundle.wechat.common.model.ocr.WechatBankcardResult;
import com.dsltyyz.bundle.wechat.common.model.ocr.WechatBizlicenseResult;
import com.dsltyyz.bundle.wechat.common.model.ocr.WechatIdcardResult;
import com.dsltyyz.bundle.wechat.common.model.openid.WechatMiniOpenId;
import com.dsltyyz.bundle.wechat.common.model.openid.WechatOpenId;
import com.dsltyyz.bundle.wechat.common.model.phone.WechatPhone;
import com.dsltyyz.bundle.wechat.common.model.publish.WechatMass;
import com.dsltyyz.bundle.wechat.common.model.publish.WechatPublish;
import com.dsltyyz.bundle.wechat.common.model.template.*;
import com.dsltyyz.bundle.wechat.common.model.token.WechatToken;
import com.dsltyyz.bundle.wechat.common.model.user.WechatUser;
import com.dsltyyz.bundle.wechat.common.model.user.WechatUserSubscribe;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
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

    /*****************服务号 后台************/
    /**
     * 【后台】获取access_token
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
     * 【后台】获取用户订阅信息
     * 2021年12月27日之后，不再输出头像、昵称信息
     *
     * @param token  访问口令
     * @param openid 唯一标识
     * @return
     */
    public static WechatUserSubscribe getUserSubscribe(String token, String openid) {
        String url = WechatAccessUrl.USER_INFO_URL.replace("ACCESS_TOKEN", token).replace("OPENID", openid);
        return HttpUtils.doGet(url, null, null, new TypeReference<WechatUserSubscribe>() {
        });
    }

    /**
     * 【后台】创建菜单
     *
     * @param token      访问口令
     * @param wechatMenu 微信菜单
     * @return
     */
    public static WechatResult addMenu(String token, WechatMenu wechatMenu) {
        String url = WechatAccessUrl.ADD_MENU_URL.replace("ACCESS_TOKEN", token);
        return HttpUtils.doPostJson(url, null, null, wechatMenu, new TypeReference<WechatUserSubscribe>() {
        });
    }

    /**
     * 【后台】删除菜单
     *
     * @param token 访问口令
     * @return
     */
    public static WechatResult delMenu(String token) {
        String url = WechatAccessUrl.DEL_MENU_URL.replace("ACCESS_TOKEN", token);
        return HttpUtils.doGet(url, null, null, new TypeReference<WechatUserSubscribe>() {
        });
    }

    /**
     * 【后台】新增永久素材
     *
     * @param wechatToken        访问口令
     * @param wechatMaterialSend 素材
     * @return
     */
    public static WechatMaterial addMaterial(WechatToken wechatToken, WechatMaterialSend wechatMaterialSend) {
        String url = WechatAccessUrl.ADD_MATERIAL_URL.replace("ACCESS_TOKEN", wechatToken.getAccess_token()).replace("TYPE", wechatMaterialSend.getType());
        JSONObject jsonObject = new JSONObject();
        if (!StringUtils.isEmpty(wechatMaterialSend.getTitle()) && !StringUtils.isEmpty(wechatMaterialSend.getIntroduction())) {
            jsonObject.put("title", wechatMaterialSend.getTitle());
            jsonObject.put("introduction", wechatMaterialSend.getIntroduction());
        }
        Map<String, Object> param = new HashMap<>();
        if (jsonObject.size() != 0) {
            param.put("description", jsonObject.toJSONString());
        }
        Map<String, File> file = new HashMap<>();
        file.put("media", wechatMaterialSend.getMedia());
        return HttpUtils.doPostFile(url, null, null, param, file, new TypeReference<WechatMaterial>() {
        });
    }

    /**
     * 【后台】删除永久素材
     *
     * @param wechatToken 访问口令
     * @param mediaId     素材的media_id
     * @return
     */
    public static WechatResult delMaterial(WechatToken wechatToken, String mediaId) {
        String url = WechatAccessUrl.DEL_MATERIAL_URL.replace("ACCESS_TOKEN", wechatToken.getAccess_token());

        Map<String, Object> param = new HashMap<>();
        param.put("media_id", mediaId);
        return HttpUtils.doPostJson(url, null, null, param, new TypeReference<WechatResult>() {
        });
    }

    /**
     * 【后台】增加草稿
     *
     * @param wechatToken   访问口令
     * @param wechatArticle 文章
     * @return
     */
    public static WechatMaterial addDraft(WechatToken wechatToken, WechatArticle wechatArticle) {
        String url = WechatAccessUrl.ADD_DRAFT_URL.replace("ACCESS_TOKEN", wechatToken.getAccess_token());

        return HttpUtils.doPostJson(url, null, null, wechatArticle, new TypeReference<WechatMaterial>() {
        });
    }

    /**
     * 【后台】删除草稿
     *
     * @param wechatToken 访问口令
     * @param mediaId     草稿mediaId
     * @return
     */
    public static WechatResult delDraft(WechatToken wechatToken, String mediaId) {
        String url = WechatAccessUrl.DEL_DRAFT_URL.replace("ACCESS_TOKEN", wechatToken.getAccess_token());
        Map<String, Object> param = new HashMap<>();
        param.put("media_id", mediaId);
        return HttpUtils.doPostJson(url, null, null, param, new TypeReference<WechatResult>() {
        });
    }

    /**
     * 【后台】获取草稿
     *
     * @param wechatToken 访问口令
     * @param mediaId     草稿mediaId
     * @return
     */
    public static WechatDraftDetailVO getDraft(WechatToken wechatToken, String mediaId) {
        String url = WechatAccessUrl.GET_DRAFT_URL.replace("ACCESS_TOKEN", wechatToken.getAccess_token());
        Map<String, Object> param = new HashMap<>();
        param.put("media_id", mediaId);
        return HttpUtils.doPostJson(url, null, null, param, new TypeReference<WechatDraftDetailVO>() {
        });
    }

    /**
     * 【后台】增加发布草稿
     *
     * @param wechatToken 访问口令
     * @param mediaId     草稿mediaId
     * @return
     */
    public static WechatPublish addPublish(WechatToken wechatToken, String mediaId) {
        String url = WechatAccessUrl.ADD_PUBLISH_URL.replace("ACCESS_TOKEN", wechatToken.getAccess_token());
        Map<String, Object> param = new HashMap<>();
        param.put("media_id", mediaId);
        return HttpUtils.doPostJson(url, null, null, param, new TypeReference<WechatPublish>() {
        });
    }

    /**
     * 【后台】获取发布草稿
     *
     * @param wechatToken 访问口令
     * @param publishId   发布publishId
     * @return
     */
    public static WechatPublish getPublish(WechatToken wechatToken, String publishId) {
        String url = WechatAccessUrl.GET_PUBLISH_URL.replace("ACCESS_TOKEN", wechatToken.getAccess_token());
        Map<String, Object> param = new HashMap<>();
        param.put("publish_id", publishId);
        return HttpUtils.doPostJson(url, null, null, param, new TypeReference<WechatPublish>() {
        });
    }

    /**
     * 【后台】删除发布草稿
     *
     * @param wechatToken 访问口令
     * @param articleId   发布成功时返回的articleId
     * @param index       要删除的文章在图文消息中的位置，第一篇编号为1，该字段不填或填0会删除全部文章
     * @return
     */
    public static WechatResult delPublish(WechatToken wechatToken, String articleId, Integer index) {
        String url = WechatAccessUrl.DEL_PUBLISH_URL.replace("ACCESS_TOKEN", wechatToken.getAccess_token());
        Map<String, Object> param = new HashMap<>();
        param.put("article_id", articleId);
        param.put("index", index);
        return HttpUtils.doPostJson(url, null, null, param, new TypeReference<WechatResult>() {
        });
    }

    /**
     * 【后台】增加群发草稿
     *
     * @param wechatToken
     * @param mediaId
     * @return
     */
    public static WechatMass addBatchPublish(WechatToken wechatToken, String mediaId) {
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> filter = new HashMap<>();
        filter.put("is_to_all", true);
        param.put("filter", filter);
        Map<String, Object> mpnews = new HashMap<>();
        mpnews.put("media_id", mediaId);
        param.put("mpnews", mpnews);
        //群发的消息类型，图文消息为mpnews，文本消息为text，语音为voice，音乐为music，图片为image，视频为video，卡券为wxcard
        param.put("msgtype", WechatMsgType.MPNEWS);
        //图文消息被判定为转载时，是否继续群发。 1为继续群发（转载），0为停止群发。 该参数默认为0。
        param.put("send_ignore_reprint", "1");
        return addMessageMass(wechatToken, param);
    }

    /**
     * 【后台】增加群发
     *
     * @param wechatToken
     * @param param       参数
     * @return
     */
    public static WechatMass addMessageMass(WechatToken wechatToken, Map<String, Object> param) {
        String url = WechatAccessUrl.MESSAGE_MASS_SENDALL_URL.replace("ACCESS_TOKEN", wechatToken.getAccess_token());
        return HttpUtils.doPostJson(url, null, null, param, new TypeReference<WechatMass>() {
        });
    }

    /**
     * 【后台】删除群发
     *
     * @param wechatToken
     * @param msgId       发送出去的消息ID
     * @param articleIdx  要删除的文章在图文消息中的位置，第一篇编号为1，该字段不填或填0会删除全部文章
     * @return
     */
    public static WechatResult delMessageMass(WechatToken wechatToken, Integer msgId, Integer articleIdx) {
        String url = WechatAccessUrl.MESSAGE_MASS_DELETE_URL.replace("ACCESS_TOKEN", wechatToken.getAccess_token());
        Map<String, Object> param = new HashMap<>();
        param.put("msg_id", msgId);
        if (articleIdx != null) {
            param.put("article_idx", articleIdx);
        }
        return HttpUtils.doPostJson(url, null, null, param, new TypeReference<WechatResult>() {
        });
    }

    /**
     * 【后台】获取群发状态
     *
     * @param wechatToken
     * @param msgId       发送出去的消息ID
     * @return
     */
    public static WechatMass getMessageMass(WechatToken wechatToken, Integer msgId) {
        String url = WechatAccessUrl.GET_MESSAGE_MASS_URL.replace("ACCESS_TOKEN", wechatToken.getAccess_token());
        Map<String, Object> param = new HashMap<>();
        param.put("msg_id", msgId);
        return HttpUtils.doPostJson(url, null, null, param, new TypeReference<WechatMass>() {
        });
    }

    /**
     * 【后台】获取模板列表 未测试
     *
     * @param wechatToken 访问口令
     * @return
     */
    public static List<WechatTemplate> getAllPrivateTemplate(WechatToken wechatToken) {
        String url = WechatAccessUrl.TEMPLATE_GET_ALL_PRIVATE_TEMPLATE_MESSAGE_URL.replace("ACCESS_TOKEN", wechatToken.getAccess_token());
        WechatTemplateResult wechatTemplateResult = HttpUtils.doGet(url, null, null, new TypeReference<WechatTemplateResult>() {
        });
        return wechatTemplateResult.getTemplate_list();
    }

    /**
     * 【后台】获取模板列表 未测试
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

    /*****************服务号 前台************/
    /**
     * 【网站】【前台】通过code换取网页授权openid
     *
     * @param appId     第三方用户唯一凭证
     * @param appSecret 第三方用户唯一凭证密钥
     * @param code      请求码
     * @return
     */
    public static WechatOpenId getOpenId(String appId, String appSecret, String code) {
        String url = WechatAccessUrl.SNS_ACCESS_TOKEN_URL.replace("APPID", appId).replace("APPSECRET", appSecret).replace("CODE", code);
        return HttpUtils.doGet(url, null, null, new TypeReference<WechatOpenId>() {
        });
    }

    /**
     * 【网站】【前台】获取用户信息
     *
     * @param token  访问口令
     * @param openid 唯一标识
     * @return
     */
    public static WechatUser getUserInfo(String token, String openid) {
        String url = WechatAccessUrl.SNS_USER_INFO_URL.replace("ACCESS_TOKEN", token).replace("OPENID", openid);
        return HttpUtils.doGet(url, null, null, new TypeReference<WechatUser>() {
        });
    }

    /*******************小程序***********/
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
     * 【小程序】获取用户手机号
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

    /**
     * 【小程序】订阅消息获取当前帐号下的个人模板列表
     *
     * @param token
     * @return
     */
    public static List<WechatMiniTemplate> getNewTmplList(String token) {
        String url = WechatAccessUrl.NEW_TMPL_LIST_URL.replace("ACCESS_TOKEN", token);
        WechatMiniTemplateResult wechatMiniTemplateResult = HttpUtils.doGet(url, null, null, new TypeReference<WechatMiniTemplateResult>() {
        });
        Assert.isTrue(wechatMiniTemplateResult.getErrcode().equals(0L), wechatMiniTemplateResult.getErrmsg());
        return wechatMiniTemplateResult.getData();
    }

    /**
     * 【小程序】发送订阅消息
     * @param token
     * @param wechatMiniTemplateSend
     * @return
     */
    public static WechatResult sendNewTmpl(String token, WechatMiniTemplateSend wechatMiniTemplateSend){
        String url = WechatAccessUrl.SEND_NEW_TMPL_URL.replace("ACCESS_TOKEN", token);
        return HttpUtils.doPostJson(url, null, null,wechatMiniTemplateSend, new TypeReference<WechatResult>() {
        });
    }

    /*******************OCR***********/
    /**
     * 获取银行卡号
     * @param token
     * @param type
     * @param imgUrl
     * @param img
     * @return
     */
    public static WechatBankcardResult getBankcard(String token, String type, String imgUrl, File img){
        String url = WechatAccessUrl.OCR_BANKCARD_URL;
        Map<String, Object> query = new HashMap<>();
        Map<String, File> file = new HashMap<>();
        query.put("access_token", token);
        query.put("type", type);
        if(!StringUtils.isEmpty(imgUrl)){
            query.put("img_url", imgUrl);
        }else{
            file.put("img", img);
        }
        return HttpUtils.doPostFile(url, null, query, null,file, new TypeReference<WechatBankcardResult>() {
        });
    }

    /**
     * 获取身份证号
     * @param token
     * @param type
     * @param imgUrl
     * @param img
     * @return
     */
    public static WechatIdcardResult getIdcard(String token, String type, String imgUrl, File img){
        String url = WechatAccessUrl.OCR_IDCARD_URL;
        Map<String, Object> query = new HashMap<>();
        Map<String, File> file = new HashMap<>();
        query.put("access_token", token);
        query.put("type", type);
        if(!StringUtils.isEmpty(imgUrl)){
            query.put("img_url", imgUrl);
        }else{
            file.put("img", img);
        }
        return HttpUtils.doPostFile(url, null, query, null,file, new TypeReference<WechatIdcardResult>() {
        });
    }

    /**
     * 获取营业执照
     * @param token
     * @param imgUrl
     * @param img
     * @return
     */
    public static WechatBizlicenseResult getBizLicense(String token, String imgUrl, File img){
        String url = WechatAccessUrl.OCR_BIZLICENSE_URL;
        Map<String, Object> query = new HashMap<>();
        Map<String, File> file = new HashMap<>();
        query.put("access_token", token);
        if(!StringUtils.isEmpty(imgUrl)){
            query.put("img_url", imgUrl);
        }else{
            file.put("img", img);
        }
        return HttpUtils.doPostFile(url, null, query, null,file, new TypeReference<WechatBizlicenseResult>() {
        });
    }
}
