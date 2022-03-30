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
 * Mybatis Plus 代码生成器XML配置类
 *
 * @author: dsltyyz
 * @since: 2019-2-20
 */
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MybatisPlusCodeGeneratorXml {

    /**
     * 创建人
     */
    @XmlElement(name = "author")
    @JSONField(name = "author")
    private String author;

    /**
     * 数据源
     */
    @XmlElement(name = "data-source")
    @JSONField(name = "data-source")
    private DataSourceXml dataSource;

    /**
     * 策略
     */
    @XmlElement(name = "strategy")
    @JSONField(name = "strategy")
    private StrategyXml strategy;

}
