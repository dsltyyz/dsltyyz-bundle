package com.dsltyyz.bundle.wechat.common.model.shipping;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 * 物流信息
 *
 * @author: dsltyyz
 * @date: 2026-3-5
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Shipping implements Serializable {

    /**
     * 物流单号，物流快递发货时必填，示例值: 323244567777 字符字节限制: [1, 128]
     */
    private String tracking_no;

    /**
     * 物流公司编码，快递公司ID，参见「获取运力 id 列表get_delivery_list」，物流快递发货时必填，
     */
    private String express_company;

    /**
     * 商品信息，例如：微信红包抱枕*1个，限120个字以内
     */
    private String item_desc;

    /**
     * 联系方式，当发货的物流公司为顺丰时，联系方式为必填，收件人或寄件人联系方式二选一
     */
    private Contact contact;

}
