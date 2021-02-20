package com.dsltyyz.bundle.jwt.token.aop;

import com.dsltyyz.bundle.common.cache.client.CacheClient;
import com.dsltyyz.bundle.common.handler.ContextHandler;
import com.dsltyyz.bundle.common.util.EncryptUtils;
import com.dsltyyz.bundle.jwt.constant.JwtConstant;
import com.dsltyyz.bundle.jwt.entity.JwtUser;
import com.dsltyyz.bundle.jwt.helper.JwtHelper;
import com.dsltyyz.bundle.jwt.token.annotation.RequireToken;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Description:
 * 检测用户
 *
 * @author: dsltyyz
 * @date: 2019/3/21 0021
 */
@Slf4j
@Aspect
@Component
public class RequireTokenAspect {

    public static final String HEADER_TOKEN = "Token";

    @Resource
    private JwtHelper jwtHelper;

    @Resource
    private CacheClient cacheClient;

    @Around("@annotation(requireToken)")
    public Object doAround(ProceedingJoinPoint point, RequireToken requireToken) throws Throwable {
        //执行之前
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(HEADER_TOKEN);
        if (StringUtils.isEmpty(token)) {
            throw new JwtException("缺少Token参数");
        }
        JwtUser jwtUser = jwtHelper.parserToken(token);
        ContextHandler.set(JwtConstant.JWT_USER, jwtUser);

        //需要权限 且 需要权限与用户权限没有交集
        Boolean flag = requireToken.value().length != 0 && (jwtUser.getRole().length == 0 || !Arrays.asList(requireToken.value()).retainAll(Arrays.asList(jwtUser.getRole())));
        if (flag) {
            throw new JwtException("权限不足");
        }

        //防止重复提交
        String key = EncryptUtils.MD5(token, request.getRequestURI()+request.getMethod(), 32);
        String value = cacheClient.getEntity(key, String.class);
        Assert.isNull(value, "操作过于频繁，请稍后重试");
        //添加标识
        cacheClient.putEntity(key, key);

        //下一步主流程
        Object next = point.proceed();
        //移除标识
        cacheClient.deleteEntity(key);
        //执行之后
        return next;
    }

}
