package com.dsltyyz.bundle.wechat.common.model.phone;

import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 * 微信手机信息
 *
 * @author: dsltyyz
 * @since: 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatPhone extends WechatResult implements Serializable {

    /**
     * 用户手机号信息
     */
    private WechatPhoneInfo phone_info;


}
