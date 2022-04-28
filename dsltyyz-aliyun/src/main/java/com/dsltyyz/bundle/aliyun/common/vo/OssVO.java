package com.dsltyyz.bundle.aliyun.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * Description:
 * OSS值对象
 *
 * @author: dsltyyz
 * @date: 2019-11-19
 */
@ApiModel(description = "OSS值对象")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class OssVO implements Serializable {

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 唯一标识
     */
    @NonNull
    @ApiModelProperty(value = "唯一标识", required = true)
    private String key;

    /**
     * 访问路径
     */
    @NonNull
    @ApiModelProperty(value = "访问路径", required = true)
    private String url;
}
