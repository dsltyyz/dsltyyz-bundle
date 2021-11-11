# dsltyyz捆绑包
- 快速开发捆绑包
  - 当前版本: 2.1.0-SNAPSHOT
  - 开发人员:
    - [yangyuanliang](mailto:yangyuanliang@dsltyyz.com) 
## 1 目录介绍
- dsltyyz-bundle ___包依赖及模块___
- dsltyyz-aliyun ___阿里云组件___
  - client ___客户端___
  - common ___对象定义___
  - config ___条件注入___
  - resources ___资源包___
      - META-INF ___初始化配置___
- dsltyyz-common ___公共模块___
  - cache ___缓存___
  - config ___缓存自动注入___
  - constant ___常量定义包___
  - data ___数据格式包___
  - entity ___实体包___
  - factory ___CompositePropertySourceFactory处理@PropertySource支持yaml文件___
  - handler ___处理包___
  - properties ___属性包___
  - response ___通用响应包___
  - util ___通用工具包___
  - vo ___值对象___
  - resources ___资源包___
      - META-INF ___初始化配置___
- dsltyyz-dependencies ___捆绑包版本依赖___
- dsltyyz-jwt ___Java Web Token___
  - config ___配置___
  - constant ___常量___
  - entity ___实体___
  - helper ___助手___
  - rsa ___RSA___
  - token ___口令验证___
  - resources ___资源包___
      - META-INF ___初始化配置___
- dsltyyz-office ___office办公模块___
  - excel ___excel工具包___
    - annotation ___注解___
    - entity ___实体___
    - util ___工具类___
  - pdf ___PDF工具包___
- dsltyyz-template ___代码模板模块___
  - bean ___对象包___
  - config ___配置___
  - filter ___适配___
  - handler ___处理器___
  - mybatisplus ___Mybatis Plus拓展配置___
  - page ___通用分页DTO/VO___
  - util ___工具包___
  - resources ___资源包___
    - META-INF ___初始化配置___
    - user-defined  ___用户自定义模板包___
- dsltyyz-wechat ___微信模块___
  - client ___客户端___
  - common ___通用___
    - constant ___常量___
    - model ___模型___
    - property ___属性___
    - util ___工具___
  - config ___配置___ 
  - resources ___资源包___
      - META-INF ___初始化配置___
## 2 搭建介绍
### 2.1 环境介绍
> 环境检测
- 配置JAVA_HOME 查看java版本 `java -version`
- 配置M2_HOME 查看maven版本 `mvn -v`
### 2.2 安装介绍
> 在dsltyyz-bundle目录下 二选一
- 未搭建私服 执行 `mvn install -Dmaven.test.skip=true `
- 已搭建私服 执行 `mvn deploy -Dmaven.test.skip=true `
## 3 maven使用介绍
### pom.xml
~~~
<properties>
    ...
    <dsltyyz-bundle.version>2.1.0-SNAPSHOT</dsltyyz-bundle.version>
    ...
</properties>
<dependencyManagement>
    <dependencies>
        ...
        <dependency>
            <groupId>com.dsltyyz.bundle</groupId>
            <artifactId>dsltyyz-dependencies</artifactId>
            <version>${dsltyyz-bundle.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        ...
    </dependencies>
</dependencyManagement>
<dependencies>
    ...
    <dependency>
        <groupId>com.dsltyyz.bundle</groupId>
        <artifactId>dsltyyz-common</artifactId>
    </dependency>
    <dependency>
        <groupId>com.dsltyyz.bundle</groupId>
        <artifactId>dsltyyz-jwt</artifactId>
    </dependency>
    <dependency>
        <groupId>com.dsltyyz.bundle</groupId>
        <artifactId>dsltyyz-aliyun</artifactId>
    </dependency>
    <dependency>
        <groupId>com.dsltyyz.bundle</groupId>
        <artifactId>dsltyyz-office</artifactId>
    </dependency>
    <dependency>
        <groupId>com.dsltyyz.bundle</groupId>
        <artifactId>dsltyyz-template</artifactId>
    </dependency>
    <dependency>
        <groupId>com.dsltyyz.bundle</groupId>
        <artifactId>dsltyyz-wechat</artifactId>
    </dependency>
    ...
 </dependencies>
~~~
