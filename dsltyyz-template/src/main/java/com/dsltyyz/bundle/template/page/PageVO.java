package com.dsltyyz.bundle.template.page;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 分页VO
 *
 * @author: dsltyyz
 * @date: 2020-05-20
 */
@ApiModel(description = "分页VO")
@Data
public class PageVO<T> implements Serializable {

    @ApiModelProperty(value = "数据列表")
    private List<T> records;

    @ApiModelProperty(value = "总数目")
    private Long total;

    @ApiModelProperty(value = "每页数目")
    private Long size;

    @ApiModelProperty(value = "当前页")
    private Long current;

    @ApiModelProperty(value = "升序排列")
    private List<OrderItem> orders;

    @ApiModelProperty(value = "是否统计sql")
    private Boolean optimizeCountSql;

    @ApiModelProperty(value = "是否搜索数量")
    protected Boolean isSearchCount;

    @ApiModelProperty(value = "是否命中数量")
    protected Boolean hitCount;

}
