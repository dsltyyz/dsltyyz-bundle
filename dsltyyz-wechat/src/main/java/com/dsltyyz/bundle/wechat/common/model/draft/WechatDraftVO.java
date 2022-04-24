package com.dsltyyz.bundle.wechat.common.model.draft;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 * 发送微信草稿
 *
 * @author: dsltyyz
 * @date: 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatDraftVO implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空。如果本字段为没有填写，则默认抓取正文前54个字。
     */
    private String digest;

    /**
     * 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS,涉及图片url必须来源 "上传图文消息内的图片获取URL"接口获取。外部图片url将被过滤。
     */
    private String content;

    /**
     * 图文消息的原文地址，即点击“阅读原文”后的URL
     */
    private String content_source_url;

    /**
     * 图文消息的封面图片素材id（必须是永久MediaID）
     */
    private String thumb_media_id;

    /**
     * Uint32 是否打开评论，0不打开(默认)，1打开
     */
    private Integer need_open_comment;

    /**
     * Uint32 是否粉丝才可评论，0所有人可评论(默认)，1粉丝才可评论
     * 接口返回说明
     */
    private Integer only_fans_can_comment;

    /**
     * 草稿的临时链接
     */
    private String url;
}
