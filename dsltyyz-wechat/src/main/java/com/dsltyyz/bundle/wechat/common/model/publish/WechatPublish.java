package com.dsltyyz.bundle.wechat.common.model.publish;

import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 微信文章发布
 *
 * @author: dsltyyz
 * @date: 2019-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatPublish extends WechatResult implements Serializable {

    /**
     * 发布任务的id
     */
    private String publish_id;

    /**
     * 发布状态，0:成功, 1:发布中，2:原创失败, 3: 常规失败, 4:平台审核不通过, 5:成功后用户删除所有文章, 6: 成功后系统封禁所有文章
     */
    private Integer publish_status;

    /**
     * 当发布状态为0时（即成功）时，返回图文的 article_id，可用于“客服消息”场景
     */
    private String article_id;

    /**
     * 文章详情
     */
    private WechatPublishArticle article_detail;

    /**
     * 当发布状态为2或4时，返回不通过的文章编号，第一篇为 1；其他发布状态则为空
     */
    private List<Integer> fail_idx;

}
