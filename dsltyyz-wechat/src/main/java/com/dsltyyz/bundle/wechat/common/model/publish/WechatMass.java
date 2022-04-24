package com.dsltyyz.bundle.wechat.common.model.publish;

import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 * 微信文章发布
 *
 * @author: dsltyyz
 * @date: 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatMass extends WechatResult implements Serializable {

    /**
     * 消息发送任务的ID
     */
    private Integer msg_id;

    /**
     * 消息的数据ID，该字段只有在群发图文消息时，才会出现。可以用于在图文分析数据接口中，获取到对应的图文消息的数据，是图文分析数据接口中的msgid字段中的前半部分，详见图文分析数据接口中的msgid字段的介绍。
     */
    private Long msg_data_id;

    /**
     * 消息发送后的状态，SEND_SUCCESS表示发送成功，SENDING表示发送中，SEND_FAIL表示发送失败，DELETE表示已删除
     */
    private String msg_status;


}
