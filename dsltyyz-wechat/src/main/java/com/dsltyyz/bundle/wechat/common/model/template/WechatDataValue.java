package com.dsltyyz.bundle.wechat.common.model.template;

import lombok.*;

import java.io.Serializable;

/**
 * Description:
 * 微信数据值
 *
 * @author: dsltyyz
 * @since: 2019-11-07
 */
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class WechatDataValue implements Serializable {

    /**
     * 模板ID
     */
    @NonNull
    private String value;

    /**
     * 模板标题
     */
    private String color = "#173177";

}
