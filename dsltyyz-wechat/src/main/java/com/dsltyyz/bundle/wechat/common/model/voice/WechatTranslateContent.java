package com.dsltyyz.bundle.wechat.common.model.voice;

import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 * 微信文本翻译
 *
 * @author: dsltyyz
 * @date: 2023-4-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatTranslateContent extends WechatResult implements Serializable {

    /**
     * 原始内容
     */
    private String fromContent;

    /**
     * 原始内容
     */
    private String toContent;


}
