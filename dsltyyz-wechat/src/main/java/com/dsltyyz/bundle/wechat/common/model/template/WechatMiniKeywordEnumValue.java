package com.dsltyyz.bundle.wechat.common.model.template;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 微信小程序订阅消息枚举参数
 *
 * @author: dsltyyz
 * @date: 2019-11-07
 */
@Data
public class WechatMiniKeywordEnumValue implements Serializable {

    /**
     * 枚举参数值范围列表
     */
    private List<String> enumValueList;

    /**
     *枚举参数的 key
     */
    private String keywordCode;

}
