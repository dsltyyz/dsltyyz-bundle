package com.dsltyyz.bundle.wechat.common.model.draft;

import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 发送微信草稿
 *
 * @author: dsltyyz
 * @date: 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatDraftDetailVO extends WechatResult implements Serializable {

    /**
     * 新闻列表
     */
    private List<WechatDraftVO> news_item;
}
