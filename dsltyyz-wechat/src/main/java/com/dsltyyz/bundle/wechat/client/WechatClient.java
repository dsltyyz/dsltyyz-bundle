package com.dsltyyz.bundle.wechat.client;

import com.dsltyyz.bundle.common.cache.client.CacheClient;
import com.dsltyyz.bundle.common.util.FileUtils;
import com.dsltyyz.bundle.common.util.HttpUtils;
import com.dsltyyz.bundle.wechat.common.constant.WechatMaterialType;
import com.dsltyyz.bundle.wechat.common.model.article.WechatArticle;
import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import com.dsltyyz.bundle.wechat.common.model.material.WechatMaterial;
import com.dsltyyz.bundle.wechat.common.model.material.WechatMaterialSend;
import com.dsltyyz.bundle.wechat.common.model.openid.WechatMiniOpenId;
import com.dsltyyz.bundle.wechat.common.model.openid.WechatOpenId;
import com.dsltyyz.bundle.wechat.common.model.pay.WechatPayConfig;
import com.dsltyyz.bundle.wechat.common.model.pay.WechatPayOrder;
import com.dsltyyz.bundle.wechat.common.model.phone.WechatPhone;
import com.dsltyyz.bundle.wechat.common.model.phone.WechatPhoneInfo;
import com.dsltyyz.bundle.wechat.common.model.publish.WechatPublish;
import com.dsltyyz.bundle.wechat.common.model.template.WechatTemplate;
import com.dsltyyz.bundle.wechat.common.model.template.WechatTemplateSend;
import com.dsltyyz.bundle.wechat.common.model.token.WechatToken;
import com.dsltyyz.bundle.wechat.common.model.user.WechatUser;
import com.dsltyyz.bundle.wechat.common.model.user.WechatUserSubscribe;
import com.dsltyyz.bundle.wechat.common.property.WechatPayProperties;
import com.dsltyyz.bundle.wechat.common.property.WechatProperties;
import com.dsltyyz.bundle.wechat.common.util.WechatPayUtils;
import com.dsltyyz.bundle.wechat.common.util.WechatPayV3Utils;
import com.dsltyyz.bundle.wechat.common.util.WechatUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * 微信客户端
 *
 * @author: dsltyyz
 * @date: 2019-11-22
 */
public class WechatClient {

    @Resource
    private WechatProperties wechatProperties;

    @Resource
    private CacheClient cacheClient;

    /**
     * 【服务器】【后台】获取access_token
     *
     * @return
     */
    public WechatToken getWechatToken() {
        WechatToken wechatToken = cacheClient.getEntity(wechatProperties.getOauth().getAppId(), WechatToken.class);
        if (null != wechatToken) {
            return wechatToken;
        }
        wechatToken = WechatUtils.getAccessToken(wechatProperties.getOauth().getAppId(), wechatProperties.getOauth().getAppSecret());
        cacheClient.putEntity(wechatProperties.getOauth().getAppId(), wechatToken, Long.valueOf(wechatToken.getExpires_in()));
        return wechatToken;
    }

    /**
     * 【服务器】【后台】根据用户openid获取用户信息
     *
     * @param openId
     * @return
     */
    public WechatUserSubscribe getUserSubscribe(String openId) {
        WechatToken wechatToken = getWechatToken();
        //openid及token获取用户信息
        return WechatUtils.getUserSubscribe(wechatToken.getAccess_token(),openId);
    }

    /**
     * 【服务器】【后台】获取该服务号下所有消息模板
     *
     * @return
     */
    public List<WechatTemplate> getAllPrivateTemplate() {
        WechatToken wechatToken = cacheClient.getEntity(wechatProperties.getOauth().getAppId(), WechatToken.class);
        return WechatUtils.getAllPrivateTemplate(wechatToken);
    }

    /**
     * 【服务器】【后台】发送消息模板
     *
     * @param wechatTemplateSend
     * @return
     */
    public WechatResult sendTemplate(WechatTemplateSend wechatTemplateSend) {
        WechatToken wechatToken = cacheClient.getEntity(wechatProperties.getOauth().getAppId(), WechatToken.class);
        return WechatUtils.sendTemplate(wechatToken, wechatTemplateSend);
    }

    /**
     * 【服务器】【后台】新增永久素材
     *
     * @param wechatMaterialSend 素材
     * @return
     */
    public WechatMaterial addMaterial(WechatMaterialSend wechatMaterialSend){
        WechatToken wechatToken = cacheClient.getEntity(wechatProperties.getOauth().getAppId(), WechatToken.class);
        return WechatUtils.addMaterial(wechatToken, wechatMaterialSend);
    }

    /**
     * 【服务器】【后台】删除永久素材
     *
     * @param mediaId     素材的media_id
     * @return
     */
    public WechatResult delMaterial(String mediaId) {
        WechatToken wechatToken = cacheClient.getEntity(wechatProperties.getOauth().getAppId(), WechatToken.class);
        return WechatUtils.delMaterial(wechatToken, mediaId);
    }

    /**
     * 将资源转换为微信永久素材
     * @param sourceUrl
     * @return
     */
    public WechatMaterial convertMaterial(String sourceUrl){
        //网络资源下载
        InputStream inputStream = HttpUtils.doGetInputStream(sourceUrl, null, null);
        //创建本地文件
        File file = new File(sourceUrl.substring(sourceUrl.lastIndexOf("/")+1));
        //文件信息写入
        FileUtils.inputStreamToFile(inputStream, file);
        //微信素材
        WechatMaterialSend wechatMaterialSend = new WechatMaterialSend();
        wechatMaterialSend.setType(WechatMaterialType.IMAGE);
        wechatMaterialSend.setMedia(file);
        WechatMaterial wechatMaterial = addMaterial(wechatMaterialSend);
        //删除本地文件
        file.delete();
        return wechatMaterial;
    }

    /**
     * 【服务器】【后台】增加草稿
     *
     * @param wechatArticle 文章
     * @return
     */
    public WechatMaterial addDraft(WechatArticle wechatArticle) {
        WechatToken wechatToken = cacheClient.getEntity(wechatProperties.getOauth().getAppId(), WechatToken.class);
        return WechatUtils.addDraft(wechatToken, wechatArticle);
    }

    /**
     * 【服务器】【后台】删除草稿
     *
     * @param mediaId     草稿mediaId
     * @return
     */
    public WechatResult delDraft(String mediaId) {
        WechatToken wechatToken = cacheClient.getEntity(wechatProperties.getOauth().getAppId(), WechatToken.class);
        return WechatUtils.delDraft(wechatToken, mediaId);
    }

    /**
     * 【服务器】【后台】增加发布
     *
     * @param mediaId     草稿mediaId
     * @return
     */
    public WechatPublish addPublish(String mediaId) {
        WechatToken wechatToken = cacheClient.getEntity(wechatProperties.getOauth().getAppId(), WechatToken.class);
        return WechatUtils.addPublish(wechatToken, mediaId);
    }

    /**
     * 【服务器】【后台】获取发布
     *
     * @param publishId   发布publishId
     * @return
     */
    public WechatPublish getPublish(String publishId) {
        WechatToken wechatToken = cacheClient.getEntity(wechatProperties.getOauth().getAppId(), WechatToken.class);
        return WechatUtils.getPublish(wechatToken, publishId);
    }

    /**
     * 【服务器】【后台】增加发布
     *
     * @param articleId   发布成功时返回的articleId
     * @param index       要删除的文章在图文消息中的位置，第一篇编号为1，该字段不填或填0会删除全部文章
     * @return
     */
    public WechatResult delPublish(String articleId, Integer index) {
        WechatToken wechatToken = cacheClient.getEntity(wechatProperties.getOauth().getAppId(), WechatToken.class);
        return WechatUtils.delPublish(wechatToken, articleId, index);
    }

    /**
     * 【网站】【服务号】根据用户授权码获取用户信息
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
     * 【小程序】通过code换取openid
     *
     * @param jsCode 请求码
     * @return
     */
    public WechatMiniOpenId getMiniOpenId(String jsCode) {
        return WechatUtils.getMiniOpenId(wechatProperties.getOauth().getAppId(), wechatProperties.getOauth().getAppSecret(), jsCode);
    }

    /**
     * 【小程序】通过code换取手机信息
     *
     * @param code 请求码
     * @return
     */
    public WechatPhoneInfo getUserPhoneInfo(String code) {
        WechatToken wechatToken = getWechatToken();
        WechatPhone userPhone = WechatUtils.getUserPhone(wechatToken.getAccess_token(), code);
        return userPhone != null ? userPhone.getPhone_info() : null;
    }

    /***************支付**************/
    /**
     * 统一下单JSAPI
     *
     * @param wechatPayOrder
     * @return
     */
    public Map<String, String> unifiedOrderByJsApi(WechatPayOrder wechatPayOrder) {
        if(WechatPayProperties.V3.equals(wechatProperties.getPay().getVersion())){
            return WechatPayV3Utils.unifiedOrderByJsApi(wechatProperties, wechatPayOrder);
        }else {
            WechatPayConfig wechatPayConfig = new WechatPayConfig(wechatProperties.getOauth().getAppId(), wechatProperties.getPay().getMchId(), wechatProperties.getPay().getMchPrivateKey(), wechatProperties.getPay().getCertUrl());
            return WechatPayUtils.unifiedOrderByJsApi(wechatPayConfig, wechatPayOrder);
        }
    }

    /**
     * 统一下单Native
     *
     * @param wechatPayOrder
     * @return
     */
    public String unifiedOrderByNative(WechatPayOrder wechatPayOrder) {
        if(WechatPayProperties.V3.equals(wechatProperties.getPay().getVersion())){
            return WechatPayV3Utils.unifiedOrderByNative(wechatProperties, wechatPayOrder);
        }else {
            WechatPayConfig wechatPayConfig = new WechatPayConfig(wechatProperties.getOauth().getAppId(), wechatProperties.getPay().getMchId(), wechatProperties.getPay().getMchPrivateKey(), wechatProperties.getPay().getCertUrl());
            return WechatPayUtils.unifiedOrderByNative(wechatPayConfig, wechatPayOrder);
        }
    }

    /**
     * 查询订单
     * @param id 商户系统内部订单号
     * @return
     */
    public Map<String, String> getUnifiedOrderById(String id){
        if(WechatPayProperties.V3.equals(wechatProperties.getPay().getVersion())){
            return WechatPayV3Utils.getUnifiedOrderById(wechatProperties, id);
        }else {
            WechatPayConfig wechatPayConfig = new WechatPayConfig(wechatProperties.getOauth().getAppId(), wechatProperties.getPay().getMchId(), wechatProperties.getPay().getMchPrivateKey(), wechatProperties.getPay().getCertUrl());
            return WechatPayUtils.getUnifiedOrderById(wechatPayConfig, id);
        }
    }

    /**
     * 查询订单
     * @param outTradeNo 商户系统内部订单号
     * @return
     */
    public Map<String, String> getUnifiedOrderByOutTradeNo(String outTradeNo){
        if(WechatPayProperties.V3.equals(wechatProperties.getPay().getVersion())){
            return WechatPayV3Utils.getUnifiedOrderByOutTradeNo(wechatProperties, outTradeNo);
        }else {
            WechatPayConfig wechatPayConfig = new WechatPayConfig(wechatProperties.getOauth().getAppId(), wechatProperties.getPay().getMchId(), wechatProperties.getPay().getMchPrivateKey(), wechatProperties.getPay().getCertUrl());
            return WechatPayUtils.getUnifiedOrderByOutTradeNo(wechatPayConfig, outTradeNo);
        }
    }

    /**
     * 申请退款(全额)
     *
     * @param id       订单ID
     * @param totalFee 总费用（分）
     * @return
     */
    public Map<String, String> applyRefund(String id, String totalFee) {
        if(WechatPayProperties.V3.equals(wechatProperties.getPay().getVersion())){
            return WechatPayV3Utils.applyRefund(wechatProperties, id, totalFee, totalFee);
        }else {
            WechatPayConfig wechatPayConfig = new WechatPayConfig(wechatProperties.getOauth().getAppId(), wechatProperties.getPay().getMchId(), wechatProperties.getPay().getMchPrivateKey(), wechatProperties.getPay().getCertUrl());
            return WechatPayUtils.applyRefund(wechatPayConfig, id, totalFee, totalFee);
        }
    }

    /**
     * 申请退款(指定金额)
     *
     * @param id        订单ID
     * @param totalFee  总费用（分）
     * @param refuseFee 退款费用（分）
     * @return
     */
    public Map<String, String> applyRefund(String id, String totalFee, String refuseFee) {
        if(WechatPayProperties.V3.equals(wechatProperties.getPay().getVersion())){
            return WechatPayV3Utils.applyRefund(wechatProperties, id, totalFee, refuseFee);
        }else {
            WechatPayConfig wechatPayConfig = new WechatPayConfig(wechatProperties.getOauth().getAppId(), wechatProperties.getPay().getMchId(), wechatProperties.getPay().getMchPrivateKey(), wechatProperties.getPay().getCertUrl());
            return WechatPayUtils.applyRefund(wechatPayConfig, id, totalFee, refuseFee);
        }
    }

}
