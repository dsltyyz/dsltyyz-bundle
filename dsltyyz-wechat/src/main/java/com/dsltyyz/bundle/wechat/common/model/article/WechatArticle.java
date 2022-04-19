package com.dsltyyz.bundle.wechat.common.model.article;

import com.dsltyyz.bundle.wechat.common.model.draft.WechatDraftSend;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 微信文章
 *
 * @author: dsltyyz
 * @date: 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatArticle implements Serializable {

    /**
     * 文章构成的草稿列表
     */
    private List<WechatDraftSend> articles;

}
