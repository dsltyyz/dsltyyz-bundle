package com.dsltyyz.bundle.wechat.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.MessageDigest;
import java.security.Security;
import java.util.Arrays;

/**
 * Description:
 * 通用工具类
 *
 * @author: dsltyyz
 * @since: 2019-11-07
 */
public class WechatCommonUtils {

    /**
     * sha1加密工具方法
     * @param array
     * @return
     */
    private static String sha1(String... array) {
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
     * @param echostr 输出
     * @param checkToken 验证token(微信回调配置TOKEN)
     * @return
     */
    public static String callbackCheck(String signature, String timestamp, String nonce, String echostr, String checkToken){
        String sha1Str = sha1(checkToken, timestamp, nonce);
        return signature.equals(sha1Str)?echostr:"error";
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

}
