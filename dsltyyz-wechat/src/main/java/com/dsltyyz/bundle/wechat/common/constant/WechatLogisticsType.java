package com.dsltyyz.bundle.wechat.common.constant;

/**
 * Description:
 * 物流模式，发货方式枚举值：1、实体物流配送采用快递公司进行实体物流配送形式 2、同城配送 3、虚拟商品，虚拟商品，例如话费充值，点卡等，无实体配送形式 4、用户自提
 *
 * @author: dsltyyz
 * @date: 2020-12-9
 */
public interface WechatLogisticsType {

    /**
     * 1、实体物流配送采用快递公司进行实体物流配送形式
     */
    Integer EXPRESS_DELIVERY = 1;

    /**
     * 2、同城配送
     */
    Integer SAME_CITY_DELIVERY = 2;

    /**
     * 3、虚拟商品，虚拟商品，例如话费充值，点卡等，无实体配送形式
     */
    Integer VIRTUAL_PRODUCT = 3;

    /**
     * 4、用户自提
     */
    Integer SELF_PICKUP = 4;

}
