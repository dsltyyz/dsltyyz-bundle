package com.dsltyyz.bundle.aliyun.common.properties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description:
 * OSS属性
 *
 * @author: dsltyyz
 * @date: 2019-11-19
 */
@ApiModel(description = "OSS属性")
@ConfigurationProperties("spring.cloud.alicloud.oss")
@Data
public class OssProperties {

    /**
     * 包名
     */
    @ApiModelProperty(value = "包名", required = true)
    private String bucketName;

    /**
     * 节点
     */
    @ApiModelProperty(value = "节点", required = true)
    private String endpoint;
}
