package com.dsltyyz.bundle.wechat.common.model.token;

import com.alibaba.fastjson.JSONObject;
import com.dsltyyz.bundle.common.util.DateUtils;
import com.dsltyyz.bundle.wechat.common.model.common.WechatResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

/**
 * Description:
 * 微信token
 *
 * @author: dsltyyz
 * @date: 2019/11/06
 */
@NoArgsConstructor
@Data
public class WechatToken extends WechatResult {

    /**
     * 获取到的凭证
     */
    private String access_token;

    /**
     * 凭证有效时间，单位：秒
     */
    private Integer expires_in;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 过期时间
     */
    private Date expirationTime;

    public WechatToken(String access_token, Integer expires_in) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.createTime = new Date();
        this.expirationTime = DateUtils.calc(this.createTime, Calendar.SECOND, this.expires_in);
    }

    public WechatToken(JSONObject jsonObject) {
        this(jsonObject.getString("access_token"), jsonObject.getInteger("expires_in"));
    }

    /**
     * 判断是否过期
     *
     * @return
     */
    public boolean isExpired() {
        return this.expirationTime.before(new Date());
    }
}
