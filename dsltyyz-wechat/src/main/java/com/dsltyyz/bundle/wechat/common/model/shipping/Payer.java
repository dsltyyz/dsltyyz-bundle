package com.dsltyyz.bundle.wechat.common.model.shipping;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 * 支付者，支付者信息
 *
 * @author: dsltyyz
 * @date: 2026-3-5
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Payer implements Serializable {

    /**
     * 用户标识，用户在小程序appid下的唯一标识。 下单前需获取到用户的Openid
     * 示例值: oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
     * 字符字节限制: [1, 128]
     */
    private String openid;

}
