package com.dsltyyz.bundle.wechat.common.model.shipping;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 上传物流信息
 *
 * @author: dsltyyz
 * @date: 2026-3-5
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UploadShipping implements Serializable {

    /**
     * 订单，需要上传物流信息的订单
     */
    private OrderKey order_key;

    /**
     * 物流模式，发货方式枚举值：
     * 1、实体物流配送采用快递公司进行实体物流配送形式  WechatLogisticsType.EXPRESS_DELIVERY
     * 2、同城配送 WechatLogisticsType.SAME_CITY_DELIVERY
     * 3、虚拟商品，虚拟商品，例如话费充值，点卡等，无实体配送形式 WechatLogisticsType.VIRTUAL_PRODUCT
     * 4、用户自提 WechatLogisticsType.SELF_PICKUP
     */
    private Integer logistics_type;

    /**
     * 发货模式，发货模式枚举值：
     * 1、统一发货 WechatDeliveryMode.UNIFIED_DELIVERY
     * 2、分拆发货 WechatDeliveryMode.SPLIT_DELIVERY
     */
    private Integer delivery_mode;

    /**
     * 分拆发货模式时必填，用于标识分拆发货模式下是否已全部发货完成，只有全部发货完成的情况下才会向用户推送发货完成通知。示例值: true/false
     */
    private Boolean is_all_delivered;

    /**
     * 物流信息列表，发货物流单列表，支持统一发货（单个物流单）和分拆发货（多个物流单）两种模式，多重性: [1, 15]
     */
    private List<Shipping> shipping_list;

    /**
     * 上传时间，用于标识请求的先后顺序
     * 示例值: `2022-12-15T13:29:35.120+08:00`
     * "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
     */
    private String upload_time;

    /**
     * 支付者，支付者信息
     */
    private Payer payer;

}
