# dsltyyz-aliyun包
- aliyun包
  - 当前版本: 2.2.0-SNAPSHOT
  - 开发人员:
    - [yangyuanliang](mailto:yangyuanliang@dsltyyz.com) 
## 1 目录介绍
- dsltyyz-aliyun ___阿里云组件___
  - client ___客户端___
  - common ___对象定义___
  - config ___条件注入___
  - resources ___资源包___
      - META-INF ___初始化配置___
## 2 快速入门
### 2.0 pom.xml配置
~~~
 <dependencies>
        ...
    <dependency>
        <groupId>com.dsltyyz.bundle</groupId>
        <artifactId>dsltyyz-aliyun</artifactId>
    </dependency>
    ...
 </dependencies>
~~~
### 2.1 配置yaml文件
~~~
...
spring:
  ...
  cloud:
    alicloud:
      access-key: 阿里云access-key
      secret-key: 阿里云secret-key
      #短信  
      sms:
        enable: true #不用 设置为false
        sign-name: 短信签名
      #对象存储
      oss:
        enabled: true #不用 设置为false
        endpoint: oss-cn-hangzhou.aliyuncs.com
        bucket-name: dsltyyz
      #支付
      pay:
        enable: true #不用 设置为false
        server-url: https://openapi.alipay.com/gateway.do
        app-id: 应用ID
        private-key: 应用私钥
        cert-path: 文件绝对路径
        alipay-public-cert-path: 文件绝对路径
        root-cert-path: 文件绝对路径
...
~~~
### 2.2 OSS对象存储
~~~
/**
 * <p>
 * 对象存储 前端控制器
 * </p>
 *
 * @author dsltyyz
 * @since 2021-04-14
 */
@Api(value = "对象存储controller", tags = {"对象存储"})
@RestController
@RequestMapping("oss")
public class OssController {

    @Resource
    private AliyunOssClient aliyunOssClient;

    @ApiOperation(value = "获取WEB直传OSS签名")
    @GetMapping(value = "signature")
    public CommonResponse<OssSignatureVO> getOssSignature(@RequestParam("fileName") String fileName, @RequestParam(name = "dir", defaultValue = "default") String dir) {
        String key = (dir + "/" + UUIDUtils.getUUIDByLength(16) + fileName.substring(fileName.lastIndexOf("."))).replaceAll("//", "/");
        OssSignatureVO ossSignatureVO = aliyunOssClient.getOssSignature(dir);
        ossSignatureVO.setKey(key);
        return new CommonResponse<>(ossSignatureVO);
    }

    @ApiOperation(value = "文件上传")
    @PostMapping(value = "file")
    public CommonResponse<OssVO> uploadFile(@RequestPart("file") MultipartFile file, @RequestParam(name = "dir", defaultValue = "default") String dir, @RequestParam(name = "key",required = false) String key) throws Exception {
        String name = file.getOriginalFilename();
        if(StringUtils.isEmpty(key)){
            key = (dir + "/" + UUIDUtils.getUUIDByLength(16) + name.substring(name.lastIndexOf("."))).replaceAll("//", "/");
        }else{
            key = dir + "/" + key;
        }

        Future<OssVO> future = aliyunOssClient.putObject(key, file.getInputStream());
        OssVO ossVO = future.get();
        ossVO.setName(name);
        return new CommonResponse<>(ossVO);
    }

}
~~~
### 2.3 短信
~~~
/**
 * <p>
 * 短信消息 前端控制器
 * </p>
 *
 * @author dsltyyz
 * @since 2021-04-14
 */
@Api(value = "短信消息 controller", tags = {"短信消息"})
@RestController
@RequestMapping("sms")
public class SmsController {

    @Resource
    private AliyunSmsClient aliyunSmsClient;

    @ApiOperation(value = "发送短信")
    @PostMapping(value = "")
    public CommonResponse create(@RequestBody @Valid UserSmsDTO userSmsDTO) {
        aliyunSmsClient.sendSms("电话号码", "模板码", "短信json参数");
        return new CommonResponse();
    }

}
~~~
### 2.4 支付宝支付
~~~
/**
 * <p>
 * 支付宝 前端控制器
 * </p>
 *
 * @author dsltyyz
 * @since 2021-04-14
 */
@Api(value = "支付宝controller", tags = {"支付宝"})
@RestController
@RequestMapping("alipay")
public class AliPayController {

    @Resource
    private AliyunPayClient aliyunPayClient;

    @ApiOperation(value = "获取页面支付")
    @GetMapping(value = "")
    public CommonResponse<String> createPagePay(@Valid AlipayOrder alipayOrder, String returnUrl) throws AlipayApiException {
        return new CommonResponse<>(aliyunPayClient.createPagePay(alipayOrder, returnUrl, null));
    }

}
~~~