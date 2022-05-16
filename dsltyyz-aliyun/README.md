# dsltyyz-aliyun包
- aliyun包
  - 当前版本: 1.0.0
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
### 2.1 传送门
- [支付宝支付文档](https://opendocs.alipay.com/open/01zuoj)
- [支付宝沙盒平台](https://open.alipay.com/develop/sandbox/app)
### 2.2 配置yaml文件
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
        enable: true #默认 不用设置为false 
        sign-name: 短信签名
      #对象存储
      oss:
        enabled: true #默认 不用设置为false 
        endpoint: 节点域名
        bucket-name: 包名称
      #支付
      pay:
        enable: true #默认为false 启用设置为true
        server-url: https://openapi.alipay.com/gateway.do #默认 沙盒设置为https://openapi.alipaydev.com/gateway.do
        app-id: 应用ID
        private-key: 应用私钥
        mode: CERT #默认 公钥设置为KEY
        #MODE KEY
        alipay-public-key: 支付宝公钥
        #MODE CERT
        cert-path: 文件绝对路径 #appCertPublicKey_${app-id}.crt
        alipay-public-cert-path: 文件绝对路径 #alipayCertPublicKey_RSA2.crt
        root-cert-path: 文件绝对路径 #alipayRootCert.crt
...
~~~
## 3 示例
### 3.1 OSS对象存储
~~~
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
### 3.2 短信
~~~
@Api(value = "短信消息controller", tags = {"短信消息"})
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
### 3.3 支付
~~~
@Api(value = "支付宝controller", tags = {"支付宝"})
@RestController
@RequestMapping("alipay")
public class AliPayController {

    @Resource
    private AliyunPayClient aliyunPayClient;

    @Resource
    private AliyunOssClient aliyunOssClient;

    @ApiOperation(value = "获取预支付")
    @GetMapping(value = "precreate")
    public CommonResponse<String> createAlipayTradePrecreate(@Valid AlipayOrder alipayOrder) throws AlipayApiException {
        String s = aliyunPayClient.createAlipayTradePrecreate(alipayOrder, "异步通知url 不通知设置为null");
        String key = "alipay/" + alipayOrder.getOut_trade_no() + "." + ImageType.JPG;
        aliyunOssClient.putObject(key, QRCodeUtils.encode(s));
        return new CommonResponse<>(aliyunOssClient.getResourceUrl(key));
    }

    @ApiOperation(value = "获取页面支付")
    @GetMapping(value = "page")
    public String createAlipayTradePagePay(@Valid AlipayOrder alipayOrder, String returnUrl) throws AlipayApiException {
        return aliyunPayClient.createAlipayTradePagePay(alipayOrder, returnUrl, "异步通知url 不通知设置为null");
    }

    @ApiOperation(value = "获取APP支付")
    @GetMapping(value = "app")
    public String createAlipayTradeAppPay(@Valid AlipayOrder alipayOrder) throws AlipayApiException {
        return aliyunPayClient.createAlipayTradeAppPay(alipayOrder, "异步通知url 不通知设置为null");
    }

    @ApiOperation(value = "获取WAP支付")
    @GetMapping(value = "wap")
    public String createAlipayTradeAppPay(@Valid AlipayOrder alipayOrder, String returnUrl) throws AlipayApiException {
        return aliyunPayClient.createAlipayTradeWapPay(alipayOrder, returnUrl, "异步通知url 不通知设置为null");
    }

    @ApiOperation(value = "获取订单详情")
    @GetMapping(value = "order")
    public JSONObject createAlipayTradeAppPay(@RequestParam("outTradeNo") String outTradeNo) throws AlipayApiException {
        return aliyunPayClient.getAlipayTradeQuery(outTradeNo);
    }

    @ApiOperation(value = "订单退款")
    @PostMapping(value = "refund")
    public JSONObject createAlipayTradeAppPay(@RequestParam("outTradeNo") String outTradeNo, @RequestParam("refundAmount") String refundAmount) throws AlipayApiException {
        return aliyunPayClient.createAlipayTradeRefund(outTradeNo, refundAmount);
    }

    @ApiOperation(value = "查询订单退款")
    @GetMapping(value = "refund")
    public JSONObject getAlipayTradeFastpayRefundQuery(@RequestParam("outTradeNo") String outTradeNo) throws AlipayApiException {
        return aliyunPayClient.getAlipayTradeFastpayRefundQuery(outTradeNo);
    }

    @ApiOperation(value = "异步支付回调")
    @PostMapping(value = "v1/pay/callback")
    public String payCallback(HttpServletRequest request) {
        System.out.println("------------request parameter参数-------------");
        System.out.println(JSONObject.toJSONString(request.getParameterMap()));
        // 解析回调数据
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        AlipayNotifyResult alipayNotifyResult = aliyunPayClient.dealData(params);
        System.out.println("------------json转对象数据-------------");
        System.out.println(JSONObject.toJSONString(alipayNotifyResult));
        return "success";
    }
}
~~~