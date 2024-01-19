package com.dsltyyz.bundle.common.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取用户访问IP
 *
 * @author dsltyyz
 * @date 2022-5-8
 */
public class IpUtils {

    private static String LOCALHOST = "0:0:0:0:0:0:0:1";
    private static String LOCALHOST_IP = "127.0.0.1";

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 获取到多个ip时取第一个作为客户端真实ip
        if (!StringUtils.isEmpty(ip) && ip.contains(",")) {
            String[] ipArray = ip.split(",");
            if (ipArray != null && ipArray.length > 0) {
                ip = ipArray[0];
            }
        }
        return LOCALHOST.equals(ip) ? LOCALHOST_IP : ip;
    }

}
