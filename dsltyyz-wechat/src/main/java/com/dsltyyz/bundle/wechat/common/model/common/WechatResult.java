package com.dsltyyz.bundle.wechat.common.model.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 微信返回结果
 */
@Data
public class WechatResult implements Serializable {

    /**
     * 错误码
     */
    private Long errcode;

    /**
     * 错误消息
     */
    private String errmsg;

}