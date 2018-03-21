package com.fjs.cronus.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.BucketInfo;
import com.aliyun.oss.model.PutObjectRequest;
import com.fjs.cronus.exception.CronusException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.Date;

/**
 * oss tool
 */
@Component
public class OssUtil {

    static Logger logger = Logger.getLogger(OssUtil.class);

    private static String endpoint;

    private static String accessKeyId;

    private static String accessKeySecret;

    private static String bucketName;

    private static String aliyunOssUrl;

    @Value("${aliyun.oss.endpoint}")
    public void setEndpoint(String endpoint) {
        OssUtil.endpoint = endpoint;
    }

    @Value("${aliyun.oss.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        OssUtil.accessKeyId = accessKeyId;
    }

    @Value("${aliyun.oss.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        OssUtil.accessKeySecret = accessKeySecret;
    }

    @Value("${aliyun.oss.bucketName}")
    public void setBucketName(String bucketName) {
        OssUtil.bucketName = bucketName;
    }

    @Value("${aliyun.oss.url}")
    public void setAliyunOssUrl(String aliyunOssUrl) {
        OssUtil.aliyunOssUrl = aliyunOssUrl;
    }

    private static OSSClient init() {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        BucketInfo f = ossClient.getBucketInfo(bucketName);
        return ossClient;
    }

    public static String uploadImag(String fileName, InputStream in,String keyUrl) {
        OSSClient ossClient = null;
        try {
            ossClient = init();
            Date date = new Date();
            logger.info("start upload!");
            ossClient.putObject(new PutObjectRequest(bucketName, keyUrl, in));
            logger.info("End upload!");
            String picUrl = aliyunOssUrl + "/" + keyUrl;
            return picUrl;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new CronusException(CronusException.Type.THEA_SYSTEM_ERROR);
        } finally {
            if (null != ossClient) {
                ossClient.shutdown();
            }
            if (in !=null){
                try{
                    in.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
