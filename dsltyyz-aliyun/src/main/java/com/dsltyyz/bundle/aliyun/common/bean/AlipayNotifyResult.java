package com.dsltyyz.bundle.aliyun.common.bean;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付宝支付回调结果VO
 * @author dsltyyz
 * @date 2022-4-28
 */
@Data
@Accessors(chain = true)
@ApiModel("支付宝支付回调结果VO")
public class AlipayNotifyResult implements Serializable {
    @JSONField(name = "notify_time")
    @ApiModelProperty("通知的发送时间。格式为 yyyy-MM-dd HH:mm:ss")
    private Date notifyTime;
    @JSONField(name = "notify_type")
    @ApiModelProperty("通知的类型")
    private String notifyType;
    @JSONField(name = "notify_id")
    @ApiModelProperty("通知校验ID")
    private String notifyId;

    @JSONField(name = "app_id")
    @ApiModelProperty("支付宝分配给开发者的应用ID")
    private String appId;
    @ApiModelProperty("编码格式")
    private String charset;
    @ApiModelProperty("调用的接口版本")
    private String version;
    @JSONField(name = "sign_type")
    @ApiModelProperty("商户生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA")
    private String signType;
    @ApiModelProperty("签名")
    private String sign;

    @JSONField(name = "trade_no")
    @ApiModelProperty("支付宝交易凭证号")
    private String tradeNo;
    @JSONField(name = "out_trade_no")
    @ApiModelProperty("原支付请求的商户订单号")
    private String outTradeNo;
    @JSONField(name = "out_biz_no")
    @ApiModelProperty("商户业务 ID，主要是退款通知中返回退款申请的流水号")
    private String outBizNo;

    @JSONField(name = "buyer_id")
    @ApiModelProperty("买家支付宝账号对应的支付宝唯一用户号。以 2088 开头的纯 16 位数字。")
    private String buyerId;
    @JSONField(name = "buyer_logon_id")
    @ApiModelProperty("买家支付宝账号")
    private String buyerLogonId;
    @JSONField(name = "seller_id")
    @ApiModelProperty("卖家支付宝用户号")
    private String sellerId;
    @JSONField(name = "seller_email")
    @ApiModelProperty("卖家支付宝账号")
    private String sellerEmail;

    @JSONField(name = "trade_status")
    @ApiModelProperty("交易目前所处的状态： WAIT_BUYER_PAY创建交易，等待买家付款 TRADE_CLOSED未付款交易超时关闭 TRADE_SUCCESS交易支付成功 TRADE_FINISHED交易结束，不可退款")
    private String tradeStatus;

    @JSONField(name = "total_amount")
    @ApiModelProperty("本次交易支付的订单金额，单位为人民币（元）")
    private BigDecimal totalAmount;
    @JSONField(name = "receipt_amount")
    @ApiModelProperty("商家在收益中实际收到的款项，单位人民币（元）")
    private BigDecimal receiptAmount;
    @JSONField(name = "invoice_amount")
    @ApiModelProperty("用户在交易中支付的可开发票的金额")
    private BigDecimal invoiceAmount;
    @JSONField(name = "buyer_pay_amount")
    @ApiModelProperty("用户在交易中支付的金额")
    private BigDecimal buyerPayAmount;
    @JSONField(name = "point_amount")
    @ApiModelProperty("使用集分宝支付的金额")
    private BigDecimal pointAmount;
    @JSONField(name = "refund_fee")
    @ApiModelProperty("退款通知中，返回总退款金额，单位为人民币（元），支持两位小数")
    private BigDecimal refundFee;

    @ApiModelProperty("商品的标题/交易标题/订单标题/订单关键字等，是请求时对应的参数，原样通知回来")
    private String subject;
    @ApiModelProperty("订单的备注、描述、明细等。对应请求时的 body 参数，原样通知回来。")
    private String body;

    @JSONField(name = "gmt_create")
    @ApiModelProperty("该笔交易创建的时间。格式为 yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JSONField(name = "gmt_payment")
    @ApiModelProperty("该笔交易的买家付款时间。格式为 yyyy-MM-dd HH:mm:ss")
    private Date gmtPayment;
    @JSONField(name = "gmt_refund")
    @ApiModelProperty("该笔交易的退款时间。格式 yyyy-MM-dd HH:mm:ss.S")
    private Date gmtRefund;
    @JSONField(name = "gmt_close")
    @ApiModelProperty("该笔交易结束时间。格式为 yyyy-MM-dd HH:mm:ss")
    private Date gmtClose;

    @JSONField(name = "fund_bill_list")
    @ApiModelProperty("支付成功的各个渠道金额信息")
    private String fundBillList;
    @JSONField(name = "passback_params")
    @ApiModelProperty("公共回传参数，如果请求时传递了该参数，则返回给商户时会在异步通知时将该参数原样返回")
    private String passbackParams;
    @JSONField(name = "voucherDetailList")
    @ApiModelProperty("本交易支付时所有优惠券信息")
    private String voucherDetailList;
}