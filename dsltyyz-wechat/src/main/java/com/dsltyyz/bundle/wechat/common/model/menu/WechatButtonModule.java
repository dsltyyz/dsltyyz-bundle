package com.dsltyyz.bundle.wechat.common.model.menu;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 微信按钮模块
 *
 * @author: dsltyyz
 * @date: 2022-4-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatButtonModule extends WechatModule implements Serializable {

    /**
     * 二级及以上菜单
     * 最多包含5个二级菜单
     */
    private List<WechatModule> sub_button;

}
