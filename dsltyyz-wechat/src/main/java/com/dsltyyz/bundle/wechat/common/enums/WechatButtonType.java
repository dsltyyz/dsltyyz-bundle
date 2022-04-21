package com.dsltyyz.bundle.wechat.common.enums;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信按钮类型
 * @author dsltyyz
 * @date 2022-4-21
 */
@AllArgsConstructor
@Getter
public enum WechatButtonType {

    //文件夹
    DIR("","点击"),

    //点击推事件用户点击click类型按钮后，微信服务器会通过消息接口推送消息类型为event的结构给开发者（参考消息接口指南），并且带上按钮中开发者填写的key值，开发者可以通过自定义的key值与用户进行交互；
    CLICK("click","点击"),

    //跳转URL用户点击view类型按钮后，微信客户端将会打开开发者在按钮中填写的网页URL，可与网页授权获取用户基本信息接口结合，获得用户基本信息。
    VIEW("view","跳转URL"),

    //扫码推事件用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后显示扫描结果（如果是URL，将进入URL），且会将扫码的结果传给开发者，开发者可以下发消息。
    SCANCODE_PUSH("scancode_push","扫码"),

    //扫码推事件且弹出“消息接收中”提示框用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后，将扫码的结果传给开发者，同时收起扫一扫工具，然后弹出“消息接收中”提示框，随后可能会收到开发者下发的消息。
    SCANCODE_WAITMSG("scancode_waitmsg","扫码消息"),

    //弹出系统拍照发图用户点击按钮后，微信客户端将调起系统相机，完成拍照操作后，会将拍摄的相片发送给开发者，并推送事件给开发者，同时收起系统相机，随后可能会收到开发者下发的消息。
    PIC_SYSPHOTO("pic_sysphoto","系统拍照"),

    //弹出拍照或者相册发图用户点击按钮后，微信客户端将弹出选择器供用户选择“拍照”或者“从手机相册选择”。用户选择后即走其他两种流程。
    PIC_PHOTO_OR_ALBUM("click","拍照或者相册发图"),

    //弹出微信相册发图器用户点击按钮后，微信客户端将调起微信相册，完成选择操作后，将选择的相片发送给开发者的服务器，并推送事件给开发者，同时收起相册，随后可能会收到开发者下发的消息。
    PIC_WEIXIN("pic_weixin","微信相册发图"),

    //弹出地理位置选择器用户点击按钮后，微信客户端将调起地理位置选择工具，完成选择操作后，将选择的地理位置发送给开发者的服务器，同时收起位置选择工具，随后可能会收到开发者下发的消息。
    LOCATION_SELECT("location_select","地理位置"),

    //下发消息（除文本消息）用户点击media_id类型按钮后，微信服务器会将开发者填写的永久素材id对应的素材下发给用户，永久素材类型可以是图片、音频、视频 、图文消息。请注意：永久素材id必须是在“素材管理/新增永久素材”接口上传后获得的合法id。
    MEDIA_ID("click","下发消息"),

    //用户点击 article_id 类型按钮后，微信客户端将会以卡片形式，下发开发者在按钮中填写的图文消息
    ARTICLE_ID("article_id","图文消息"),

    //跳转图文消息URL用户点击view_limited类型按钮后，微信客户端将打开开发者在按钮中填写的永久素材id对应的图文消息URL，永久素材类型只支持图文消息。请注意：永久素材id必须是在“素材管理/新增永久素材”接口上传后获得的合法id。
    //类似 view_limited，但不使用 media_id 而使用 article_id
    ARTICLE_VIEW_LIMITED("article_view_limited","图文消息URL");

    /**
     * 值
     */
    String value;

    /**
     * 名称
     */
    String name;

    /**
     * 获取枚举JSON数组
     */
    public static JSONArray toJson() {
        JSONArray jsonArray = new JSONArray();
        for (WechatButtonType e : WechatButtonType.values()) {
            JSONObject object = new JSONObject();
            object.put("value", e.getValue());
            object.put("name", e.getName());
            jsonArray.add(object);
        }
        return jsonArray;
    }
}
