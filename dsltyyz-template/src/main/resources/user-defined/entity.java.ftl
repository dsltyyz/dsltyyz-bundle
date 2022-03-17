package ${package.Entity};

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import com.dsltyyz.bundle.common.util.DateUtils;
import com.dsltyyz.bundle.template.enums.*;

/**
 * ${table.comment!}
 *
 * @author ${author}
 * @since ${date}
 */
@ApiModel(description = "${table.comment!}")
@Data
@TableName(value = "${table.name}", autoResultMap = true)
public class ${entity} implements Serializable {

    private static final long serialVersionUID = 1L;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    @ApiModelProperty(value = "${field.comment}")
    <#if field.propertyType == "LocalDateTime">
    @JsonFormat(pattern = DateUtils.PATTERN_DATETIME)
    </#if>
    <#if field.keyFlag>
    <#-- 主键 -->
    @TableId(value = "${field.name}", type = IdType.AUTO)
    <#elseif field.name == "version">
    @TableField(value = "version", fill = FieldFill.INSERT)
    @Version
    <#elseif field.name == "deleted">
    @TableField(value="deleted", select = false, fill = FieldFill.INSERT)
    @TableLogic
    <#elseif field.name == "create_time">
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    <#elseif field.name == "update_time">
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    <#else>
    <#-- 普通字段 -->
    @TableField("${field.name}")
    </#if>
    private <#if field.name == "status">CommonStatus${field.propertyType}</#if> ${field.propertyName};

</#list>
<#------------  END 字段循环遍历  ---------->
}
