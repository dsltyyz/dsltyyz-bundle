# dsltyyz-jwt
- Java Web Token包
  - 当前版本: 1.0.0
  - 开发人员:
    - [yangyuanliang](mailto:yangyuanliang@dsltyyz.com) 
## 1 目录介绍
- dsltyyz-jwt ___Java Web Token___
  - config ___配置___
  - constant ___常量___
  - entity ___实体___
  - helper ___助手___
  - rsa ___RSA___
  - token ___口令验证___
  - resources ___资源包___
      - META-INF ___初始化配置___
## 2 快速入门
### 2.0 pom.xml配置
~~~
 <dependencies>
        ...
    <dependency>
        <groupId>com.dsltyyz.bundle</groupId>
        <artifactId>dsltyyz-jwt</artifactId>
    </dependency>
    ...
 </dependencies>
~~~
### 2.1 配置yaml文件
~~~
...
key-pair:
  #仅用于生成公钥私钥文件种子 
  seed: 种子秘钥（用户设定）
  #生成及加载公钥路径（用于解析token）
  public-key-path: rsa/public.pem
  #生成及加载私钥路径（用于信息组装token）
  private-key-path: rsa/private.pem
...
~~~
### 2.2 生成公钥私钥文件 RSAGenerator.java
~~~
@RunWith(SpringRunner.class)
@SpringBootTest(classes = XXXApplication.class)
public class RSAGenerator {

    @Resource
    private KeyPairHelper keyPairHelper;

    @Test
    public void initRSA() {
        keyPairHelper.createKeyPairFile(RSAGenerator.class);
    }
}
~~~
## 3 前台用户
### 3.1 登录后用户信息组装JwtUser返回JwtToken
~~~
    @Resource
    private JwtHelper jwtHelper;
    ...

    /**
     * 登录模块
     */
    public JwtToken signIn(){
        ...
        //只验证登录
        JwtUser jwtUser = new JwtUser(用户ID, 用户名称);
        //验证登录及权限
        //JwtUser jwtUser = new JwtUser(用户ID, 用户名称, 权限组（{权限1,权限2}）);
        return jwtHelper.getToken(jwtUser);
    }
    ...
~~~
### 3.2 请求api头部增加Token
~~~
Token: 登录后返回的口令
~~~
### 3.3 需要验证登录方法增加注解@RequireToken
~~~
    //验证登录
    @RequireToken
    //验证登录及允许权限（与用户权限取交集）
    //@RequireToken({权限1,权限2})
    @ApiOperation(value = "获取用户信息")
    @GetMapping(value = "info")
    public JwtUser getInfo() {
        JwtUser jwtUser = (JwtUser) ContextHandler.get(JwtConstant.JWT_USER);
        return jwtUser;
    }
~~~
## 4 后台用户
### 4.1 登录后用户信息组装JwtUser返回JwtToken
~~~
    @Resource
    private JwtHelper jwtHelper;
    ...

    /**
     * 登录模块
     */
    public JwtToken signIn(){
        ...
        //只验证登录
        JwtUser jwtUser = new JwtUser(用户ID, 用户名称);
        //验证登录及权限
        //JwtUser jwtUser = new JwtUser(用户ID, 用户名称, 权限组（{权限1,权限2}）);
        return jwtHelper.getToken(jwtUser);
    }
    ...
~~~
### 4.2 请求api头部增加AdminToken
~~~
AdminToken: 登录后返回的口令
~~~
### 4.3 需要验证登录方法增加注解@RequireToken
~~~
    //验证登录
    @RequireAdminToken
    //验证登录及允许权限（与用户权限取交集）
    //@RequireAdminToken({权限1,权限2})
    @ApiOperation(value = "获取用户信息")
    @GetMapping(value = "info")
    public JwtUser getInfo() {
        JwtUser jwtUser = (JwtUser) ContextHandler.get(JwtConstant.JWT_ADMIN);
        return jwtUser;
    }
~~~
## 5 *其他服务支持同一套jwt，yaml文件配置
~~~
...
#需要拷贝rsa/public.pem到其他服务resources目录下
key-pair:
  #生成及加载公钥路径（分配于其他服务）
  public-key-path: rsa/public.pem
...
~~~