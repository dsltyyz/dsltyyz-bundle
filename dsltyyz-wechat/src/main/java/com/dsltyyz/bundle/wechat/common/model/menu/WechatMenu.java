package com.dsltyyz.bundle.wechat.common.model.menu;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 微信菜单
 *
 * @author: dsltyyz
 * @date: 2022-4-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatMenu implements Serializable {

    /**
     * 一级菜单
     * 最多包括3个一级菜单
     */
    private List<WechatButtonMenu> button;
}
