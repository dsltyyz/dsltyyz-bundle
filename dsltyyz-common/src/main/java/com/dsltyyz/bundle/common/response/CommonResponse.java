package com.dsltyyz.bundle.common.response;

import com.dsltyyz.bundle.common.constant.CommonResponseStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:
 * 通用响应
 *
 * @author: dsltyyz
 * @date: 2019/2/20
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(value = "通用响应")
public class CommonResponse<T> implements Serializable {

    @ApiModelProperty(value = "状态 非0为异常")
    private Long status = CommonResponseStatus.OK;

    @ApiModelProperty(value = "状态消息")
    private String msg = "成功";

    @ApiModelProperty(value = "数据")
    private T data;

    @ApiModelProperty(value = "资源链接")
    private String resourceUrl;

    public CommonResponse(Long status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public CommonResponse(T data) {
        this.data = data;
    }

    public CommonResponse(T data, String resourceUrl) {
        this.data = data;
        this.resourceUrl = resourceUrl;
    }
}

