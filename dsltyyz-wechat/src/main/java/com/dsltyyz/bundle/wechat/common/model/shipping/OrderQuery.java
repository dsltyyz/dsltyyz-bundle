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
public class OrderQuery implements Serializable {

    /**
     * 支付时间所属范围。
     */
    private PayTimeRange pay_time_range;

    /**
     * 订单状态枚举：(1) 待发货；(2) 已发货；(3) 确认收货；(4) 交易完成；(5) 已退款；(6) 资金待结算。
     */
    private Integer order_state;

    /**
     * 支付者openid。
     */
    private String openid;

    /**
     * 翻页时使用，获取第一页时不用传入，如果查询结果中 has_more 字段为 true，则传入该次查询结果中返回的 last_index 字段可获取下一页
     */
    private String last_index;

    /**
     * 翻页时使用，返回列表的长度，默认为100。
     */
    private Integer page_size;


}
