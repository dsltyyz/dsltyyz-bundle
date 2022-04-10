package com.dsltyyz.bundle.aliyun.common.properties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description:
 * 短信属性
 *
 * @author: dsltyyz
 * @date: 2019-11-19
 */
@ApiModel(description = "短信属性")
@ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
@Data
public class SmsProperties {

    /**
     * 签名
     */
    @ApiModelProperty(value = "签名", required = true)
    private String signName;
}
