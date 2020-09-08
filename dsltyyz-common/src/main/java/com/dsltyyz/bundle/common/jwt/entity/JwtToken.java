package com.dsltyyz.bundle.common.jwt.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Description:
 * Jwt令牌
 *
 * @author: dsltyyz
 * @date: 2020-9-8
 */
@ApiModel(description = "Jwt令牌")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JwtToken {

    @ApiModelProperty(value = "令牌")
    private String token;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "过期时间")
    private LocalDateTime expireTime;
}
