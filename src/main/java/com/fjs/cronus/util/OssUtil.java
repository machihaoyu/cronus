package com.fjs.cronus.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.BucketInfo;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.exception.CronusException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
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
            String picUrl = aliyunOssUrl + keyUrl;
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

    public static String downLoad(HttpServletResponse response,String filename, String key) {
        OSSClient ossClient = null;
        try {
            ossClient = init();
            logger.info("start download!");
            OSSObject ossObject = ossClient.getObject(bucketName,key);
            BufferedInputStream in=new BufferedInputStream(ossObject.getObjectContent());
            BufferedOutputStream out=new BufferedOutputStream(response.getOutputStream());
            response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(filename,"utf-8"));
            IOUtils.copyLarge(in,out);
            if(out!=null){
                out.flush();
                out.close();
            }
            if(in!=null){
                in.close();
            }
            logger.info("End download!");
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new CronusException(CronusException.Type.THEA_SYSTEM_ERROR);
        } finally {
            if (null != ossClient)
                ossClient.shutdown();
        }

    }
}
