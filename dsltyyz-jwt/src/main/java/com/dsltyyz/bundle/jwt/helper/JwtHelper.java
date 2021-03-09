package com.dsltyyz.bundle.jwt.helper;

import com.alibaba.fastjson.JSONArray;
import com.dsltyyz.bundle.jwt.entity.JwtToken;
import com.dsltyyz.bundle.jwt.entity.JwtUser;
import com.dsltyyz.bundle.jwt.rsa.helper.KeyPairHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Description:
 * Jwt
 *
 * @author: dsltyyz
 * @since: 2020-9-8
 */
public class JwtHelper {

    @Resource
    private KeyPairHelper keyPairHelper;

    /**
     * 对象转token
     *
     * @param jwtUser
     * @return
     */
    public JwtToken getToken(JwtUser jwtUser) {
        //一天过期
        LocalDateTime expireTime = LocalDateTime.now().plusDays(1);

        Claims claims = Jwts.claims();
        claims.put("id", jwtUser.getId());
        claims.put("user", jwtUser.getUser());
        claims.put("role", JSONArray.toJSONString(jwtUser.getRole()));
        String token = Jwts.builder()
                // 设置claims
                .setClaims(claims)
                // 发行者
                .setIssuer("dsltyyz")
                // 设置面向的用户
                .setSubject(jwtUser.getUser())
                // 设置过期时间
                .setExpiration(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
                // 设置算法（必须）
                .signWith(SignatureAlgorithm.RS256, keyPairHelper.getPrivateKey())
                // 全部设置完成后拼成jwt串
                .compact();
        return new JwtToken(token, expireTime);
    }

    /**
     * token转对象
     *
     * @param token
     * @return
     */
    public JwtUser parserToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(keyPairHelper.getPublicKey()).parseClaimsJws(token);
        Claims body = claims.getBody();
        return new JwtUser(Long.valueOf(body.get("id").toString()), String.valueOf(body.get("user")), JSONArray.parseArray(body.get("role").toString(), String.class).toArray(new String[0]));
    }
}
