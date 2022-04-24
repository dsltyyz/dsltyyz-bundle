package com.dsltyyz.bundle.wechat.common.model.template;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 微信模板消息结果
 *
 * @author: dsltyyz
 * @date: 2019-11-07
 */
@Data
public class WechatTemplateResult implements Serializable {

    /**
     * 模板列表
     */
    private List<WechatTemplate> template_list;

}
