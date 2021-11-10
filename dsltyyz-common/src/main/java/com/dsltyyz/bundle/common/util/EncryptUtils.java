package com.dsltyyz.bundle.common.util;


import java.security.MessageDigest;

/**
 * Description:
 * 加密工具类
 *
 * @author: dsltyyz
 * @since: 2019-3-2
 */
public class EncryptUtils {

    public static String MD5 = "MD5";
    public static String SHA = "SHA";

    private static final String PREFIX = "dsltyyz=>";
    private static char[] HEXDIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * MD5加密
     *
     * @param content 内容
     * @param salt    盐值
     * @param length  长度
     * @return String
     */
    public final static String MD5(String content, String salt, int length) {
        return algorithm(content, salt, length, MD5);
    }

    /**
     * SHA加密
     *
     * @param content 内容
     * @param salt    盐值
     * @param length  长度
     * @return
     */
    public final static String SHA(String content, String salt, int length) {
        return algorithm(content, salt, length, SHA);
    }

    /**
     * 加密基础算法
     *
     * @param content 内容
     * @param salt    盐值
     * @param length  长度
     * @param method  加密方式 MD5 SHA
     * @return String
     */
    private final static String algorithm(String content, String salt, int length, String method) {

        try {
            content = getContentWithSalt(content, salt);
            byte[] btInput = content.getBytes();
            // 获得method摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance(method);
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = HEXDIGITS[byte0 >>> 4 & 0xf];
                str[k++] = HEXDIGITS[byte0 & 0xf];
            }
            return new String(str).substring(0, length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 字符串content，salt组装
     *
     * @param content
     * @param salt
     * @return
     */
    private static String getContentWithSalt(String content, String salt) {
        StringBuffer sb = new StringBuffer();
        sb.append(PREFIX);
        sb.append("{");
        sb.append(salt);
        sb.append(":");
        sb.append(content);
        sb.append("}");
        return sb.toString();
    }

    public static void main(String[] args) {
    }
}
