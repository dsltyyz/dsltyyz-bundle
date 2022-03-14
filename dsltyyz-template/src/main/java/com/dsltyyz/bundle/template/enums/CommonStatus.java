package com.dsltyyz.bundle.template.enums;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态枚举
 *
 * @author dsltyyz
 */
@AllArgsConstructor
@Getter
public enum CommonStatus {

    /**
     * 不可用
     */
    DISABLE(-1, "不可用"),
    /**
     * 可用
     */
    ENABLE(1, "可用");

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
        for (CommonStatus e : CommonStatus.values()) {
            JSONObject object = new JSONObject();
            object.put("value", e.getValue());
            object.put("name", e.getName());
            jsonArray.add(object);
        }
        return jsonArray;
    }

}
