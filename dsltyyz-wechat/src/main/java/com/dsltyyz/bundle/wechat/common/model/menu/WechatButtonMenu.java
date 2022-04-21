package com.dsltyyz.bundle.wechat.common.model.menu;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 微信按钮菜单
 *
 * @author: dsltyyz
 * @date: 2022-4-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatButtonMenu implements Serializable {

    /**
     * 菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型
     * 必填
     */
    private String type;

    /**
     * 菜单标题，不超过16个字节，子菜单不超过60个字节
     * 必填
     */
    private String name;

    /**
     * 菜单KEY值，用于消息接口推送，不超过128字节
     * click等点击类型必须
     */
    private String key;

    /**
     * 网页 链接，用户点击菜单可打开链接，不超过1024字节。 type为miniprogram时，不支持小程序的老版本客户端将打开本url。
     * view、miniprogram类型必须
     */
    private String url;

    /**
     * 调用新增永久素材接口返回的合法media_id
     * media_id类型和view_limited类型必须
     */
    private String media_id;

    /**
     * 小程序的appid（仅认证公众号可配置）
     * miniprogram类型必须
     */
    private String appid;

    /**
     * 小程序的页面路径
     * miniprogram类型必须
     */
    private String pagepath;

    /**
     * 发布后获得的合法 article_id
     * article_id类型和article_view_limited类型必须
     */
    private String article_id;

    /**
     * 二级及以上菜单
     * 最多包含5个二级菜单
     */
    private List<WechatButtonMenu> sub_button;

}
