# dsltyyz-common
- 公共包
  - 当前版本: 2.2.0-SNAPSHOT
  - 开发人员:
    - [yangyuanliang](mailto:yangyuanliang@dsltyyz.com) 
## 1 目录介绍
- dsltyyz-common ___公共模块___
  - cache ___缓存___
  - config ___缓存自动注入___
  - constant ___常量定义包___
  - entity ___实体包___
  - factory ___CompositePropertySourceFactory处理@PropertySource支持yaml文件___
  - handler ___处理包___
  - properties ___属性包___
  - response ___通用响应包___
  - util ___通用工具包___
  - vo ___值对象___
  - resources ___资源包___
      - META-INF ___初始化配置___
## 2 快速入门（建议初始化依赖）
### 2.0 pom.xml配置
~~~
 <dependencies>
    ...
    <dependency>
        <groupId>com.dsltyyz.bundle</groupId>
        <artifactId>dsltyyz-common</artifactId>
    </dependency>
    ...
 </dependencies>
~~~
### 2.1 yaml配置 不配置用内存缓存 配置了用redis缓存
~~~
spring:
  redis:
    #开启redis作为缓存
    enable: true
    database: 0
    host: 127.0.0.1
    port: 6379
    password: dsltyyz
    timeout: 10000
    jedis:
      pool:
        max-active: 20
        max-wait: -1
        max-idle: 10
        min-idle: 0
~~~
### 2.2 缓存客户端
~~~
    @Resource
    private CacheClient cacheClient;
~~~
### 2.3 数据处理
~~~
    @ApiModelProperty(value = "创建时间")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //DataFormat等同于JsonFormat 可以自行定义
    @DataFormat(value = DateDataHelper.class, param = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
~~~