package com.dsltyyz.bundle.template.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Description:
 * 数据源
 *
 * @author: dsltyyz
 * @date: 2019/2/20
 */
@XmlRootElement(name = "datasource")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceXml {

    @XmlElement(name = "url")
    private String url;

    @XmlElement(name = "driver-name")
    private String driverName;

    @XmlElement(name = "username")
    private String username;

    @XmlElement(name = "password")
    private String password;
}
