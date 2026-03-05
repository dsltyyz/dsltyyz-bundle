package com.dsltyyz.bundle.wechat.common.model.shipping;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 订单列表发货信息
 *
 * @author: dsltyyz
 * @date: 2026-3-5
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderListShippingInfo implements Serializable {

    /**
     * 发货模式，发货模式枚举值：1、UNIFIED_DELIVERY（统一发货）2、SPLIT_DELIVERY（分拆发货）
     */
    private Integer delivery_mode;

    /**
     * 物流模式，发货方式枚举值：1、实体物流配送采用快递公司进行实体物流配送形式 2、同城配送 3、虚拟商品，虚拟商品，例如话费充值，点卡等，无实体配送形式 4、用户自提
     */
    private Integer logistics_type;

    /**
     * 是否已完成全部发货。
     */
    private Boolean finish_shipping;

    /**
     * 在小程序后台发货信息录入页录入的商品描述。
     */
    private String goods_desc;


    /**
     * 已完成全部发货的次数，未完成时为 0，完成时为 1，重新发货并完成后为 2。
     */
    private String finish_shipping_count;

    /**
     * 支付单实际支付金额，整型，单位：分钱。
     */
    private List<ShippingListInfo> shipping_list;



}
