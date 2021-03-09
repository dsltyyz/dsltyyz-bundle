package com.dsltyyz.bundle.aliyun.client.oss;

import com.alibaba.alicloud.context.AliCloudProperties;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PolicyConditions;
import com.dsltyyz.bundle.aliyun.common.properties.OssProperties;
import com.dsltyyz.bundle.aliyun.common.vo.OssVO;
import com.dsltyyz.bundle.aliyun.common.vo.OssSignatureVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.Future;

/**
 * Description:
 * 阿里云OSS客户端
 *
 * @author: dsltyyz
 * @since: 2020-08-27
 */
@Slf4j
public class AliyunOssClient {

    @Resource
    private OSS ossClient;

    @Resource
    private OssProperties ossProperties;

    @Resource
    private AliCloudProperties aliCloudProperties;

    public AliyunOssClient() {
        log.info("阿里云OSS客户端已加载");
    }

    /**
     * 获取资源访问路径
     *
     * @return
     */
    public String getResourceUrl() {
        return getResourceUrl("");
    }

    /**
     * 获取资源key访问路径
     *
     * @return
     */
    public String getResourceUrl(String key) {
        Assert.notNull(key, "参数key不能为空");
        return "https://BUCKETNAME.ENDPOINT/KEY".replace("BUCKETNAME", ossProperties.getBucketName()).replace("ENDPOINT", ossProperties.getEndpoint()).replace("KEY", key);
    }

    /**
     * 根据指定文件夹获取前端OSS直传参数
     *
     * @param dir
     * @return
     */
    public OssSignatureVO getOssSignature(String dir) {
        try {
            // 生成过期时间
            long expireEndTime = System.currentTimeMillis() + 3600 * 1000;
            Date expiration = new Date(expireEndTime);
            // 生成Policy
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            //指定文件夹
            if (!StringUtils.isEmpty(dir)) {
                policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            }
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);
            return new OssSignatureVO(aliCloudProperties.getAccessKey(), encodedPolicy, postSignature, getResourceUrl(""));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 上传文件
     *
     * @param key
     * @param file
     * @return
     */
    @Async
    public Future<OssVO> putObject(String key, File file) {
        try {
            ossClient.putObject(ossProperties.getBucketName(), key, file);
            log.info("Put Object [{}] to bucket [{}]", key, ossProperties.getBucketName());
            return new AsyncResult<>(new OssVO(file.getName(), key, getResourceUrl(key)));
        } catch (Exception e) {
            log.info("Put Object [{}] to bucket [{}] Error:{}", key, ossProperties.getBucketName(), e.getMessage());
            return null;
        }
    }

    /**
     * 上传文件流
     *
     * @param key
     * @param input
     * @return
     */
    @Async
    public Future<OssVO> putObject(String key, InputStream input) {
        ossClient.putObject(ossProperties.getBucketName(), key, input);
        log.info("Put Object [{}] to bucket [{}]", key, ossProperties.getBucketName());
        return new AsyncResult<>(new OssVO(key, getResourceUrl(key)));
    }

    /**
     * 获取OSS对象
     *
     * @param key
     * @return
     */
    public OSSObject getObject(String key) {
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(ossProperties.getBucketName(), key);
            return ossClient.getObject(getObjectRequest);
        } catch (Exception e) {
            log.info("Put Object [{}] to bucket [{}] Error:{}", key, ossProperties.getBucketName(), e.getMessage());
            return null;
        }
    }
}
