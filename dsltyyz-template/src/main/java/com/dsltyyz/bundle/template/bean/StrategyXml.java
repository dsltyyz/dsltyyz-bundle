package com.dsltyyz.bundle.template.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Description:
 * 策略
 *
 * @author: dsltyyz
 * @since: 2019-2-20
 */
@XmlRootElement(name = "strategy")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StrategyXml {

    /**
     * 父包
     */
    @XmlElement(name = "parent-package")
    @JSONField(name = "parent-package")
    private String parentPackage;

    /**
     * 模块名称
     */
    @XmlElement(name = "module-name")
    @JSONField(name = "module-name")
    private String moduleName;

    /**
     * 模块名称
     */
    @XmlElement(name = "include-table")
    @JSONField(name = "include-table")
    private IncludeTableXml includeTable;

}
