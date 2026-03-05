package com.dsltyyz.bundle.wechat.common.model.shipping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 微信订单查询结果
 *
 * @author: dsltyyz
 * @date: 2026-3-5
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WechatOrderResult implements Serializable {

    /**
     * 公众账号ID
     */
    private OrderListInfo order;


}