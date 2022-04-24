package com.dsltyyz.bundle.wechat.common.model.material;

import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 * 微信素材
 *
 * @author: dsltyyz
 * @date: 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatMaterial extends WechatResult implements Serializable {

    /**
     * 新增的永久素材的media_id
     */
    private String media_id;

    /**
     * 新增的图片素材的图片URL（仅新增图片素材时会返回该字段）
     */
    private String url;

}
