package com.dsltyyz.bundle.common.util;

import cn.hutool.crypto.SmUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;

/**
 * 区块链加密工具类
 *
 * @author dsltyyz
 * @date 2022-7-12
 */
public class BlockChainEncryptUtils {

    /**
     * SHA256加密
     *
     * @param s
     * @return
     */
    public static String sha256Hex(String s) {
        return DigestUtils.sha256Hex(s);
    }

    /**
     * SHA256加密
     *
     * @param b
     * @return
     */
    public static String sha256Hex(byte[] b) {
        return DigestUtils.sha256Hex(b);
    }

    /**
     * SHA256加密
     *
     * @param i
     * @return
     */
    public static String sha256Hex(InputStream i) {
        try {
            return DigestUtils.sha256Hex(i);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * MD5加密
     *
     * @param s
     * @return
     */
    public static String md5Hex(String s) {
        return DigestUtils.md5Hex(s);
    }

    /**
     * MD5加密
     *
     * @param b
     * @return
     */
    public static String md5Hex(byte[] b) {
        return DigestUtils.md5Hex(b);
    }

    /**
     * MD5加密
     *
     * @param i
     * @return
     */
    public static String md5Hex(InputStream i) {
        try {
            return DigestUtils.md5Hex(i);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * MD5加密
     *
     * @param s
     * @return
     */
    public static String sm3Hex(String s) {
        return SmUtil.sm3(s);
    }


    /**
     * SM3加密
     *
     * @param b
     * @return
     */
    public static String sm3Hex(byte[] b) {
        return SmUtil.sm3().digestHex(b);
    }

    /**
     * MD5加密
     *
     * @param i
     * @return
     */
    public static String sm3Hex(InputStream i) {
        return SmUtil.sm3(i);
    }

    /**
     * 字符串转十六进制
     *
     * @param str
     * @return
     */
    public static String strToHex(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        Assert.isTrue(sb.toString().length()<1024*1024, "十六进制字符串大于1M");
        return sb.toString().trim();
    }

    /**
     * 十六进制转字符串
     *
     * @param hex
     * @return
     */
    public static String hexToStr(String hex) {
        String str = "0123456789ABCDEF";
        char[] hexs = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    public static void main(String[] args) {
        String hex = strToHex("Nhooo");
        System.out.println(hex);
        System.out.println(hexToStr(hex));
    }
}
