package com.dsltyyz.bundle.aliyun.client.sms;

import com.alibaba.alicloud.sms.ISmsService;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.dsltyyz.bundle.aliyun.common.properties.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * Description:
 * 阿里云短信客户端
 *
 * @author: dsltyyz
 * @since: 2020-08-27
 */
@Slf4j
public class AliyunSmsClient {

    private final String OK = "OK";

    @Resource
    private ISmsService smsService;

    @Resource
    private SmsProperties smsProperties;

    public AliyunSmsClient(){
        log.info("阿里云SMS客户端已加载");
    }

    /**
     * 短信发送
     *
     * @param telephone    手机号(多个以逗号隔开)
     * @param templateCode 短信模板码
     * @param jsonParams   短信模板参数
     * @param signName     签名
     * @return
     */
    @Async
    public void sendSms(String telephone, String templateCode, String jsonParams, String signName) {
        // 组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        // 必填:待发送手机号
        request.setPhoneNumbers(telephone);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        // 可选:模板中的变量替换JSON串
        request.setTemplateParam(jsonParams);
        SendSmsResponse sendSmsResponse;
        try {
            sendSmsResponse = smsService.sendSmsRequest(request);
            Assert.isTrue(null != sendSmsResponse.getCode() && OK.equals(sendSmsResponse.getCode()), "发送短信验证码失败");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.isTrue(false, e.getMessage());
        }
    }

    /**
     * 短信发送（默认签名）
     *
     * @param telephone    手机号(多个以逗号隔开)
     * @param templateCode 短信模板码
     * @param jsonParams   短信模板参数
     * @return
     */
    @Async
    public void sendSms(String telephone, String templateCode, String jsonParams) {
        sendSms(telephone, templateCode, jsonParams, smsProperties.getSignName());
    }

    /**
     * 短信发送（手动配置）
     *
     * @param telephone       手机号(多个以逗号隔开)
     * @param templateCode    短信模板码
     * @param jsonParams      短信模板参数
     * @param signName        签名
     * @param accessKeyId     阿里云访问keyId
     * @param accessKeySecret 阿里云访问keySecret
     */
    @Async
    public void sendSms(String telephone, String templateCode, String jsonParams, String signName, String accessKeyId, String accessKeySecret) {
        // 组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        // 必填:待发送手机号
        request.setPhoneNumbers(telephone);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        // 可选:模板中的变量替换JSON串
        request.setTemplateParam(jsonParams);
        SendSmsResponse sendSmsResponse;
        try {
            sendSmsResponse = smsService.sendSmsRequest(request, accessKeyId, accessKeySecret);
            Assert.isTrue(null != sendSmsResponse.getCode() && OK.equals(sendSmsResponse.getCode()), "发送短信验证码失败");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.isTrue(false, e.getMessage());
        }
    }
}
