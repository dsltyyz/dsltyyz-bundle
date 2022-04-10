package com.dsltyyz.bundle.aliyun.client.pay;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.dsltyyz.bundle.aliyun.common.bean.AlipayOrder;
import com.dsltyyz.bundle.aliyun.common.properties.PayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Description:
 * 阿里云Pay客户端
 *
 * @author: dsltyyz
 * @date: 2020-08-27
 */
@Slf4j
public class AliyunPayClient {

    private AlipayClient alipayClient;

    @Resource
    private PayProperties payProperties;

    public AliyunPayClient() {
        log.info("阿里云Pay客户端已加载");
    }

    @PostConstruct
    public void init() throws AlipayApiException {
        CertAlipayRequest certParams = new CertAlipayRequest();
        BeanUtils.copyProperties(payProperties, certParams);
        alipayClient = new DefaultAlipayClient(certParams);
    }

    /**
     * 创建页面订单
     *
     * @param alipayOrder 订单
     * @param returnUrl   同步通知页面路径
     * @param notifyUrl   服务器异步通知路径
     * @return
     * @throws AlipayApiException
     */
    public String createPagePay(AlipayOrder alipayOrder, String returnUrl, String notifyUrl) throws AlipayApiException {
        // 2、设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        // 页面跳转同步通知页面路径
        alipayRequest.setReturnUrl(returnUrl);
        // 服务器异步通知路径
        alipayRequest.setNotifyUrl(notifyUrl);
        // 封装参数
        alipayRequest.setBizContent(JSON.toJSONString(alipayOrder));
        // 3、请求支付宝进行付款，并获取支付结果
        return alipayClient.pageExecute(alipayRequest).getBody();
    }
}
