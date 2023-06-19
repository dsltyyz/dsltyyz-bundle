# dsltyyz-cache包
- 数据库缓存模块
  - 当前版本: 2.3.0-SNAPSHOT
  - 开发人员:
    - [yangyuanliang](mailto:yangyuanliang@dsltyyz.com) 
## 1 目录介绍
- dsltyyz-cache ___数据库缓存包___
  - config ___配置___
  - enums ___枚举___
  - annotation ___注解___
  - aop ___AOP___
  - resources ___资源包___
      - META-INF ___初始化配置___
## 2 快速入门
### 2.0 pom.xml配置
~~~
 <dependencies>
        ...
    <dependency>
        <groupId>com.dsltyyz.bundle</groupId>
        <artifactId>dsltyyz-cache</artifactId>
    </dependency>
    ...
 </dependencies>
~~~
### 2.1 配置yaml文件
~~~
...
spring:
  ...
  #一级缓存
  redis:
    enable: true
    database: 0
    host: IP地址
    port: 6379
    password: 密码
    timeout: 10000
    jedis:
      pool:
        max-active: 20
        max-wait: -1
        max-idle: 10
        min-idle: 0 
#二级缓存
lettuce:
  timeout: 30 #过期时间 默认60秒
  pool:
    max-active: 8
    max-wait: -1ms
    max-idle: 8
    min-idle: 0
...
~~~
### 2.2 启动类配置注解@EnableCaching
~~~
/**
  * Description:
  * springboot启动类
  *
  * @author: dsltyyz
  * @date: 2020-8-28
  */
 @EnableAsync
 //TIPS: 缓存注解
 @EnableCaching
 @EnableFeignClients(basePackages = {"com.dsltyyz.module"})
 @EnableDiscoveryClient
 @SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
 public class DynamicUserApplication {
 
     public static void main(String[] args) {
         SpringApplication.run(DynamicUserApplication.class, args);
     }
 
 }
~~~
## 3 注解及参数说明
### 3.1 一级缓存RedisCache
~~~
/**
 * REDIS缓存
 * @author dsltyyz
 * @date 2022-10-11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisCache {
    //缓存名称
    String cacheName();

    //支持springEl表达式
    String key();

    //REDIS超时时间（秒）
    long redisTimeOut() default 120;

    //缓存类型
    CacheType type() default CacheType.FULL;
}
~~~
### 3.2 二级缓存CaffeineRedisCache
~~~
/**
 * 二级缓存
 * @author dsltyyz
 * @date 2022-10-11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CaffeineRedisCache {
    //缓存名称
    String cacheName();

    //支持springEl表达式
    String key();

    //REDIS超时时间（秒）
    long redisTimeOut() default 120;

    //缓存类型
    CacheType type() default CacheType.FULL;
}
~~~
## 4 示例
### 4.1 一级缓存RedisCache及二级缓存CaffeineRedisCache示例
~~~
/**
 * <p>
 * 测试 Service Impl
 * </p>
 *
 * @author dsltyyz
 * @since 2022-04-02
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestDAO, Test> implements TestService {

    @Resource
    private TestDAO testDAO;

    ...

    /**
     * 更新
     *
     * @param dto
     */
    @Override
    @CaffeineRedisCache(cacheName = "test", key = "#dto.id", type = CacheType.PUT)
    //@RedisCache(cacheName = "test", key = "#dto.id", type = CacheType.PUT)
    //TIPS: 将原有返回值void改成get方法相同返回值
    public TestVO updateTest(TestDTO dto){
        Test test =  testDAO.selectById(dto.getId());
        Assert.notNull(test, "该ID对应测试不存在");
        BeanUtils.copyProperties(dto, test);
        testDAO.updateById(test);

        TestVO  vo = new TestVO();
        BeanUtils.copyProperties(test, vo);
        return vo;
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    @CaffeineRedisCache(cacheName = "test", key = "#id", type = CacheType.DELETE)
    //@RedisCache(cacheName = "test", key = "#id", type = CacheType.DELETE)
    public void deleteTest(Long id){
        Test test =  testDAO.selectById(id);
        Assert.notNull(test, "该ID对应测试不存在");
        testDAO.deleteById(id);
    }

    /**
     * 查询
     *
     * @param id
     * @return
     */
    @CaffeineRedisCache(cacheName = "test", key = "#id")
    //@RedisCache(cacheName = "test", key = "#id")
    @DS("cluster")
    @Override
    public TestVO getTestById(Long id){
        TestVO  vo = new TestVO();
        Test entity =  testDAO.selectById(id);
        Assert.notNull(entity, "该ID对应测试不存在");
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    ...

}
~~~
