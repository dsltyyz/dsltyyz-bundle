package com.dsltyyz.bundle.wechat.common.model.ocr;

import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * 微信银行卡
 *
 * @author: dsltyyz
 * @date: 2019/05/15
 */
@Data
@ApiModel(value = "微信银行卡")
public class WechatBankcardResult extends WechatResult implements Serializable {

    @ApiModelProperty(value = "银行卡?")
    private String id;

    @ApiModelProperty(value = "银行卡号")
    private String number;

}
