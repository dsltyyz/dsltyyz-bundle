package com.dsltyyz.bundle.wechat.common.model.template;

import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 微信小程序订阅消息结果
 *
 * @author: dsltyyz
 * @date: 2019-11-07
 */
@Data
public class WechatMiniTemplateResult extends WechatResult implements Serializable {

    /**
     * 模板列表
     */
    private List<WechatMiniTemplate> data;

}
