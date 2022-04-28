package com.dsltyyz.bundle.aliyun.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

/**
 * Description:
 * OSS签名VO
 *
 * @author: dsltyyz
 * @date: 2019-11-19
 */
@ApiModel(description = "OSS签名VO")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OssSignatureVO implements Serializable {

    /**
     * 访问KEY
     */
    @NonNull
    @ApiModelProperty(value = "访问KEY")
    private String accessKey;

    /**
     * 策略
     */
    @NonNull
    @ApiModelProperty(value = "策略")
    private String policy;

    /**
     * 签名
     */
    @NonNull
    @ApiModelProperty(value = "签名")
    private String signature;

    /**
     * 主机
     */
    @NonNull
    @ApiModelProperty(value = "主机")
    private String host;

}
