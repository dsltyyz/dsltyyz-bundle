# dsltyyz-wechat
- 模板包
  - 当前版本: 2.2.0-SNAPSHOT
  - 开发人员:
    - [yangyuanliang](mailto:yangyuanliang@dsltyyz.com) 
## 1 目录介绍
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
## 2 快速入门
### 2.0 pom.xml配置
~~~
 <dependencies>
        ...
    <dependency>
        <groupId>com.dsltyyz.bundle</groupId>
        <artifactId>dsltyyz-wechat</artifactId>
    </dependency>
    ...
 </dependencies>
~~~
### 2.1 配置 
#### 2.1.1 配置参数获取
微信公众平台 https://mp.weixin.qq.com/
微信支付 https://pay.weixin.qq.com/index.php/core/home/login?return_url=%2F
#### 2.1.2 配置说明 application.yml
~~~
wechat:
  oauth:
    app-id: 公众号ID
    app-secret: 公众号秘钥
  pay:
    mch-id: 商户ID
    mch-private-key: 商户KEY
    version: v3 #默认
    mch-serial-no: 版本为v3 商户API证书的证书序列号
    api-v3-key: 版本为v3 apiV3秘钥
    cert-url: 版本为v2 微信支付平台证书URL
~~~
#### 2.1.3 示例
~~~
@Api(value = "微信controller", tags = {"微信"})
@RestController
@RequestMapping("wechat")
public class WechatController {

    @Resource
    private WechatClient wechatClient;

    @ApiOperation(value = "获取小程序用户openid信息")
    @GetMapping(value = "/useropenid/jscode/{jscode}")
    public CommonResponse<WechatMiniOpenId> getUserOpenidByJsCode(@PathVariable("jscode") String jscode) {
        return new CommonResponse<>(wechatClient.getMiniOpenId(jscode));
    }

    @ApiOperation(value = "获取小程序用户手机号信息")
    @GetMapping(value = "/userphone/code/{code}")
    public CommonResponse<WechatPhoneInfo> getUserPhoneByCode(@PathVariable("code") String code) {
        return new CommonResponse<>(wechatClient.getUserPhoneInfo(code));
    }

    @ApiOperation(value = "解密微信数据")
    @GetMapping(value = "/decrypt")
    public CommonResponse decrypt(@RequestParam("data") String data, @RequestParam("key") String key, @RequestParam("iv") String iv) throws Exception {
        return new CommonResponse<>(WechatCommonUtils.decrypt(data, key, iv));
    }

}
~~~