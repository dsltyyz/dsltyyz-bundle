package com.dsltyyz.bundle.jwt.token.aop;

import com.dsltyyz.bundle.common.cache.client.CacheClient;
import com.dsltyyz.bundle.common.handler.ContextHandler;
import com.dsltyyz.bundle.common.response.CommonResponse;
import com.dsltyyz.bundle.common.util.EncryptUtils;
import com.dsltyyz.bundle.jwt.constant.JwtConstant;
import com.dsltyyz.bundle.jwt.entity.JwtUser;
import com.dsltyyz.bundle.jwt.helper.JwtHelper;
import com.dsltyyz.bundle.jwt.token.annotation.RequireAdminToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
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
 * @date: 2019-3-21
 */
@Slf4j
@Aspect
@Component
public class RequireAdminTokenAspect {

    public static final String HEADER_ADMIN_TOKEN = "AdminToken";
    public static final String ADMIN_TOKEN_KEY = "AdminTokenKey";

    @Resource
    private JwtHelper jwtHelper;

    @Resource
    private CacheClient cacheClient;

    @Around("@annotation(requireAdminToken)")
    public Object doAround(ProceedingJoinPoint point, RequireAdminToken requireAdminToken) throws Throwable {
        //执行之前
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(HEADER_ADMIN_TOKEN);
        if (StringUtils.isEmpty(token)) {
            return new CommonResponse<>(1L, "缺少AdminToken参数");
        }
        JwtUser jwtUser = null;
        try {
            jwtUser = jwtHelper.parserToken(token);
            ContextHandler.set(JwtConstant.JWT_ADMIN, jwtUser);
        } catch (MalformedJwtException | SignatureException e) {
            return new CommonResponse<>(1L, "AdminToken非法签名");
        } catch (ExpiredJwtException e) {
            return new CommonResponse<>(2L, "AdminToken已过期，请重新登录");
        } catch (JwtException e){
            return new CommonResponse<>(3L, "AdminToken出错啦");
        }

        //需要权限 且 需要权限与用户权限没有交集
        Boolean flag = requireAdminToken.value().length != 0 && (jwtUser.getRole().length == 0 || !Arrays.asList(requireAdminToken.value()).retainAll(Arrays.asList(jwtUser.getRole())));
        if (flag) {
            return new CommonResponse<>(3L, "权限不足");
        }

        //防止重复提交
        String key = EncryptUtils.MD5(token, request.getRequestURI() + request.getMethod(), 32);
        //标识ADMIN_TOKEN_KEY 出现异常移除
        ContextHandler.set(ADMIN_TOKEN_KEY, key);
        String value = cacheClient.getEntity(key, String.class);
        if(value!=null){
            return new CommonResponse<>(4L, "操作过于频繁，请稍后重试");
        }
        //添加标识
        cacheClient.putEntity(key, key);

        //下一步主流程
        Object next = point.proceed();

        //移除标识
        cacheClient.deleteEntity(key);
        //执行之后
        return next;
    }

    @AfterThrowing("@annotation(requireAdminToken)")
    public void afterThrowing(RequireAdminToken requireAdminToken) {
        //出现异常 移除标识
        cacheClient.deleteEntity(ContextHandler.get(ADMIN_TOKEN_KEY).toString());
    }
}
