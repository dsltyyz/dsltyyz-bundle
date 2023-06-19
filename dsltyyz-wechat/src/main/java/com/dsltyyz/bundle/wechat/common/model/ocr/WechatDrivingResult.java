package com.dsltyyz.bundle.wechat.common.model.ocr;

import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * 微信行驶证
 *
 * @author: dsltyyz
 * @date: 2019/05/15
 */
@Data
@ApiModel(value = "微信行驶证")
public class WechatDrivingResult extends WechatResult implements Serializable {

    @ApiModelProperty(value = "车牌号码")
    private String plate_num;

    @ApiModelProperty(value = "车辆类型")
    private String vehicle_type;

    @ApiModelProperty(value = "所有人")
    private String owner;

    @ApiModelProperty(value = "住址")
    private String addr;

    @ApiModelProperty(value = "使用性质")
    private String use_character;

    @ApiModelProperty(value = "品牌型号")
    private String model;

    @ApiModelProperty(value = "车辆识别代号")
    private String vin;

    @ApiModelProperty(value = "发动机号码")
    private String engine_num;

    @ApiModelProperty(value = "注册日期")
    private String register_date;

    @ApiModelProperty(value = "发证日期")
    private String issue_date;

    @ApiModelProperty(value = "车牌号码")
    private String plate_num_b;

    @ApiModelProperty(value = "号牌")
    private String record;

    @ApiModelProperty(value = "核定载人数")
    private String passengers_num;

    @ApiModelProperty(value = "总质量")
    private String total_quality;

    @ApiModelProperty(value = "整备质量")
    private String prepare_quality;

    @ApiModelProperty(value = "外廓尺寸")
    private String overall_size;
}
