package com.dsltyyz.bundle.common.jwt.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * JWT用户信息
 *
 * @author: dsltyyz
 * @date: 2020-9-8
 */
@ApiModel(description = "JWT用户信息")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JwtUser {

    @ApiModelProperty(value = "编号")
    public Long id;

    @ApiModelProperty(value = "用户名")
    public String user;

}
