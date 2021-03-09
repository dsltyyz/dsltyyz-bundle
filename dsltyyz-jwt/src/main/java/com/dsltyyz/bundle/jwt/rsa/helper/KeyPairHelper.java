package com.dsltyyz.bundle.jwt.rsa.helper;

import com.dsltyyz.bundle.common.util.Base64Utils;
import com.dsltyyz.bundle.common.util.FileUtils;
import com.dsltyyz.bundle.jwt.rsa.properties.KeyPairProperties;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Description:
 * 加密解密
 *
 * @author: dsltyyz
 * @since: 2019-3-11
 */
@ApiModel(description = "加密解密工具")
@Slf4j
public class KeyPairHelper {

    @Resource
    private KeyPairProperties keyPairProperties;

    public KeyPair initDefaultKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyPairProperties.getAlgorithm());
            SecureRandom secureRandom;
            if (!StringUtils.isEmpty(keyPairProperties.getSeed())) {
                secureRandom = new SecureRandom(keyPairProperties.getSeed().getBytes());
            } else {//使用默认
                secureRandom = new SecureRandom();
            }
            keyPairGenerator.initialize(keyPairProperties.getSize(), secureRandom);
            // 生成一个密钥对
            return keyPairGenerator.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 生成RSA 公钥 私钥文件
     *
     * @param clazz
     */
    public void createKeyPairFile(Class clazz) {
        Assert.notNull(keyPairProperties.getPublicKeyPath(), "公钥文件路径不能为空");
        Assert.notNull(keyPairProperties.getPrivateKeyPath(), "私钥文件路径不能为空");
        String path = clazz.getResource(File.separator).getPath();
        String projectResourcePath = path.substring(0, path.indexOf("/target")) + "/src/main/resources/";
        String publicFilePath = projectResourcePath + keyPairProperties.getPublicKeyPath();
        String privateFilePath = projectResourcePath + keyPairProperties.getPrivateKeyPath();
        // 输出公钥、私钥
        FileOutputStream fos = null;
        BufferedWriter osw = null;
        KeyPair keyPair = initDefaultKeyPair();
        try {
            PublicKey publicKey = keyPair.getPublic();
            File publicFile = FileUtils.checkFileExists(publicFilePath);
            fos = new FileOutputStream(publicFile);
            osw = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            osw.write(Base64Utils.encode(publicKey.getEncoded()));
            osw.flush();
            osw.close();

            PrivateKey privateKey = keyPair.getPrivate();
            File privateFile = FileUtils.checkFileExists(privateFilePath);
            fos = new FileOutputStream(privateFile);
            osw = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            osw.write(Base64Utils.encode(privateKey.getEncoded()));
            osw.flush();
            osw.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            if (null != osw) {
                try {
                    osw.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    public PublicKey getPublicKey() {
        String content = FileUtils.readFile(keyPairProperties.getPublicKeyPath());
        //获取public文件
        if (!StringUtils.isEmpty(content)) {
            try {
                X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64Utils.decode(content));
                KeyFactory keyFactory = KeyFactory.getInstance(keyPairProperties.getAlgorithm());
                return keyFactory.generatePublic(spec);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        //返回默认public key
        return initDefaultKeyPair().getPublic();
    }

    public PrivateKey getPrivateKey() {
        String content = FileUtils.readFile(keyPairProperties.getPrivateKeyPath());
        //获取private文件
        if (!StringUtils.isEmpty(content)) {
            try {
                PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64Utils.decode(content));
                KeyFactory keyFactory = KeyFactory.getInstance(keyPairProperties.getAlgorithm());
                return keyFactory.generatePrivate(spec);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        //返回默认private key
        return initDefaultKeyPair().getPrivate();
    }

}
