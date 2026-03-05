package com.dsltyyz.bundle.wechat.common.model.shipping;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 * 支付时间所属范围
 *
 * @author: dsltyyz
 * @date: 2026-3-5
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PayTimeRange implements Serializable {

    /**
     * 起始时间，时间戳形式，不填则视为从0开始。秒级
     */
    private Long begin_time;

    /**
     * 结束时间（含），时间戳形式，不填则视为32位无符号整型的最大值。秒级
     */
    private Long end_time;

}
