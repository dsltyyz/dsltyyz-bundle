package com.dsltyyz.bundle.wechat.common.model.voice;

import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 * 微信音频转文字
 *
 * @author: dsltyyz
 * @date: 2023-4-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatVoiceToText extends WechatResult implements Serializable {

    /**
     * 返回结果
     */
    private String result;

    /**
     * 是否结束
     */
    private Boolean isEnd;


}
