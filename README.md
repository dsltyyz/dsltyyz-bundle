# dsltyyz捆绑包
    快速开发捆绑包
    当前版本: 2.0.0-SNAPSHOT
    开发人员： 
        [yangyuanliang](mailto:yangyuanliang@dsltyyz.com) 
## 1 目录介绍
[yangyuanliang](mailto:yangyuanliang@dsltyyz.com) 
<yangyuanliang@dsltyyz.com>
- dsltyyz-common ___公共模块___
  - cache ___缓存___
  - config ___缓存自动注入___
  - constant ___常量定义包___
  - factory ___CompositePropertySourceFactory处理@PropertySource支持yaml文件___
  - handler ___处理包___
  - page ___通用分页DTO/VO___
  - response ___通用响应包___
  - util ___通用工具包___
- dsltyyz-dependencies ___捆绑包版本依赖___
- dsltyyz-office ___office办公模块___
  - pdf ___PDF工具包___
  - excel ___excel工具包___
- dsltyyz-template ___代码模板模块___
  - bean ___对象包___
  - util ___工具包___
  - resources ___资源包___
    - user-defined  ___用户自定义模板包___
## 2 搭建介绍
### 2.1 环境介绍
    1.配置JAVA_HOME
    2.配置M2_HOME
### 2.2 安装介绍
> 在dsltyyz-bundle目录下 二选一
- 未搭建私服 执行 `mvn install -Dmaven.test.skip=true `
- 已搭建私服 执行 `mvn deploy -Dmaven.test.skip=true `
## 3 maven使用介绍
### pom.xml
~~~
<properties>
    ...
    <dsltyyz-bundle.version>2.0.0-SNAPSHOT</dsltyyz-bundle.version>
    ...
</properties>
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
~~~
