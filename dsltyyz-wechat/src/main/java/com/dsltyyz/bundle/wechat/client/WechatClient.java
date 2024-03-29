package com.dsltyyz.bundle.wechat.client;

import com.alibaba.fastjson.JSONObject;
import com.dsltyyz.bundle.common.cache.client.CacheClient;
import com.dsltyyz.bundle.common.util.FileUtils;
import com.dsltyyz.bundle.common.util.HttpUtils;
import com.dsltyyz.bundle.common.util.PatternUtils;
import com.dsltyyz.bundle.common.util.UUIDUtils;
import com.dsltyyz.bundle.wechat.common.constant.WechatMaterialType;
import com.dsltyyz.bundle.wechat.common.model.article.WechatArticle;
import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import com.dsltyyz.bundle.wechat.common.model.draft.DsltyyzDraftSend;
import com.dsltyyz.bundle.wechat.common.model.draft.WechatDraftDetailVO;
import com.dsltyyz.bundle.wechat.common.model.draft.WechatDraftSend;
import com.dsltyyz.bundle.wechat.common.model.material.WechatMaterial;
import com.dsltyyz.bundle.wechat.common.model.material.WechatMaterialSend;
import com.dsltyyz.bundle.wechat.common.model.menu.WechatMenu;
import com.dsltyyz.bundle.wechat.common.model.ocr.WechatBankcardResult;
import com.dsltyyz.bundle.wechat.common.model.ocr.WechatBizlicenseResult;
import com.dsltyyz.bundle.wechat.common.model.ocr.WechatDrivingResult;
import com.dsltyyz.bundle.wechat.common.model.ocr.WechatIdcardResult;
import com.dsltyyz.bundle.wechat.common.model.openid.WechatMiniOpenId;
import com.dsltyyz.bundle.wechat.common.model.openid.WechatOpenId;
import com.dsltyyz.bundle.wechat.common.model.pay.WechatPayConfig;
import com.dsltyyz.bundle.wechat.common.model.pay.WechatPayOrder;
import com.dsltyyz.bundle.wechat.common.model.phone.WechatPhone;
import com.dsltyyz.bundle.wechat.common.model.phone.WechatPhoneInfo;
import com.dsltyyz.bundle.wechat.common.model.publish.WechatMass;
import com.dsltyyz.bundle.wechat.common.model.publish.WechatPublish;
import com.dsltyyz.bundle.wechat.common.model.template.WechatMiniTemplate;
import com.dsltyyz.bundle.wechat.common.model.template.WechatMiniTemplateSend;
import com.dsltyyz.bundle.wechat.common.model.template.WechatTemplate;
import com.dsltyyz.bundle.wechat.common.model.template.WechatTemplateSend;
import com.dsltyyz.bundle.wechat.common.model.token.WechatToken;
import com.dsltyyz.bundle.wechat.common.model.user.WechatUser;
import com.dsltyyz.bundle.wechat.common.model.user.WechatUserSubscribe;
import com.dsltyyz.bundle.wechat.common.model.voice.WechatTranslateContent;
import com.dsltyyz.bundle.wechat.common.model.voice.WechatVoiceToText;
import com.dsltyyz.bundle.wechat.common.property.WechatPayProperties;
import com.dsltyyz.bundle.wechat.common.property.WechatProperties;
import com.dsltyyz.bundle.wechat.common.util.WechatPayUtils;
import com.dsltyyz.bundle.wechat.common.util.WechatPayV3Utils;
import com.dsltyyz.bundle.wechat.common.util.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * 微信客户端
 *
 * @author: dsltyyz
 * @date: 2019-11-22
 */
@Slf4j
public class WechatClient {

    @Resource
    private WechatProperties wechatProperties;

    @Resource
    private CacheClient cacheClient;

    public WechatClient() {
        log.info("微信客户端已加载");
    }


    /**
     * 【服务号-后台】获取access_token
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
     * 【服务号-后台】根据用户openid获取用户信息
     *
     * @param openId
     * @return
     */
    public WechatUserSubscribe getUserSubscribe(String openId) {
        WechatToken wechatToken = getWechatToken();
        //openid及token获取用户信息
        return WechatUtils.getUserSubscribe(wechatToken.getAccess_token(), openId);
    }

    /**
     * 【后台】创建菜单
     *
     * @param wechatMenu 微信菜单
     * @return
     */
    public WechatResult addMenu(WechatMenu wechatMenu) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.addMenu(wechatToken.getAccess_token(), wechatMenu);
    }

    /**
     * 【后台】删除菜单
     *
     * @return
     */
    public WechatResult delMenu() {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.delMenu(wechatToken.getAccess_token());
    }

    /**
     * 【服务号-后台】获取该服务号下所有消息模板
     *
     * @return
     */
    public List<WechatTemplate> getAllPrivateTemplate() {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.getAllPrivateTemplate(wechatToken);
    }

    /**
     * 【服务号-后台】发送消息模板
     *
     * @param wechatTemplateSend
     * @return
     */
    public WechatResult sendTemplate(WechatTemplateSend wechatTemplateSend) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.sendTemplate(wechatToken, wechatTemplateSend);
    }

    /**
     * 【服务号-后台】新增永久素材
     *
     * @param wechatMaterialSend 素材
     * @return
     */
    public WechatMaterial addMaterial(WechatMaterialSend wechatMaterialSend) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.addMaterial(wechatToken, wechatMaterialSend);
    }

    /**
     * 【服务号-后台】删除永久素材
     *
     * @param mediaId 素材的media_id
     * @return
     */
    public WechatResult delMaterial(String mediaId) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.delMaterial(wechatToken, mediaId);
    }

    /**
     * 【服务号-后台】将资源转换为微信永久素材
     *
     * @param sourceUrl
     * @return
     */
    public WechatMaterial convertMaterial(String sourceUrl) {
        //网络资源下载
        InputStream inputStream = HttpUtils.doGetInputStream(sourceUrl, null, null);
        //创建本地文件
        File file = new File(sourceUrl.substring(sourceUrl.lastIndexOf("/") + 1));
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
     * 【服务号-后台】增加草稿
     *
     * @param wechatArticle 文章
     * @return
     */
    public WechatMaterial addDraft(WechatArticle wechatArticle) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.addDraft(wechatToken, wechatArticle);
    }

    /**
     * 【服务号-后台】删除草稿
     *
     * @param mediaId 草稿mediaId
     * @return
     */
    public WechatResult delDraft(String mediaId) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.delDraft(wechatToken, mediaId);
    }

    /**
     * 【后台】获取草稿
     *
     * @param mediaId 草稿mediaId
     * @return
     */
    public WechatDraftDetailVO getDraft(String mediaId) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.getDraft(wechatToken, mediaId);
    }

    /**
     * 【服务号-后台】增加草稿
     *
     * @param dsltyyzDraftSend 自定义文章
     * @return
     */
    public WechatMaterial addDsltyyzDraft(DsltyyzDraftSend dsltyyzDraftSend) {
        return addDsltyyzDraft(Arrays.asList(dsltyyzDraftSend));
    }

    /**
     * 【服务号-后台】增加草稿
     *
     * @param list 自定义文章列表
     * @return
     */
    public WechatMaterial addDsltyyzDraft(List<DsltyyzDraftSend> list) {
        Assert.isTrue(list != null && list.size() > 0, "文章列表不能为空");
        WechatArticle wechatArticle = new WechatArticle();
        wechatArticle.setArticles(list.stream().map(dsltyyzDraftSend -> {
            WechatDraftSend wechatDraftSend = new WechatDraftSend();
            BeanUtils.copyProperties(dsltyyzDraftSend, wechatDraftSend);
            //将资源转换为微信永久素材
            WechatMaterial wechatMaterial = convertMaterial(dsltyyzDraftSend.getLogo());
            wechatDraftSend.setThumb_media_id(wechatMaterial.getMedia_id());
            //将内容资源全部找出
            List<String> srcResource = PatternUtils.getSrcResource(wechatDraftSend.getContent());
            //将内容资源全部替换为微信永久素材
            srcResource.stream().forEach(s -> {
                WechatMaterial wechatMaterial1 = convertMaterial(s);
                wechatDraftSend.setContent(wechatDraftSend.getContent().replace(s, wechatMaterial1.getUrl()));
            });
            return wechatDraftSend;
        }).collect(Collectors.toList()));
        return addDraft(wechatArticle);
    }

    /**
     * 【服务号-后台】增加发布
     *
     * @param mediaId 草稿mediaId
     * @return
     */
    public WechatPublish addPublish(String mediaId) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.addPublish(wechatToken, mediaId);
    }

    /**
     * 【服务号-后台】获取发布
     *
     * @param publishId 发布publishId
     * @return
     */
    public WechatPublish getPublish(String publishId) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.getPublish(wechatToken, publishId);
    }

    /**
     * 【服务号-后台】增加发布
     *
     * @param articleId 发布成功时返回的articleId
     * @param index     要删除的文章在图文消息中的位置，第一篇编号为1，该字段不填或填0会删除全部文章
     * @return
     */
    public WechatResult delPublish(String articleId, Integer index) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.delPublish(wechatToken, articleId, index);
    }

    /**
     * 【服务号-后台】增加群发草稿
     *
     * @param mediaId
     * @return
     */
    public WechatMass addBatchPublish(String mediaId) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.addBatchPublish(wechatToken, mediaId);
    }

    /**
     * 【服务号-后台】增加群发
     *
     * @param param 参数
     * @return
     */
    public WechatMass addMessageMass(Map<String, Object> param) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.addMessageMass(wechatToken, param);
    }

    /**
     * 【服务号-后台】删除群发
     *
     * @param msgId      发送出去的消息ID
     * @param articleIdx 要删除的文章在图文消息中的位置，第一篇编号为1，该字段不填或填0会删除全部文章
     * @return
     */
    public WechatResult delMessageMass(Integer msgId, Integer articleIdx) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.delMessageMass(wechatToken, msgId, articleIdx);
    }

    /**
     * 【服务号-后台】获取群发状态
     *
     * @param msgId 发送出去的消息ID
     * @return
     */
    public WechatMass getMessageMass(Integer msgId) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.getMessageMass(wechatToken, msgId);
    }

    /**
     * 【网站】【服务号-前台】根据用户授权码获取用户信息
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

    /**
     * 【小程序】订阅消息获取当前帐号下的个人模板列表
     *
     * @return
     */
    public List<WechatMiniTemplate> getNewTmplList() {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.getNewTmplList(wechatToken.getAccess_token());
    }

    /**
     * 【小程序】发送订阅消息
     *
     * @param wechatMiniTemplateSend
     * @return
     */
    public WechatResult sendNewTmpl(WechatMiniTemplateSend wechatMiniTemplateSend) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.sendNewTmpl(wechatToken.getAccess_token(), wechatMiniTemplateSend);
    }

    /*******************OCR***********/
    /**
     * 获取银行卡号
     *
     * @param type
     * @param imgUrl
     * @param img
     * @return
     */
    public WechatBankcardResult getBankcard(String type, String imgUrl, File img) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.getBankcard(wechatToken.getAccess_token(), type, imgUrl, img);
    }

    /**
     * 获取身份证号
     *
     * @param type
     * @param imgUrl
     * @param img
     * @return
     */
    public WechatIdcardResult getIdcard(String type, String imgUrl, File img) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.getIdcard(wechatToken.getAccess_token(), type, imgUrl, img);
    }

    /**
     * 获取营业执照
     *
     * @param imgUrl
     * @param img
     * @return
     */
    public WechatBizlicenseResult getBizLicense(String imgUrl, File img) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.getBizLicense(wechatToken.getAccess_token(), imgUrl, img);
    }

    /**
     * 获取行驶证
     *
     * @param imgUrl
     * @param img
     * @return
     */
    public WechatDrivingResult getDriving(String imgUrl, File img) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.getDriving(wechatToken.getAccess_token(), imgUrl, img);
    }

    /*******************AI***********/
    /**
     * 【公众号】【小程序】【APP】语音转文本
     *
     * @param content
     * @return
     */
    public WechatVoiceToText getVoiceToText(byte[] content) {
        WechatToken wechatToken = getWechatToken();
        String uuid = UUIDUtils.getUUID();
        WechatResult wechatResult = WechatUtils.addVoiceToText(wechatToken.getAccess_token(), uuid, content);
        Assert.isTrue(wechatResult.getErrcode() == 0, wechatResult.getErrmsg());
        WechatVoiceToText voiceToText = WechatUtils.getVoiceToText(wechatToken.getAccess_token(), uuid);
        while (!voiceToText.getIsEnd()) {
            try {
                Thread.sleep(500);
                voiceToText = WechatUtils.getVoiceToText(wechatToken.getAccess_token(), uuid);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
        return voiceToText;
    }

    /**
     * 【公众号】【小程序】【APP】微信翻译(中文转英文)
     *
     * @param content
     * @return
     */
    public WechatTranslateContent translateContentCnToEn(String content) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.translateContent(wechatToken.getAccess_token(), "zh_CN", "en_US", content);
    }

    /**
     * 【公众号】【小程序】【APP】微信翻译(中文转英文)
     *
     * @param content
     * @return
     */
    public WechatTranslateContent translateContentEnToCn(String content) {
        WechatToken wechatToken = getWechatToken();
        return WechatUtils.translateContent(wechatToken.getAccess_token(), "en_US", "zh_CN", content);
    }

    /***************支付**************/
    /**
     * 【微信支付】统一下单JSAPI
     *
     * @param wechatPayOrder
     * @return
     */
    public Map<String, String> unifiedOrderByJsApi(WechatPayOrder wechatPayOrder) {
        if (WechatPayProperties.V2.equals(wechatProperties.getPay().getVersion())) {
            WechatPayConfig wechatPayConfig = new WechatPayConfig(wechatProperties.getOauth().getAppId(), wechatProperties.getPay().getMchId(), wechatProperties.getPay().getMchPrivateKey(), wechatProperties.getPay().getCertUrl());
            return WechatPayUtils.unifiedOrderByJsApi(wechatPayConfig, wechatPayOrder);
        } else {
            return WechatPayV3Utils.unifiedOrderByJsApi(wechatProperties, wechatPayOrder);
        }
    }

    /**
     * 【未测试】【微信支付】统一下单APP
     *
     * @param wechatPayOrder
     * @return
     */
    public Map<String, String> unifiedOrderByApp(WechatPayOrder wechatPayOrder) {
        if (WechatPayProperties.V2.equals(wechatProperties.getPay().getVersion())) {
            WechatPayConfig wechatPayConfig = new WechatPayConfig(wechatProperties.getOauth().getAppId(), wechatProperties.getPay().getMchId(), wechatProperties.getPay().getMchPrivateKey(), wechatProperties.getPay().getCertUrl());
            return WechatPayUtils.unifiedOrderByApp(wechatPayConfig, wechatPayOrder);
        } else {
            return WechatPayV3Utils.unifiedOrderByApp(wechatProperties, wechatPayOrder);
        }
    }

    /**
     * 【微信支付】统一下单Native
     *
     * @param wechatPayOrder
     * @return
     */
    public String unifiedOrderByNative(WechatPayOrder wechatPayOrder) {
        if (WechatPayProperties.V2.equals(wechatProperties.getPay().getVersion())) {
            WechatPayConfig wechatPayConfig = new WechatPayConfig(wechatProperties.getOauth().getAppId(), wechatProperties.getPay().getMchId(), wechatProperties.getPay().getMchPrivateKey(), wechatProperties.getPay().getCertUrl());
            return WechatPayUtils.unifiedOrderByNative(wechatPayConfig, wechatPayOrder);
        } else {
            return WechatPayV3Utils.unifiedOrderByNative(wechatProperties, wechatPayOrder);
        }
    }

    /**
     * 【微信支付】统一下单H5
     *
     * @param wechatPayOrder
     * @return
     */
    public String unifiedOrderByH5(WechatPayOrder wechatPayOrder) {
        if (WechatPayProperties.V2.equals(wechatProperties.getPay().getVersion())) {
            WechatPayConfig wechatPayConfig = new WechatPayConfig(wechatProperties.getOauth().getAppId(), wechatProperties.getPay().getMchId(), wechatProperties.getPay().getMchPrivateKey(), wechatProperties.getPay().getCertUrl());
            return WechatPayUtils.unifiedOrderByH5(wechatPayConfig, wechatPayOrder);
        } else {
            return WechatPayV3Utils.unifiedOrderByH5(wechatProperties, wechatPayOrder);
        }
    }

    /**
     * 【微信支付】查询订单
     *
     * @param id 微信订单号
     * @return
     */
    public JSONObject getUnifiedOrderById(String id) {
        if (WechatPayProperties.V2.equals(wechatProperties.getPay().getVersion())) {
            WechatPayConfig wechatPayConfig = new WechatPayConfig(wechatProperties.getOauth().getAppId(), wechatProperties.getPay().getMchId(), wechatProperties.getPay().getMchPrivateKey(), wechatProperties.getPay().getCertUrl());
            return WechatPayUtils.getUnifiedOrderById(wechatPayConfig, id);
        } else {
            return WechatPayV3Utils.getUnifiedOrderById(wechatProperties, id);
        }
    }

    /**
     * 【微信支付】查询订单
     *
     * @param outTradeNo 商户系统内部订单号
     * @return
     */
    public JSONObject getUnifiedOrderByOutTradeNo(String outTradeNo) {
        if (WechatPayProperties.V2.equals(wechatProperties.getPay().getVersion())) {
            WechatPayConfig wechatPayConfig = new WechatPayConfig(wechatProperties.getOauth().getAppId(), wechatProperties.getPay().getMchId(), wechatProperties.getPay().getMchPrivateKey(), wechatProperties.getPay().getCertUrl());
            return WechatPayUtils.getUnifiedOrderByOutTradeNo(wechatPayConfig, outTradeNo);
        } else {
            return WechatPayV3Utils.getUnifiedOrderByOutTradeNo(wechatProperties, outTradeNo);
        }
    }

    /**
     * 【微信支付】申请退款(全额)
     *
     * @param id        微信订单ID
     * @param totalFee  总费用（分）
     * @param notifyUrl 通知URL
     * @return
     */
    public JSONObject applyRefundById(String id, String totalFee, String notifyUrl) {
        return applyRefundById(id, totalFee, totalFee, notifyUrl);
    }

    /**
     * 【微信支付】申请退款(指定金额)
     *
     * @param id        微信订单ID
     * @param totalFee  总费用（分）
     * @param refuseFee 退款费用（分）
     * @param notifyUrl 通知URL
     * @return
     */
    public JSONObject applyRefundById(String id, String totalFee, String refuseFee, String notifyUrl) {
        if (WechatPayProperties.V2.equals(wechatProperties.getPay().getVersion())) {
            WechatPayConfig wechatPayConfig = new WechatPayConfig(wechatProperties.getOauth().getAppId(), wechatProperties.getPay().getMchId(), wechatProperties.getPay().getMchPrivateKey(), wechatProperties.getPay().getCertUrl());
            return WechatPayUtils.applyRefundById(wechatPayConfig, id, totalFee, refuseFee, notifyUrl);
        } else {
            return WechatPayV3Utils.applyRefundById(wechatProperties, id, totalFee, refuseFee, notifyUrl);
        }
    }

    /**
     * 【微信支付】申请退款(全额)
     *
     * @param outTradeNo 商户订单ID
     * @param totalFee   总费用（分）
     * @param notifyUrl  通知URL
     * @return
     */
    public JSONObject applyRefundByOutTradeNo(String outTradeNo, String totalFee, String notifyUrl) {
        return applyRefundByOutTradeNo(outTradeNo, totalFee, totalFee, notifyUrl);
    }

    /**
     * 【微信支付】申请退款(指定金额)
     *
     * @param outTradeNo 商户订单ID
     * @param totalFee   总费用（分）
     * @param refuseFee  退款费用（分）
     * @param notifyUrl  通知URL
     * @return
     */
    public JSONObject applyRefundByOutTradeNo(String outTradeNo, String totalFee, String refuseFee, String notifyUrl) {
        if (WechatPayProperties.V2.equals(wechatProperties.getPay().getVersion())) {
            WechatPayConfig wechatPayConfig = new WechatPayConfig(wechatProperties.getOauth().getAppId(), wechatProperties.getPay().getMchId(), wechatProperties.getPay().getMchPrivateKey(), wechatProperties.getPay().getCertUrl());
            JSONObject jsonObject = WechatPayUtils.applyRefundByOutTradeNo(wechatPayConfig, outTradeNo, totalFee, refuseFee, notifyUrl);
            Assert.isTrue(jsonObject.getString("result_code").equals("SUCCESS"), "微信退款失败");
            return jsonObject;
        } else {
            JSONObject jsonObject = WechatPayV3Utils.applyRefundByOutTradeNo(wechatProperties, outTradeNo, totalFee, refuseFee, notifyUrl);
            Assert.isTrue(jsonObject.getString("status").equals("SUCCESS") || jsonObject.getString("status").equals("PROCESSING"), "微信退款失败");
            return jsonObject;
        }
    }

    /********************V3微信回调解密**********************/
    /**
     * 微信回调解密V3
     *
     * @param associatedData
     * @param nonce
     * @param ciphertext
     * @return
     */
    public String decryptToStringV3(String associatedData, String nonce, String ciphertext) {
        return WechatPayV3Utils.decryptToStringV3(wechatProperties.getPay().getApiV3Key(), associatedData, nonce, ciphertext);
    }
}
