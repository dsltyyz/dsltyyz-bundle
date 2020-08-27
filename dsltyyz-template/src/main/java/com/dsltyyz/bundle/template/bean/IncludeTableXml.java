package com.dsltyyz.bundle.template.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Description:
 * 包含生成代码表
 *
 * @author: dsltyyz
 * @date: 2019/2/20
 */
@XmlRootElement(name = "include-table")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncludeTableXml {

    @XmlElement(name = "table")
    private List<String> table;
}
