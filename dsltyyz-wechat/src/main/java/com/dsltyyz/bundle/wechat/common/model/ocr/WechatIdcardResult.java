package com.dsltyyz.bundle.wechat.common.model.ocr;

import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * 微信身份证
 *
 * @author: dsltyyz
 * @date: 2019/05/15
 */
@Data
@ApiModel(value = "微信身份证")
public class WechatIdcardResult extends WechatResult implements Serializable {

    @ApiModelProperty(value = "正面或背面，Front / Back")
    private String type;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "身份证号")
    private String id;

    @ApiModelProperty(value = "地址")
    private String addr;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "民族")
    private String nationality;

    @ApiModelProperty(value = "出生")
    private String birth;

    @ApiModelProperty(value = "有效期")
    private String valid_date;
}
