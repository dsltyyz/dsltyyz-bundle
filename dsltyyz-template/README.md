# dsltyyz-template
- 模板包
  - 当前版本: 2.3.0-SNAPSHOT
  - 开发人员:
    - [yangyuanliang](mailto:yangyuanliang@dsltyyz.com) 
## 1 目录介绍
- dsltyyz-template ___代码模板模块___
  - bean ___对象包___
  - config ___配置___
  - filter ___适配___
  - mybatisplus ___Mybatis Plus拓展配置___
  - page ___通用分页DTO/VO___
  - util ___工具包___
  - resources ___资源包___
    - META-INF ___初始化配置___
    - user-defined  ___用户自定义模板包___
## 2 快速入门
### 2.0 pom.xml配置
~~~
 <dependencies>
        ...
    <dependency>
        <groupId>com.dsltyyz.bundle</groupId>
        <artifactId>dsltyyz-template</artifactId>
    </dependency>
    ...
 </dependencies>
~~~
### 2.1 代码生成
    基于springboot+mybatisplus+druid+msql+swagger代码生成
#### 2.1.1 配置文件 支持yml>xml
##### code-generator.yml
~~~
author: dsltyyz
data-source:
  url: jdbc:mysql://localhost:3306/dsltyyz
  driver-name: com.mysql.cj.jdbc.Driver
  username: root
  password: root
strategy:
  parent-package: com.dsltyyz.sms
  module-name: app
  include-table:
    table:
      - sms_app
      - sms_app_token
~~~
##### code-generator.xml
~~~
<?xml version="1.0" encoding="UTF-8"?>
<root>
    <author>coder</author>
    <datasource>
        <url>jdbc:mysql://localhost:3306/test</url>
        <driver-name>com.mysql.cj.jdbc.Driver</driver-name>
        <username>root</username>
        <password>root</password>
    </datasource>
    <strategy>
        <parent-package>项目包路径</parent-package>
        <module-name>模块名称</module-name>
        <include-table>
            <table>该模块包含数据库表</table>
        </include-table>
    </strategy>
</root>
~~~
#### 2.1.2 代码生成 CodeGenerator.java
~~~
/**
 * Description:
 * 代码生成器
 *
 * @author: dsltyyz
 * @date: 2021-04-14
 */
public class CodeGenerator {

    public static void main(String[] args) {
        CodeGeneratorUtil.autorun(CodeGenerator.class);
        #CodeGeneratorUtil.autorun(CodeGenerator.class, "配置文件资源路径");
    }

}
~~~
#### 2.1.3 数据字典生成 DictionaryGenerator.java
~~~
/**
 * Description:
 * 数据字典 存储在resources下
 *
 * @author: dsltyyz
 * @date: 2021-04-14
 */
public class DictionaryGenerator {

    public static void main(String[] args) {
        CodeGeneratorUtil.databaseDictionary(DictionaryGenerator.class);
        #CodeGeneratorUtil.databaseDictionary(DictionaryGenerator.class, "配置文件资源路径");
    }

}
~~~
#### 2.1.4 数据库敏感信息加密（生产环境） MpwGenerator.java
~~~
/**
 * Description:
 * 数据库敏感信息加密
 *
 * @author: dsltyyz
 * @date: 2021-04-14
 */
public class MpwGenerator {

    public static void main(String[] args) {
        String key = AES.generateRandomKey();
        Properties properties = new Properties();
        properties.put("url", "jdbc:mysql://localhost:3306/test?autoReconnect=true&useUnicode=true&createDatabaseIfNotExist=true&characterEncoding=utf8&useSSL=false&serverTimezone=CTT");
        properties.put("username", "root");
        properties.put("password", "root");
        Properties encrypt = MpwUtils.encrypt(properties, key);
        MpwUtils.decrypt(encrypt, key);
        
        //输出结果:
        //MPW加密
        //作为console启动参数
        //--mpw.key=335124bfb8ae861b
        //替换原有yaml配置
        //username: mpw:IZx6VcM7MGuBqyAj0ofOBQ==
        //password: mpw:IZx6VcM7MGuBqyAj0ofOBQ==
        //url: mpw:+Dp3lyYxSBYDQKKwlezwunoFjCgnO1Mfw3Q+kBI6BMTQO5pYYyeEEfmVd3mNTP0p3TH1TQ5A7VjgJyIGcRhRVeIArXXU+WkIbvOZqEhrmmMtfqbcb8ddbMpSMk40ExpNN5HTJWWm0q277bxJroQaP6Vnf/bFm69Uhnl6Ic3q+l1UepWCKZogFS4GwS+uznY3xsO2roAzE/JbiLXhP6QApw==

    }

}
~~~
#### 3 其他相关配置 application.yml
~~~
spring:
  datasource:
    druid:
      #mybatis-plus-decrypt 自定义解密
      filters: mybatis-plus-decrypt,stat,wall,log4j
        ...
mybatis-plus:
  #mapper xml文件扫描位置
  mapper-locations: classpath*:mapper/*.xml
  #实体包
  type-aliases-package: 当前项目路径.**.entity
  #枚举包
  type-enums-package: com.dsltyyz.bundle.template.enums;当前项目路径.**.enums
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
~~~