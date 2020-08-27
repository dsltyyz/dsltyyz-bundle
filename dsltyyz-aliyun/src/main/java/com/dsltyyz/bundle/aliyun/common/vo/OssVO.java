package com.dsltyyz.bundle.aliyun.common.vo;

import lombok.*;

/**
 * Description:
 * OSS值对象
 *
 * @author: dsltyyz
 * @date: 2019/11/19
 */
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class OssVO {

    /**
     * 名称
     */
    private String name;

    /**
     * 唯一标识
     */
    @NonNull
    private String key;

    /**
     * 访问路径
     */
    @NonNull
    private String url;
}
