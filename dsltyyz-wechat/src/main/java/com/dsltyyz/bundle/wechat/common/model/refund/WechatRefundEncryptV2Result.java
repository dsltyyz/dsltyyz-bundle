package com.dsltyyz.bundle.wechat.common.model.refund;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:
 * 微信退款加密V2结果
 *
 * @author: dsltyyz
 * @date: 2019-11-07
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WechatRefundEncryptV2Result implements Serializable {

    /**
     *返回状态码 SUCCESS/FAIL
     */
    private String return_code;

    /**
     * 返回信息
     */
    private String return_msg;

    /**
     *公众账号ID
     */
    private String appid;

    /**
     *退款的商户号
     */
    private String mch_id;

    /**
     *随机字符串
     */
    private String nonce_str;

    /**
     * 加密信息
     */
    private String req_info;

}