package com.dsltyyz.bundle.wechat.common.model.shipping;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 * 订单，需要上传物流信息的订单
 *
 * @author: dsltyyz
 * @date: 2026-3-5
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SpecialOrder implements Serializable {

    /**
     * 特殊发货报备类型，1为预售商品订单，2为测试订单
     */
    private Integer type;

    /**
     * 需要特殊发货报备的订单号，可传入微信支付单号或商户单号
     */
    private String order_id;

    /**
     * 预计发货时间的unix时间戳，type为1时必填，type为2可省略
     */
    private Integer delay_to;



}
