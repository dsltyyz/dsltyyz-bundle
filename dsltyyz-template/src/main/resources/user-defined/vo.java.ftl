package ${package.Entity?replace('entity','vo')};

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * ${table.comment!}VO
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@ApiModel(description = "${table.comment!}VO")
@Data
public class ${entity}VO implements Serializable {

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#assign list = ["deleted", "version"]>
<#list table.fields as field>
    <#-- ----------  排除以下字段  ---------->
    <#if !list?seq_contains(field.name)>
    @ApiModelProperty(value = "${field.comment}")
    <#if field.propertyType == "LocalDateTime">
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    </#if>
    private ${field.propertyType} ${field.propertyName};

    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
}
