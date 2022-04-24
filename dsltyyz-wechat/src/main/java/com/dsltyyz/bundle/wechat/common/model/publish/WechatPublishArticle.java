package com.dsltyyz.bundle.wechat.common.model.publish;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 微信发布文章
 *
 * @author: dsltyyz
 * @date: 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatPublishArticle implements Serializable {

    /**
     * 当发布状态为0时（即成功）时，返回文章数量
     */
    private Integer count;

    /**
     * 文章详情列表
     */
    private List<ArticleDetail> item;

}
