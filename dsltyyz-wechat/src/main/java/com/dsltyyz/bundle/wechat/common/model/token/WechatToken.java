package com.dsltyyz.bundle.wechat.common.model.token;

import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:
 * 微信token
 *
 * @author: dsltyyz
 * @date: 2019-11-06
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatToken extends WechatResult implements Serializable {

    /**
     * 获取到的凭证
     */
    private String access_token;

    /**
     * 凭证有效时间，单位：秒
     */
    private Integer expires_in;

}
