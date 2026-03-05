package com.dsltyyz.bundle.wechat.common.model.shipping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 微信订单列表结果
 *
 * @author: dsltyyz
 * @date: 2026-3-5
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WechatOrderListResult implements Serializable {

    /**
     * 翻页时使用。
     */
    private String last_index;

    /**
     * 是否还有更多支付单。
     */
    private Boolean has_more;

    /**
     * 公众账号ID
     */
    private List<OrderListInfo> order_list;


}