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
 * 数据源
 *
 * @author: dsltyyz
 * @since: 2019-2-20
 */
@XmlRootElement(name = "data-source")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceXml {

    /**
     * 访问链接
     */
    @XmlElement(name = "url")
    @JSONField(name = "url")
    private String url;

    /**
     * 驱动名称
     */
    @XmlElement(name = "driver-name")
    @JSONField(name = "driver-name")
    private String driverName;

    /**
     * 用户
     */
    @XmlElement(name = "username")
    @JSONField(name = "username")
    private String username;

    /**
     * 密码
     */
    @XmlElement(name = "password")
    @JSONField(name = "password")
    private String password;
}
