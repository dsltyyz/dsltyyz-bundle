package com.dsltyyz.bundle.common.page;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 分页DTO
 *
 * @author: dsltyyz
 * @since: 2020-05-20
 */
@ApiModel(description = "分页DTO")
@Data
@NoArgsConstructor
public class PageDTO implements Serializable {

    @ApiModelProperty(value = "当前页", required = true)
    @Min(value = 1L, message = "当前页>=1")
    private Long current;

    @ApiModelProperty(value = "每页数目", required = true)
    @Min(value = 1L, message = "每页>=1")
    @Max(value = 100, message = "每页<=100")
    private Long size;

    @ApiModelProperty(value = "升序排列")
    private List<OrderItem> orders;

}
