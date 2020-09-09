package com.dsltyyz.bundle.common.token.aop;

import com.dsltyyz.bundle.common.handler.ContextHandler;
import com.dsltyyz.bundle.common.jwt.constant.JwtConstant;
import com.dsltyyz.bundle.common.jwt.entity.JwtUser;
import com.dsltyyz.bundle.common.jwt.helper.JwtHelper;
import com.dsltyyz.bundle.common.token.annotation.RequireAuthorization;
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
public class RequireAuthorizationAspect {

    public static final String HEADER_AUTHORIZATION = "Authorization";

    @Resource
    private JwtHelper jwtHelper;

    @Around("@annotation(requireAuthorization)")
    public Object doAround(ProceedingJoinPoint point, RequireAuthorization requireAuthorization) throws Throwable {
        //执行之前
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(HEADER_AUTHORIZATION);
        Assert.isTrue(!StringUtils.isEmpty(token), "缺少Authorization");
        JwtUser jwtUser = jwtHelper.parserToken(token);
        ContextHandler.set(JwtConstant.JWT_ADMIN, jwtUser);

        //下一步主流程
        Object next = point.proceed();

        //返回移除
        ContextHandler.remove(JwtConstant.JWT_USER);
        //执行之后
        return next;
    }
}
