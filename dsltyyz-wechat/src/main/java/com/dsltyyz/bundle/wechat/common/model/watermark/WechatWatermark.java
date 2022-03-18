package com.dsltyyz.bundle.wechat.common.model.watermark;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 * 微信水印
 *
 * @author: dsltyyz
 * @since: 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatWatermark implements Serializable {

    /**
     * 用户获取手机号操作的时间戳
     */
    private Long timestamp;

    /**
     * 小程序appid
     */
    private String appid;

}
