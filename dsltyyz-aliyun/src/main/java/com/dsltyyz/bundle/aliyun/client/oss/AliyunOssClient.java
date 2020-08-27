package com.dsltyyz.bundle.aliyun.client.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.dsltyyz.bundle.aliyun.common.properties.OssProperties;
import com.dsltyyz.bundle.aliyun.common.vo.OssVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.concurrent.Future;

/**
 * Description:
 * 阿里云OSS客户端
 *
 * @author: dsltyyz
 * @date: 2020/08/27
 */
@Slf4j
public class AliyunOssClient {

    @Resource
    private OSS ossClient;

    @Resource
    private OssProperties ossProperties;

    public AliyunOssClient(){
        System.out.println("阿里云OSS客户端已加载");
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
