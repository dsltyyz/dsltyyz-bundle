package ${package.Entity?replace('entity','dto')};

import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * ${table.comment!}DTO
 *
 * @author ${author}
 * @since ${date}
 */
@ApiModel(description = "${table.comment!}DTO")
@Data
public class ${entity}DTO implements Serializable {

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#assign list = ["deleted", "version", "create_time", "update_time"]>
<#list table.fields as field>
    <#-- ----------  排除以下字段  ---------->
    <#if !list?seq_contains(field.name)>
    <#if field.keyFlag>
    @ApiModelProperty(value = "${field.comment}", hidden = true)
    <#else>
    @ApiModelProperty(value = "${field.comment}")
    </#if>
    <#if field.propertyType == "LocalDateTime">
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    </#if>
    private ${field.propertyType} ${field.propertyName};

    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
}
