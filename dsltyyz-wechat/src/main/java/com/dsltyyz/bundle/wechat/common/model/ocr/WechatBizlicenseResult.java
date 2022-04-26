package com.dsltyyz.bundle.wechat.common.model.ocr;

import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * 微信营业执照
 *
 * @author: dsltyyz
 * @date: 2019/05/15
 */
@Data
@ApiModel(value = "微信身份证")
public class WechatBizlicenseResult extends WechatResult implements Serializable {

    @ApiModelProperty(value = "注册号")
    private String reg_num;

    @ApiModelProperty(value = "编号")
    private String serial;

    @ApiModelProperty(value = "法定代表人姓名")
    private String legal_representative;

    @ApiModelProperty(value = "企业名称")
    private String enterprise_name;

    @ApiModelProperty(value = "组成形式")
    private String type_of_organization;

    @ApiModelProperty(value = "经营场所/企业住所")
    private String address;

    @ApiModelProperty(value = "公司类型")
    private String type_of_enterprise;

    @ApiModelProperty(value = "经营范围")
    private String business_scope;

    @ApiModelProperty(value = "注册资本")
    private String registered_capital;

    @ApiModelProperty(value = "实收资本")
    private String paid_in_capital;

    @ApiModelProperty(value = "营业期限")
    private String valid_period;

    @ApiModelProperty(value = "注册日期/成立日期")
    private String registered_date;

    @ApiModelProperty(value = "营业执照位置")
    private String cert_position;

    @ApiModelProperty(value = "图片大小")
    private String img_size;
}
