package ${package.Entity?replace('entity','dto')};

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * ${table.comment!}DTO
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@ApiModel(description = "${table.comment!}DTO")
@Data
public class ${entity}DTO implements Serializable {

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>

    /**
     * ${field.comment}
     */
    @ApiModelProperty(value = "${field.comment}")
    <#if field.propertyType == "LocalDateTime">
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->
}
