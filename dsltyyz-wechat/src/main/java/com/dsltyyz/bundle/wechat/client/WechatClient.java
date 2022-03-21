package com.dsltyyz.bundle.wechat.client;

import com.dsltyyz.bundle.common.cache.client.CacheClient;
import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import com.dsltyyz.bundle.wechat.common.model.openid.WechatMiniOpenId;
import com.dsltyyz.bundle.wechat.common.model.openid.WechatOpenId;
import com.dsltyyz.bundle.wechat.common.model.pay.WechatPayConfig;
import com.dsltyyz.bundle.wechat.common.model.pay.WechatPayOrder;
import com.dsltyyz.bundle.wechat.common.model.phone.WechatPhone;
import com.dsltyyz.bundle.wechat.common.model.phone.WechatPhoneInfo;
import com.dsltyyz.bundle.wechat.common.model.template.WechatTemplate;
import com.dsltyyz.bundle.wechat.common.model.template.WechatTemplateSend;
import com.dsltyyz.bundle.wechat.common.model.token.WechatToken;
import com.dsltyyz.bundle.wechat.common.model.user.WechatUser;
import com.dsltyyz.bundle.wechat.common.property.WechatPayProperties;
import com.dsltyyz.bundle.wechat.common.property.WechatProperties;
import com.dsltyyz.bundle.wechat.common.util.WechatPayUtils;
import com.dsltyyz.bundle.wechat.common.util.WechatPayV3Utils;
import com.dsltyyz.bundle.wechat.common.util.WechatUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * 微信客户端
 *
 * @author: dsltyyz
 * @since: 2019-11-22
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
        if (null != wechatToken) {
            return wechatToken;
        }
        wechatToken = WechatUtils.getAccessToken(wechatProperties.getOauth().getAppId(), wechatProperties.getOauth().getAppSecret());
        cacheClient.putEntity(wechatProperties.getOauth().getAppId(), wechatToken, Long.valueOf(wechatToken.getExpires_in()));
        return wechatToken;
    }

    /**
     * 【服务器】获取该服务号下所有消息模板
     *
     * @return
     */
    public List<WechatTemplate> getAllPrivateTemplate() {
        WechatToken wechatToken = cacheClient.getEntity(wechatProperties.getOauth().getAppId(), WechatToken.class);
        return WechatUtils.getAllPrivateTemplate(wechatToken);
    }

    /**
     * 【服务器】发送消息模板
     *
     * @param wechatTemplateSend
     * @return
     */
    public WechatResult sendTemplate(WechatTemplateSend wechatTemplateSend) {
        WechatToken wechatToken = cacheClient.getEntity(wechatProperties.getOauth().getAppId(), WechatToken.class);
        return WechatUtils.sendTemplate(wechatToken, wechatTemplateSend);
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
     * 【网站】【服务号】根据用户openid获取用户信息
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
