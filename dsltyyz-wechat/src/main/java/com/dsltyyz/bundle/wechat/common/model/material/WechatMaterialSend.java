package com.dsltyyz.bundle.wechat.common.model.material;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.Serializable;

/**
 * Description:
 * 发送微信素材
 *
 * @author: dsltyyz
 * @date: 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatMaterialSend implements Serializable {

    /**
     * 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     */
    private String type;

    /**
     * 新增的图片素材的图片URL（仅新增图片素材时会返回该字段）
     */
    private File media;

    /**
     * 视频类型 素材的标题
     */
    private String title;

    /**
     * 视频类型 素材的描述
     */
    private String introduction;

}
