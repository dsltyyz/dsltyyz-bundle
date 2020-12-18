package com.dsltyyz.bundle.jwt.token.aop;

import com.dsltyyz.bundle.common.handler.ContextHandler;
import com.dsltyyz.bundle.jwt.constant.JwtConstant;
import com.dsltyyz.bundle.jwt.entity.JwtUser;
import com.dsltyyz.bundle.jwt.helper.JwtHelper;
import com.dsltyyz.bundle.jwt.token.annotation.RequireAdminToken;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Description:
 * 检测管理员
 *
 * @author: dsltyyz
 * @date: 2019/3/21 0021
 */
@Slf4j
@Aspect
@Component
public class RequireAdminTokenAspect {

    public static final String HEADER_ADMIN_TOKEN = "AdminToken";

    @Resource
    private JwtHelper jwtHelper;

    @Around("@annotation(requireAdminToken)")
    public Object doAround(ProceedingJoinPoint point, RequireAdminToken requireAdminToken) throws Throwable {
        //执行之前
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(HEADER_ADMIN_TOKEN);
        if (StringUtils.isEmpty(token)) {
            throw new IllegalAccessException("缺少AdminToken参数");
        }
        JwtUser jwtUser = jwtHelper.parserToken(token);
        ContextHandler.set(JwtConstant.JWT_ADMIN, jwtUser);

        //需要权限 且 需要权限与用户权限没有交集
        if (requireAdminToken.value().length != 0 && (jwtUser.getRole().length == 0 || !Arrays.asList(requireAdminToken.value()).retainAll(Arrays.asList(jwtUser.getRole())))) {
            throw new IllegalAccessException("权限不足");
        }

        //下一步主流程
        Object next = point.proceed();
        //执行之后
        return next;
    }
}
