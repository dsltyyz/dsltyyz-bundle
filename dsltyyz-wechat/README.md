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
#### 2.1.1 传送门
- [微信公众平台](https://mp.weixin.qq.com/)
- [微信OCR文档](https://developers.weixin.qq.com/doc/offiaccount/Intelligent_Interface/OCR.html)
- [微信OCR服务](https://fuwu.weixin.qq.com/service/detail/000ce4cec24ca026d37900ed551415)
- [微信支付v2文档](https://pay.weixin.qq.com/wiki/doc/api/index.html)
- [微信支付v3文档](https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/pages/index.shtml)
#### 2.1.2 配置说明 application.yml
~~~
wechat:
  oauth:
    app-id: 公众号ID
    app-secret: 公众号秘钥
    token: 令牌
    encoding-type: 消息加解密方式
    encoding-aes-key: 消息加解密密钥
    data-type: XML #默认XML 开放平台及小程序支持JSON
  pay:
    mch-id: 商户ID
    version: v2 #默认
    #v2
    mch-private-key: 版本为v2 商户apiV2秘钥
    #支持三种路径 1.远程URL(http/https) 2.项目resources路径(classpath:) 3.系统绝对路径
    cert-url: 版本为v2 微信支付平台api证书路径 #apiclient_cert.p12
    #v3
    api-v3-key: 版本为v3 apiV3秘钥
    mch-serial-no: 版本为v3 商户api证书p12序列号
    #支持三种路径 1.远程URL(http/https) 2.项目resources路径(classpath:) 3.系统绝对路径
    mch-private-key-cert: 版本为v3 商户api证书私钥路径 #apiclient_key.pem
~~~
## 3 示例
### 3.1 服务号
~~~
@Api(value = "微信服务号controller", tags = {"微信服务号"})
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

    @ApiOperation(value = "获取模板列表")
    @GetMapping(value = "/template")
    public CommonResponse<List<WechatTemplate>> getAllPrivateTemplate() {
        return new CommonResponse<>(wechatClient.getAllPrivateTemplate());
    }

    @ApiOperation(value = "发送模板消息")
    @PostMapping(value = "/template")
    public CommonResponse sendTemplate() {
        WechatTemplateSend wechatTemplateSend = new WechatTemplateSend();
        wechatTemplateSend.setTouser("用户openid");
        wechatTemplateSend.setTemplate_id("模板id");
        wechatTemplateSend.setUrl("点击通知跳转url");

        Map<String, WechatDataValue> dataMap = new HashMap<>();
        dataMap.put("first", new WechatDataValue("您预约的活动结果通知如下:"));
        dataMap.put("activity_name", new WechatDataValue("活动名称"));
        dataMap.put("reserve_results", new WechatDataValue("活动状态"));
        dataMap.put("activity_time", new WechatDataValue(DateUtils.format(new Date(), "yyyy-MM-dd")));
        dataMap.put("activity_address", new WechatDataValue("活动地址"));
        dataMap.put("remark", new WechatDataValue("感谢你的参与！"));
        wechatTemplateSend.setData(dataMap);
        wechatClient.sendTemplate(wechatTemplateSend);
        return new CommonResponse<>();
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
        dsltyyzDraftSend.setLogo("LOGO URL ps:自动替换资源url为素材");
        dsltyyzDraftSend.setTitle("文章标题");
        dsltyyzDraftSend.setDigest("文章简介");
        dsltyyzDraftSend.setContent("html代码 ps:自动替换src资源url为素材");
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

}
~~~
### 3.2 小程序
~~~
@Api(value = "微信小程序controller", tags = {"微信小程序"})
@RestController
@RequestMapping("wechat")
public class WechatController {

    @Resource
    private WechatProperties wechatProperties;

    @Resource
    private WechatClient wechatClient;

    @Resource
    private AliyunOssClient aliyunOssClient;

    @ApiOperation(value = "微信请求GET回调")
    @GetMapping("/check")
    public String check(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp,
                        @RequestParam("nonce") String nonce, @RequestParam("echostr") String echostr) {
        return WechatCommonUtils.callbackCheck(signature, timestamp, nonce, echostr, wechatProperties.getOauth().getToken());
    }

    @ApiOperation(value = "微信消息回调")
    @PostMapping("/check")
    public String check(HttpServletRequest request) throws Exception {
        System.out.println("------------request parameter参数-------------");
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
            String s = StreamUtils.inputStreamToString(request.getInputStream());
            System.out.println("------------inputStream内容 aes--------------");
            System.out.println(s);
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(wechatProperties.getOauth().getAppId(), wechatProperties.getOauth().getToken(), wechatProperties.getOauth().getEncodingAesKey());
            String msgSignature = request.getParameter("msg_signature");
            if(WechatOauthDataType.XML.equals(wechatProperties.getOauth().getDataType())) {
                //XML 直接解密消息
                msg = wxBizMsgCrypt.decryptMsg(msgSignature, timestamp, nonce, s);
            }else{
                //JSON 直接解密加密消息
                msg = wxBizMsgCrypt.decrypt(JSONObject.parseObject(s).getString("Encrypt"));
            }
        } else {
            return "不可识别的加密类型";
        }
        System.out.println("------------inputStream内容--------------");
        System.out.println(msg);
        WechatMessage wechatMessage;
        if(WechatOauthDataType.XML.equals(wechatProperties.getOauth().getDataType())){
            //数据格式为XML
            System.out.println("------------xml转json数据-------------");
            System.out.println(XmlUtils.xmlToJSONObject(msg).toJSONString());
            wechatMessage = XmlUtils.xmlToJSONObject(msg).toJavaObject(WechatMessage.class);
        }else{
            //数据格式为JSON
            wechatMessage = JSONObject.parseObject(msg,WechatMessage.class);
        }
        System.out.println("------------json转对象数据-------------");
        System.out.println(JSONObject.toJSONString(wechatMessage));
        return "success";
    }

    @ApiOperation(value = "获取用户登录信息")
    @GetMapping("/user/login/{code}")
    public CommonResponse<WechatMiniOpenId> login(@PathVariable("code") String code) {
        return new CommonResponse<>(wechatClient.getMiniOpenId(code));
    }

    @ApiOperation(value = "获取用户手机信息")
    @GetMapping("/user/telephone/{code}")
    public CommonResponse<WechatPhoneInfo> getUserPhoneInfo(@PathVariable("code") String code) {
        return new CommonResponse<>(wechatClient.getUserPhoneInfo(code));
    }

    @ApiOperation(value = "获取订阅消息模板列表")
    @GetMapping("/template")
    public CommonResponse<List<WechatMiniTemplate>> getNewTmplList() {
        return new CommonResponse<>(wechatClient.getNewTmplList());
    }

    @ApiOperation(value = "发送订阅消息")
    @PostMapping("/template")
    public CommonResponse getUserPhoneInfo() {
        WechatMiniTemplateSend wechatMiniTemplateSend = new WechatMiniTemplateSend();
        wechatMiniTemplateSend.setTouser("用户OPENID");
        wechatMiniTemplateSend.setPage("跳转页面");
        wechatMiniTemplateSend.setTemplate_id("订阅模板ID");

        Map<String, WechatDataValue> dataMap = new HashMap<>();
        //参数配置
        wechatMiniTemplateSend.setData(dataMap);
        wechatClient.sendNewTmpl(wechatMiniTemplateSend);
        return new CommonResponse<>();
    }

}
~~~
### 3.3 微信网站应用
~~~
@Api(value = "微信网站应用controller", tags = {"微信网站应用"})
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
        System.out.println("------------request parameter参数-------------");
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
            String s = StreamUtils.inputStreamToString(request.getInputStream());
            System.out.println("------------inputStream内容 aes--------------");
            System.out.println(s);
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(wechatProperties.getOauth().getAppId(), wechatProperties.getOauth().getToken(), wechatProperties.getOauth().getEncodingAesKey());
            if(WechatOauthDataType.XML.equals(wechatProperties.getOauth().getDataType())) {
                String msgSignature = request.getParameter("msg_signature");
                //XML 直接解密消息
                msg = wxBizMsgCrypt.decryptMsg(msgSignature, timestamp, nonce, s);
            }else{
                //JSON 直接解密加密消息
                msg = wxBizMsgCrypt.decrypt(JSONObject.parseObject(s).getString("Encrypt"));
            }
        } else {
            return "不可识别的加密类型";
        }
        System.out.println("------------inputStream内容--------------");
        System.out.println(msg);
        WechatMessage wechatMessage;
        if (WechatOauthDataType.XML.equals(wechatProperties.getOauth().getDataType())) {
            //数据格式为XML
            System.out.println("------------xml转json数据-------------");
            System.out.println(XmlUtils.xmlToJSONObject(msg).toJSONString());
            wechatMessage = XmlUtils.xmlToJSONObject(msg).toJavaObject(WechatMessage.class);
        } else {
            //数据格式为JSON
            wechatMessage = JSONObject.parseObject(msg, WechatMessage.class);
        }
        System.out.println("------------json转对象数据-------------");
        System.out.println(JSONObject.toJSONString(wechatMessage));
        return "success";
    }

    @ApiOperation(value = "获取用户登录信息")
    @GetMapping("/user/login/{code}")
    public CommonResponse<WechatUser> login(@PathVariable("code") String code) {
        return new CommonResponse<>(wechatClient.getUserInfoByCode(code));
    }

}
~~~
### 3.4 OCR
~~~
@Api(value = "微信OCRcontroller", tags = {"微信OCR"})
@RestController
@RequestMapping("wechat/ocr")
public class WechatOcrController {
   
    @Resource
    private WechatClient wechatClient;

    @ApiOperation(value = "获取身份证信息")
    @PostMapping("/idcard")
    public CommonResponse<WechatIdcardResult> getIdCard(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        FileUtils.inputStreamToFile(multipartFile.getInputStream(), file);
        WechatIdcardResult photo = wechatClient.getIdcard("photo", null, file);
        file.delete();
        return new CommonResponse<>(photo);
    }

    @ApiOperation(value = "获取银行卡信息")
    @PostMapping("/bankcard")
    public CommonResponse<WechatBankcardResult> getBandCard(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        FileUtils.inputStreamToFile(multipartFile.getInputStream(), file);
        WechatBankcardResult photo = wechatClient.getBankcard("photo", null, file);
        file.delete();
        return new CommonResponse<>(photo);
    }

    @ApiOperation(value = "获取营业执照")
    @PostMapping("/businessLicense")
    public CommonResponse<WechatBizlicenseResult> getBizLicense(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        FileUtils.inputStreamToFile(multipartFile.getInputStream(), file);
        WechatBizlicenseResult photo = wechatClient.getBizLicense(null, file);
        file.delete();
        return new CommonResponse<>(photo);
    }
}
~~~
### 3.5 微信支付v2
~~~
@Api(value = "微信支付v2controller", tags = {"微信支付v2"})
@RestController
@RequestMapping("wechat/v2/pay")
public class WechatPayV2Controller {

    @Resource
    private WechatProperties wechatProperties;

    @Resource
    private WechatClient wechatClient;

    @Resource
    private AliyunOssClient aliyunOssClient;

    @ApiOperation(value = "二维码支付")
    @PostMapping(value = "qrcode")
    public CommonResponse<String> createPayQrcode(){
        WechatPayOrder wechatPayOrder = new WechatPayOrder();
        String orderId = "dsltyyz_" + DateUtils.format(new Date(), "yyyyMMddHHmmss");
        wechatPayOrder.setOrderId(orderId);
        wechatPayOrder.setFee("1");
        wechatPayOrder.setIp("发起请求外网IP");
        wechatPayOrder.setTitle("支付测试0.01元");
        wechatPayOrder.setNotifyUrl("支付通知回调URL /wechat/v2/pay/callback");
        String s = wechatClient.unifiedOrderByNative(wechatPayOrder);
        System.out.println(s);
        //将微信支付链接转为二维码
        String key = "pay/" + orderId + "." + ImageType.JPG;
        aliyunOssClient.putObject(key, QRCodeUtils.encode(s));
        return new CommonResponse<>(aliyunOssClient.getResourceUrl(key));
    }

    @ApiOperation(value = "[未测试]H5支付")
    @PostMapping(value = "h5")
    public CommonResponse<String> createPayH5(){
        WechatPayOrder wechatPayOrder = new WechatPayOrder();
        String orderId = "dsltyyz_" + DateUtils.format(new Date(), "yyyyMMddHHmmss");
        wechatPayOrder.setOrderId(orderId);
        wechatPayOrder.setFee("1");
        wechatPayOrder.setIp("发起请求外网IP");
        wechatPayOrder.setTitle("支付测试0.01元");
        wechatPayOrder.setNotifyUrl("支付通知回调URL /wechat/v2/pay/callback");
        return new CommonResponse<>(wechatClient.unifiedOrderByH5(wechatPayOrder));
    }

    @ApiOperation(value = "jsapi支付")
    @PostMapping(value = "jsapi")
    public CommonResponse<Map<String, String>> createPayJsapi(){
        WechatPayOrder wechatPayOrder = new WechatPayOrder();
        String orderId = "dsltyyz_" + DateUtils.format(new Date(), "yyyyMMddHHmmss");
        wechatPayOrder.setOrderId(orderId);
        wechatPayOrder.setFee("1");
        wechatPayOrder.setIp("发起请求外网IP");
        wechatPayOrder.setOpenid("发起用户OPENID");
        wechatPayOrder.setTitle("支付测试0.01元");
        wechatPayOrder.setNotifyUrl("支付通知回调URL /wechat/v2/pay/callback");
        return new CommonResponse<>(wechatClient.unifiedOrderByJsApi(wechatPayOrder));
    }

    @ApiOperation(value = "[未测试]app支付")
    @PostMapping(value = "app")
    public CommonResponse<Map<String, String>> createPayApp() {
        WechatPayOrder wechatPayOrder = new WechatPayOrder();
        String orderId = "dsltyyz_" + DateUtils.format(new Date(), "yyyyMMddHHmmss");
        wechatPayOrder.setOrderId(orderId);
        wechatPayOrder.setFee("1");
        wechatPayOrder.setIp("发起请求外网IP");
        wechatPayOrder.setTitle("支付测试0.01元");
        wechatPayOrder.setNotifyUrl("支付通知回调URL /wechat/v2/pay/callback");
        return new CommonResponse<>(wechatClient.unifiedOrderByApp(wechatPayOrder));
    }

    @ApiOperation(value = "微信支付回调")
    @PostMapping(value = "callback")
    public String payCallback(HttpServletRequest request) throws Exception {
        System.out.println("------------request parameter参数-------------");
        System.out.println(JSONObject.toJSONString(request.getParameterMap()));
        String msg = StreamUtils.inputStreamToString(request.getInputStream());
        System.out.println("------------xml转json数据-------------");
        System.out.println(XmlUtils.xmlToJSONObject(msg).toJSONString());
        if (!WXPayUtil.isSignatureValid(msg, wechatProperties.getPay().getMchPrivateKey())) {
            return "验证回调信息失败";
        }
        JSONObject jsonObject = XmlUtils.xmlToJSONObject(msg);
        System.out.println(jsonObject.toJSONString());
        WechatPayV2Result v2Result = jsonObject.toJavaObject(WechatPayV2Result.class);
        System.out.println("------------json转对象数据-------------");
        System.out.println(JSONObject.toJSONString(v2Result));
        //TODO 执行业务
        Map<String, String> result = new HashMap<>();
        result.put("return_code", "SUCCESS");
        result.put("return_msg", "OK");
        return WXPayUtil.mapToXml(result);
    }

    @ApiOperation(value = "订单查询")
    @PostMapping(value = "info")
    public CommonResponse<JSONObject> getOrder(@RequestParam("outTradeNo") String outTradeNo) {
        JSONObject o = wechatClient.getUnifiedOrderByOutTradeNo(outTradeNo);
        return new CommonResponse<>(o);
    }

    @ApiOperation(value = "退款")
    @PostMapping(value = "refund")
    public CommonResponse refund(@RequestParam("outTradeNo") String outTradeNo, @RequestParam("totalFee") String totalFee) {
        JSONObject jsonObject = wechatClient.applyRefundByOutTradeNo(outTradeNo, totalFee, "退款通知回调URL /wechat/v2/pay/refund/callback");
        System.out.println(jsonObject.toJSONString());
        return new CommonResponse();
    }

    @ApiOperation(value = "微信退款回调")
    @PostMapping(value = "refund/callback")
    public String refundCallback(HttpServletRequest request) throws Exception {
        String msg = StreamUtils.inputStreamToString(request.getInputStream());
        System.out.println("------------InputStream加密数据-------------");
        System.out.println(msg);
        JSONObject jsonObject = XmlUtils.xmlToJSONObject(msg);
        System.out.println("------------xml转json加密数据-------------");
        System.out.println(jsonObject.toJSONString());
        WechatRefundEncryptV2Result encryptV2Result = jsonObject.toJavaObject(WechatRefundEncryptV2Result.class);
        System.out.println("------------json转对象加密数据-------------");
        System.out.println(jsonObject.toJSONString());
        String decrypt = WechatCommonUtils.decrypt(encryptV2Result.getReq_info(), wechatProperties.getPay().getMchPrivateKey());
        System.out.println("------------退款详细解密数据-------------");
        System.out.println(decrypt);
        JSONObject decryptJsonObject = XmlUtils.xmlToJSONObject(decrypt);
        System.out.println("------------xml转json解密数据-------------");
        System.out.println(decryptJsonObject.toJSONString());
        WechatRefundV2Detail detail = decryptJsonObject.toJavaObject(WechatRefundV2Detail.class);
        System.out.println("------------json转对象解密数据-------------");
        System.out.println(JSONObject.toJSONString(detail));
        //TODO 执行业务
        Map<String, String> result = new HashMap<>();
        result.put("return_code", "SUCCESS");
        result.put("return_msg", "OK");
        return WXPayUtil.mapToXml(result);
    }

}
~~~
### 3.6 微信支付v3
~~~
@Api(value = "微信支付v3controller", tags = {"微信支付v3"})
@RestController
@RequestMapping("wechat/v3/pay")
public class WechatPayV3Controller {

    @Resource
    private WechatClient wechatClient;

    @Resource
    private AliyunOssClient aliyunOssClient;

    @ApiOperation(value = "二维码支付")
    @PostMapping(value = "qrcode")
    public CommonResponse<String> createPayQrcode() {
        WechatPayOrder wechatPayOrder = new WechatPayOrder();
        String orderId = "dsltyyz_" + DateUtils.format(new Date(), "yyyyMMddHHmmss");
        wechatPayOrder.setOrderId(orderId);
        wechatPayOrder.setFee("1");
        wechatPayOrder.setIp("发起请求外网IP");
        wechatPayOrder.setTitle("支付测试0.01元");
        wechatPayOrder.setNotifyUrl("支付通知回调URL /wechat/v3/pay/callback");
        String s = wechatClient.unifiedOrderByNative(wechatPayOrder);
        System.out.println(s);
        //将微信支付链接转为二维码
        String key = "pay/" + orderId + "." + ImageType.JPG;
        aliyunOssClient.putObject(key, QRCodeUtils.encode(s));
        return new CommonResponse<>(aliyunOssClient.getResourceUrl(key));
    }

    @ApiOperation(value = "[未测试]H5支付")
    @PostMapping(value = "h5")
    public CommonResponse<String> createPayH5() {
        WechatPayOrder wechatPayOrder = new WechatPayOrder();
        String orderId = "dsltyyz_" + DateUtils.format(new Date(), "yyyyMMddHHmmss");
        wechatPayOrder.setOrderId(orderId);
        wechatPayOrder.setFee("1");
        wechatPayOrder.setIp("发起请求外网IP");
        wechatPayOrder.setTitle("支付测试0.01元");
        wechatPayOrder.setNotifyUrl("支付通知回调URL /wechat/v3/pay/callback");
        return new CommonResponse<>(wechatClient.unifiedOrderByH5(wechatPayOrder));
    }

    @ApiOperation(value = "jsapi支付")
    @PostMapping(value = "jsapi")
    public CommonResponse<Map<String, String>> createPayJsapi(HttpServletResponse response) throws IOException {
        WechatPayOrder wechatPayOrder = new WechatPayOrder();
        String orderId = "dsltyyz_" + DateUtils.format(new Date(), "yyyyMMddHHmmss");
        wechatPayOrder.setOrderId(orderId);
        wechatPayOrder.setFee("1");
        wechatPayOrder.setIp("发起请求外网IP");
        wechatPayOrder.setOpenid("用户OPENID");
        wechatPayOrder.setTitle("支付测试0.01元");
        wechatPayOrder.setNotifyUrl("支付通知回调URL /wechat/v3/pay/callback");
        return new CommonResponse<>(wechatClient.unifiedOrderByJsApi(wechatPayOrder));
    }

    @ApiOperation(value = "[未测试]app支付")
    @PostMapping(value = "app")
    public CommonResponse<Map<String, String>> createPayApp() {
        WechatPayOrder wechatPayOrder = new WechatPayOrder();
        String orderId = "dsltyyz_" + DateUtils.format(new Date(), "yyyyMMddHHmmss");
        wechatPayOrder.setOrderId(orderId);
        wechatPayOrder.setFee("1");
        wechatPayOrder.setIp("发起请求外网IP");
        wechatPayOrder.setTitle("支付测试0.01元");
        wechatPayOrder.setNotifyUrl("支付通知回调URL /wechat/v3/pay/callback");
        return new CommonResponse<>(wechatClient.unifiedOrderByApp(wechatPayOrder));
    }

    @ApiOperation(value = "微信支付回调")
    @PostMapping(value = "callback")
    public Map<String, String> payCallbackV3(@RequestBody JSONObject object) {
        System.out.println(object.toJSONString());
        JSONObject resource = object.getJSONObject("resource");
        System.out.println("------------加密内容-------------");
        System.out.println(resource.toJSONString());
        String text = wechatClient.decryptToStringV3(resource.getString("associated_data"), resource.getString("nonce"), resource.getString("ciphertext"));
        System.out.println("------------解密内容-------------");
        System.out.println(text);
        Map<String, String> result = new HashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "成功");
        return result;
    }

    @ApiOperation(value = "订单查询")
    @PostMapping(value = "info")
    public CommonResponse<JSONObject> getOrder(@RequestParam("outTradeNo") String outTradeNo) {
        JSONObject o = wechatClient.getUnifiedOrderByOutTradeNo(outTradeNo);
        return new CommonResponse<>(o);
    }

    @ApiOperation(value = "退款")
    @PostMapping(value = "refund")
    public CommonResponse refund(@RequestParam("outTradeNo") String outTradeNo, @RequestParam("totalFee") String totalFee) {
        JSONObject jsonObject = wechatClient.applyRefundByOutTradeNo(outTradeNo, totalFee, "退款通知回调URL /wechat/v3/pay/refund/callback");
        System.out.println(jsonObject.toJSONString());
        return new CommonResponse();
    }

    @ApiOperation(value = "微信退款回调")
    @PostMapping(value = "refund/callback")
    public Map<String, String> refundCallbackV3(@RequestBody JSONObject object) {
        System.out.println(object.toJSONString());
        JSONObject resource = object.getJSONObject("resource");
        System.out.println("------------加密内容-------------");
        System.out.println(resource.toJSONString());
        String text = wechatClient.decryptToStringV3(resource.getString("associated_data"), resource.getString("nonce"), resource.getString("ciphertext"));
        System.out.println("------------解密内容-------------");
        System.out.println(text);
        Map<String, String> result = new HashMap<>();
        result.put("code", "SUCCESS");
        result.put("message", "成功");
        return result;
    }

}
~~~
