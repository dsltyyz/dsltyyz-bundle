package com.dsltyyz.bundle.wechat.common.model.template;

import lombok.*;

/**
 * Description:
 * 微信数据
 *
 * @author: dsltyyz
 * @since: 2019-11-07
 */
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class WechatData {

    /**
     * 头
     */
    @NonNull
    private WechatDataValue first;

    /**
     * 关键字1
     */
    @NonNull
    private WechatDataValue keyword1;

    /**
     * 关键字2
     */
    @NonNull
    private WechatDataValue keyword2;

    /**
     * 关键字3
     */
    @NonNull
    private WechatDataValue keyword3;


    /**
     * 关键字4
     */
    private WechatDataValue keyword4;

    /**
     * 关键字5
     */
    private WechatDataValue keyword5;

    /**
     * 尾
     */
    @NonNull
    private WechatDataValue remark;


}
