# dsltyyz-wechat
- 模板包
  - 当前版本: 2.3.0-SNAPSHOT
  - 开发人员:
    - [yangyuanliang](mailto:yangyuanliang@dsltyyz.com) 
## 1 目录介绍
- dsltyyz-wechat ___微信模块___
  - aes ___安全模式AES___
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
    token: 令牌
    encoding-type: 消息加解密方式
    encoding-aes-key: 消息加解密密钥
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
    private WechatProperties wechatProperties;

    @Resource
    private WechatClient wechatClient;

    @ApiOperation(value = "微信请求GET回调")
    @GetMapping("/check")
    public String check(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp,
                        @RequestParam("nonce") String nonce, @RequestParam("echostr") String echostr) {
        return WechatCommonUtils.callbackCheck(signature, timestamp, nonce, echostr, wechatProperties.getOauth().getToken());
    }

    @ApiOperation(value = "微信消息回调")
    @PostMapping("/check")
    public String check(HttpServletRequest request) throws Exception {
        System.out.println(JSONObject.toJSONString(request.getParameterMap()));
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        if (!WechatCommonUtils.checkSignature(signature, timestamp, nonce, wechatProperties.getOauth().getToken())) {
            return "消息不合法";
        }

        String encryptType = request.getParameter("encrypt_type");
        String msg;
        if (encryptType == null) {
            // 明文传输的消息
            msg = StreamUtils.inputStreamToString(request.getInputStream());
        } else if ("aes".equals(encryptType)) {
            // aes加密的消息
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(wechatProperties.getOauth().getAppId(), wechatProperties.getOauth().getToken(), wechatProperties.getOauth().getEncodingAesKey());
            String msgSignature = request.getParameter("msg_signature");
            msg = wxBizMsgCrypt.decryptMsg(msgSignature, timestamp, nonce, StreamUtils.inputStreamToString(request.getInputStream()));
        } else {
            return "不可识别的加密类型";
        }
        System.out.println(XmlUtils.xmlToJSONObject(msg).toJSONString());
        return "success";
    }

    @ApiOperation(value = "获取用户订阅信息")
    @GetMapping(value = "/user/subscribe/{openid}")
    public CommonResponse<WechatUserSubscribe> getUserInfo(@PathVariable("openid") String openid) {
        return new CommonResponse<>(wechatClient.getUserSubscribe(openid));
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping(value = "/user/{code}")
    public CommonResponse<WechatUser> getUserInfoByCode(@PathVariable("code") String code) {
        return new CommonResponse<>(wechatClient.getUserInfoByCode(code));
    }

    @ApiOperation(value = "创建草稿")
    @PostMapping(value = "/article")
    public CommonResponse<WechatMaterial> createArticle() {
        DsltyyzDraftSend dsltyyzDraftSend = new DsltyyzDraftSend();
        //配置属性
        WechatMaterial wechatMaterial = wechatClient.addDsltyyzDraft(dsltyyzDraftSend);
        return new CommonResponse<>(wechatMaterial);
    }

    @ApiOperation(value = "获取草稿")
    @GetMapping(value = "/article")
    public CommonResponse<WechatDraftDetailVO> getDraft(@RequestParam("mediaId") String mediaId) {
        return new CommonResponse<>(wechatClient.getDraft(mediaId));
    }

    @ApiOperation(value = "发布")
    @PostMapping(value = "/publish")
    public CommonResponse<WechatPublish> publish(@RequestParam("mediaId") String mediaId) {
        return new CommonResponse<>(wechatClient.addPublish(mediaId));
    }

    @ApiOperation(value = "获取发布状态")
    @GetMapping(value = "/publish")
    public CommonResponse<WechatPublish> getPublish(@RequestParam("publishId") String publishId) {
        return new CommonResponse<>(wechatClient.getPublish(publishId));
    }

    @ApiOperation(value = "删除发布")
    @DeleteMapping(value = "/publish")
    public CommonResponse<WechatResult> delPublish(@RequestParam("articleId") String articleId) {
        return new CommonResponse<>(wechatClient.delPublish(articleId, null));
    }

    @ApiOperation(value = "群发")
    @PostMapping(value = "/batchPublish")
    public CommonResponse<WechatMass> batchPublish(@RequestParam("mediaId") String mediaId) {
        return new CommonResponse<>(wechatClient.addBatchPublish(mediaId));
    }

    @ApiOperation(value = "获取群发状态")
    @GetMapping(value = "/message/mass")
    public CommonResponse<WechatMass> getMessageMass(@RequestParam("msgId") Integer msgId) {
        return new CommonResponse<>(wechatClient.getMessageMass(msgId));
    }

}
~~~