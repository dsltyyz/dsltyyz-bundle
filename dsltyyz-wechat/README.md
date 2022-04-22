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
    - enums ___枚举___
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
    version: v2 #默认
    cert-url: 版本为v2 微信支付平台证书URL
    api-v3-key: 版本为v3 apiV3秘钥
    mch-serial-no: 版本为v3 商户API证书的证书序列号
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

    @ApiOperation(value = "创建菜单")
    @PostMapping(value = "/menu")
    public CommonResponse addMenu() {
        //配置菜单
        WechatMenu wechatMenu = new WechatMenu();
        //一级 百度
        WechatButtonMenu menu1 = new WechatButtonMenu();
        menu1.setName("百度");
        menu1.setType(WechatButtonType.VIEW.getValue());
        menu1.setUrl("https://www.baidu.com/");

        //一级 代码平台
        WechatButtonMenu menu2 = new WechatButtonMenu();
        menu2.setName("代码平台");
        //二级GITHUB
        WechatButtonMenu menu21 = new WechatButtonMenu();
        menu21.setName("GITHUB");
        menu21.setType(WechatButtonType.VIEW.getValue());
        menu21.setUrl("https://github.com/");
        //二级GITEE
        WechatButtonMenu menu22 = new WechatButtonMenu();
        menu22.setName("GITEE");
        menu22.setType(WechatButtonType.VIEW.getValue());
        menu22.setUrl("https://gitee.com/");
        menu2.setSub_button(Arrays.asList(menu21, menu22));

        wechatMenu.setButton(Arrays.asList(menu1, menu2));
        wechatClient.addMenu(wechatMenu);
        return new CommonResponse();
    }

    @ApiOperation(value = "删除菜单")
    @DeleteMapping(value = "/menu")
    public CommonResponse delMenu() {
        wechatClient.delMenu();
        return new CommonResponse();
    }

    @ApiOperation(value = "创建草稿")
    @PostMapping(value = "/article")
    public CommonResponse<WechatMaterial> createArticle() {
        DsltyyzDraftSend dsltyyzDraftSend = new DsltyyzDraftSend();
        dsltyyzDraftSend.setLogo("LOGO URL");
        dsltyyzDraftSend.setTitle("文章标题");
        dsltyyzDraftSend.setDigest("文章简介");
        dsltyyzDraftSend.setContent("html代码 ps:自动替换src资源为素材");
        dsltyyzDraftSend.setContent_source_url("原文路径");
        dsltyyzDraftSend.setAuthor("auther");
        dsltyyzDraftSend.setNeed_open_comment(0);
        dsltyyzDraftSend.setOnly_fans_can_comment(0);
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

    @ApiOperation(value = "二维码支付")
    @PostMapping(value = "/pay/qrcode")
    public CommonResponse<String> createPayQrcode(HttpServletResponse response) throws IOException {
        WechatPayOrder wechatPayOrder = new WechatPayOrder();
        String orderId  ="dsltyyz_"+ DateUtils.format(new Date(),"yyyyMMddHHmmss");
        wechatPayOrder.setOrderId(orderId);
        wechatPayOrder.setFee("1");
        //本机外网IP 且在微信公众号IP白名单内
        wechatPayOrder.setIp("外网IP");
        wechatPayOrder.setTitle("支付测试0.01元");
        wechatPayOrder.setNotifyUrl("回调URL");
        String s = wechatClient.unifiedOrderByNative(wechatPayOrder);
        System.out.println(s);
        String key = "pay/"+orderId+"."+ ImageType.JPG;
        aliyunOssClient.putObject(key, QRCodeUtils.encode(s));
        return new CommonResponse<>(aliyunOssClient.getResourceUrl(key));
    }

    @ApiOperation(value = "jsapi支付")
    @PostMapping(value = "/pay/jsapi")
    public CommonResponse<Map<String, String>> createPayJsapi(HttpServletResponse response) throws IOException {
        WechatPayOrder wechatPayOrder = new WechatPayOrder();
        String orderId  ="dsltyyz_"+ DateUtils.format(new Date(),"yyyyMMddHHmmss");
        wechatPayOrder.setOrderId(orderId);
        wechatPayOrder.setFee("1");
        //本机外网IP 且在微信公众号IP白名单内
        wechatPayOrder.setIp("外网IP");
        //发起用户openid
        wechatPayOrder.setOpenid("openid");
        wechatPayOrder.setTitle("支付测试0.01元");
        wechatPayOrder.setNotifyUrl("回调URL");
        return new CommonResponse<>(wechatClient.unifiedOrderByJsApi(wechatPayOrder));
    }
    
    @ApiOperation(value = "微信支付回调")
    @PostMapping(value = "/v2/pay/callback")
    public String payCallback(HttpServletRequest request) throws Exception {
        String msg = StreamUtils.inputStreamToString(request.getInputStream());
        System.out.println(msg);
        if(!WXPayUtil.isSignatureValid(msg, wechatProperties.getPay().getMchPrivateKey())){
            return "验证回调信息失败";
        }
        JSONObject jsonObject = XmlUtils.xmlToJSONObject(msg);
        System.out.println(jsonObject.toJSONString());
        WechatPayV2Result v2Result = jsonObject.toJavaObject(WechatPayV2Result.class);
        //TODO 执行业务
        Map<String,String> result = new HashMap<>();
        result.put("return_code","SUCCESS");
        result.put("return_msg","OK");
        return WXPayUtil.mapToXml(result);
    }

    @ApiOperation(value = "【未测试】微信退款回调")
    @PostMapping(value = "/v2/refund/callback")
    public String refundCallback(HttpServletRequest request) throws Exception {
         String msg = StreamUtils.inputStreamToString(request.getInputStream());
            System.out.println(msg);
            JSONObject jsonObject = XmlUtils.xmlToJSONObject(msg);
            System.out.println(jsonObject.toJSONString());
            WechatRefundEncryptV2Result encryptV2Result = jsonObject.toJavaObject(WechatRefundEncryptV2Result.class);
            String decrypt = WechatCommonUtils.decrypt(encryptV2Result.getReq_info(), wechatProperties.getPay().getMchPrivateKey());
            if(!WXPayUtil.isSignatureValid(decrypt, wechatProperties.getPay().getMchPrivateKey())){
                return "验证回调信息失败";
            }
            JSONObject decryptJsonObject = XmlUtils.xmlToJSONObject(decrypt);
            System.out.println(decryptJsonObject.toJSONString());
            WechatRefundV2Result v2Result = decryptJsonObject.toJavaObject(WechatRefundV2Result.class);
            //TODO 执行业务
            Map<String,String> result = new HashMap<>();
            result.put("return_code","SUCCESS");
            result.put("return_msg","OK");
            return WXPayUtil.mapToXml(result);
    }

}
~~~