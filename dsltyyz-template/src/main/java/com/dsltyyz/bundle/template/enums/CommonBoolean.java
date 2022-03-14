package com.dsltyyz.bundle.template.enums;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用布尔枚举
 *
 * @author dsltyyz
 */
@AllArgsConstructor
@Getter
public enum CommonBoolean {

    /**
     * 不可用
     */
    FALSE(0, "否"),
    /**
     * 可用
     */
    TRUE(1, "是");

    /**
     * 值
     */
    @EnumValue
    @JsonValue
    private int value;

    /**
     * 名称
     */
    private String name;

    /**
     * 获取枚举JSON数组
     */
    public static JSONArray toJson() {
        JSONArray jsonArray = new JSONArray();
        for (CommonBoolean e : CommonBoolean.values()) {
            JSONObject object = new JSONObject();
            object.put("value", e.getValue());
            object.put("name", e.getName());
            jsonArray.add(object);
        }
        return jsonArray;
    }

}
