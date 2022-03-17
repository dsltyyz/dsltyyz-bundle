package ${package.Entity?replace('entity','dto')};

import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.io.Serializable;
import com.dsltyyz.bundle.template.page.PageDTO;
import com.dsltyyz.bundle.common.util.DateUtils;
import com.dsltyyz.bundle.template.enums.*;

/**
 * ${table.comment!} Page DTO
 *
 * @author ${author}
 * @since ${date}
 */
@ApiModel(description = "${table.comment!}分页DTO")
@EqualsAndHashCode(callSuper = true)
@Data
public class ${entity}PageDTO extends PageDTO implements Serializable {

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#assign list = ["deleted", "version", "create_time", "update_time"]>
<#list table.fields as field>
    <#-- ----------  排除以下字段  ---------->
    <#if !list?seq_contains(field.name) && !field.keyFlag>
    @ApiModelProperty(value = "${field.comment}")
    <#if field.propertyType == "LocalDateTime">
    @DateTimeFormat(pattern = DateUtils.PATTERN_DATETIME)
    </#if>
    private <#if field.name == "status">CommonStatus<#else>${field.propertyType}</#if> ${field.propertyName};

    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
}
