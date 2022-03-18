package com.dsltyyz.bundle.wechat.common.model.phone;

import com.dsltyyz.bundle.wechat.common.model.watermark.WechatWatermark;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 * 微信手机详细信息
 *
 * @author: dsltyyz
 * @since: 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatPhoneInfo implements Serializable {

    /**
     * 用户绑定的手机号（国外手机号会有区号）
     */
    private String phoneNumber;

    /**
     * 没有区号的手机号
     */
    private String purePhoneNumber;

    /**
     * 	区号
     */
    private String countryCode;
    /**
     * 数据水印
     */
    private WechatWatermark watermark;


}
