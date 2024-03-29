package com.dsltyyz.bundle.wechat.common.model.user;

import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 微信用户订阅
 * 20年6月8日起，用户关注来源“微信广告（ADD_SCENE_WECHAT_ADVERTISEMENT）”从“其他（ADD_SCENE_OTHERS）”中拆分给出，2021年12月27日之后，不再输出头像、昵称信息。
 * @author: dsltyyz
 * @date: 2019-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatUserSubscribe extends WechatResult implements Serializable {

    /**
     * 用户是否订阅该服务号标识，值为0时，代表此用户没有关注该服务号，拉取不到其余信息。
     */
    private Integer subscribe;

    /**
     * 用户的标识，对当前服务号唯一
     */
    private String openid;

    /**
     *用户的语言，简体中文为zh_CN
     */
    private String language;

    /**
     * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
     */
    private Long subscribe_time;

    /**
     * 只有在用户将服务号绑定到微信开放平台帐号后，才会出现该字段。
     */
    private String unionid;

    /**
     * 服务号运营者对粉丝的备注，服务号运营者可在微信公众平台用户管理界面对粉丝添加备注
     */
    private String remark;

    /**
     * 用户所在的分组ID（兼容旧的用户分组接口）
     */
    private Integer groupid;

    /**
     * 用户被打上的标签ID列表
     */
    private List<Integer> tagid_list;

    /**
     * 返回用户关注的渠道来源，ADD_SCENE_SEARCH 服务号搜索，ADD_SCENE_ACCOUNT_MIGRATION 服务号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_ LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_OTHERS 其他
     */
    private String subscribe_scene;

    /**
     * 二维码扫码场景（开发者自定义）
     */
    private Long qr_scene;

    /**
     * 二维码扫码场景描述（开发者自定义）
     */
    private String qr_scene_str;
}
