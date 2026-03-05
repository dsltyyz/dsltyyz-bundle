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
public class ConfirmOrderKey extends SearchOrder implements Serializable {

    /**
     * 快递签收时间，时间戳形式
     */
    private Long received_time;

}
