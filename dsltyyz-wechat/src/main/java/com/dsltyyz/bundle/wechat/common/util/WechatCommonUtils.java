package com.dsltyyz.bundle.wechat.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Arrays;

/**
 * Description:
 * 通用工具类
 *
 * @author: dsltyyz
 * @date: 2019-11-07
 */
public class WechatCommonUtils {

    static{
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * sha1加密工具方法
     * @param array
     * @return
     */
    public static String sha1(String... array) {
        StringBuffer sb = new StringBuffer();
        // 字符串排序
        Arrays.sort(array);
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
        }
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
            byte[] result = mDigest.digest(sb.toString().getBytes());
            StringBuffer hexStr = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                hexStr.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
            return hexStr.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 【服务号】【小程序】微信回调检测
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 字符串
     * @param checkToken 验证token(微信回调配置TOKEN)
     * @return
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce, String checkToken){
        String sha1Str = sha1(checkToken, timestamp, nonce);
        return signature.equals(sha1Str);
    }

    /**
     * 【服务号】【小程序】微信回调检测并返回
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 字符串
     * @param echostr 输出
     * @param checkToken 验证token(微信回调配置TOKEN)
     * @return
     */
    public static String callbackCheck(String signature, String timestamp, String nonce, String echostr, String checkToken){
        return checkSignature(signature, timestamp, nonce, checkToken)?echostr:"error";
    }

    /**
     * 【小程序】数据解密
     *
     * @param encryptedData
     * @param keyData
     * @param ivData
     * @return
     */
    public static JSONObject decrypt(String encryptedData, String keyData, String ivData) throws Exception {
        Key key = new SecretKeySpec(Base64.decodeBase64(keyData), "AES");
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(Base64.decodeBase64(ivData)));
        // 创建密码器
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, params);
        byte[] bytes = cipher.doFinal(Base64.decodeBase64(encryptedData));
        return  JSONObject.parseObject(new String(bytes));
    }

    /***
     * 微信退款回调数据解密
     * @param encryptedData
     * @param mchPrivateKey 商户私钥
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptedData, String mchPrivateKey) throws Exception {
        Key key = new SecretKeySpec(getMD5(mchPrivateKey).getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] bytes = cipher.doFinal(Base64.decodeBase64(encryptedData));
        return new String(bytes);
    }

    public static String getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String result = MD5(str, md);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String MD5(String strSrc, MessageDigest md) {
        byte[] bt = strSrc.getBytes();
        md.update(bt);
        String strDes = bytes2Hex(md.digest());
        return strDes;
    }

    public static String bytes2Hex(byte[] bts) {
        StringBuffer des = new StringBuffer();
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }

}
