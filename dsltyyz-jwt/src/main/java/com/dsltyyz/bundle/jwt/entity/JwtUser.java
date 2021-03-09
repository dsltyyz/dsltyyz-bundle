package com.dsltyyz.bundle.jwt.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * Description:
 * JWT用户信息
 *
 * @author: dsltyyz
 * @since: 2020-9-8
 */
@ApiModel(description = "JWT用户信息")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class JwtUser {

    @NonNull
    @ApiModelProperty(value = "编号")
    public Long id;

    @NonNull
    @ApiModelProperty(value = "用户名")
    public String user;

    @ApiModelProperty(value = "角色")
    public String[] role = {};

}
