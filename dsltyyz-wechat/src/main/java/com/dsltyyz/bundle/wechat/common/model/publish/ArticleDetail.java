package com.dsltyyz.bundle.wechat.common.model.publish;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Description:
 * 微信发布文章
 *
 * @author: dsltyyz
 * @date: 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleDetail implements Serializable {

    /**
     * 当发布状态为0时（即成功）时，返回文章对应的编号
     */
    private Integer idx;

    /**
     * 当发布状态为0时（即成功）时，返回图文的永久链接
     */
    private String article_url;

}
