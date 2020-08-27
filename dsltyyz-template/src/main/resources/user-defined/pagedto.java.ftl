package ${package.Entity?replace('entity','dto')};

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import PageDTO;
import java.io.Serializable;

/**
 * <p>
 * ${table.comment!}分页DTO
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@ApiModel(description = "${table.comment!}分页DTO")
@EqualsAndHashCode(callSuper = true)
@Data
public class ${entity}PageDTO extends PageDTO implements Serializable {

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if !field.keyFlag>
    /**
     * ${field.comment}
     */
    @ApiModelProperty(value = "${field.comment}")
    <#if field.propertyType == "LocalDateTime">
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    </#if>
    private ${field.propertyType} ${field.propertyName};
    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
}
