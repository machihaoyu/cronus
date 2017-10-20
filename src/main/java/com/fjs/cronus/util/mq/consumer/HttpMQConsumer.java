package com.fjs.cronus.util.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.impl.authority.AuthUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpMQConsumer {

    @Value("${mq.ak}")
    private String accessKey;

    @Value("${mq.sk}")
    private String secretKey;

    @Value("${mq.consumerId}")
    private String consumerId;

    @Value("${mq.url}")
    private String url;

    @Value("${mq.topic}")
    private String topic;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String NEWLINE = "\n";




    /**
     * @param tag 消息分支
     * @param key 消息key
     * @return
     */
    public List<SimpleMessage> pull(String tag, String key) {
        List<SimpleMessage> result = null;
        long time = System.currentTimeMillis();
        GetRequest req = Unirest.get(url);
        String signString = topic + NEWLINE + consumerId + NEWLINE + time;
        String sign = AuthUtil.calSignature(signString.getBytes(StandardCharsets.UTF_8), secretKey);
        req.header("Signature", sign);
        req.header("AccessKey", accessKey);
        req.header("ConsumerID", consumerId);
        req.queryString("topic", topic);
        req.queryString("time", time);
        //req.queryString("num", 32);
        req.queryString("num", 32);
        if (StringUtils.isNotBlank(tag)) {
            req.queryString("tag", tag);
        }
        if (StringUtils.isNotBlank(key)) {
            req.queryString("key", key);
        }
        try {
            HttpResponse<String> res = req.asString();
            if (res.getStatus() == 200) {
                if (res.getBody() != null && !res.getBody().isEmpty()) {
                    try {
                        result = JSON.parseArray(res.getBody(), SimpleMessage.class);
                    } catch (Exception e) {
                        logger.error("get message error", e);
                    }
                }
            }
        } catch (UnirestException e) {
            logger.error("get message error", e);
        }
        return result;
    }

    public boolean delete(String msgHandle) {
        long time = System.currentTimeMillis();
        HttpRequestWithBody req = Unirest.delete(url);
        String signString = topic + NEWLINE + consumerId + NEWLINE + msgHandle + NEWLINE
                + time;
        String sign = AuthUtil.calSignature(signString.getBytes(StandardCharsets.UTF_8), secretKey);
        req.header("Signature", sign);
        req.header("AccessKey", accessKey);
        req.header("ConsumerID", consumerId);
        req.queryString("topic", topic);
        req.queryString("time", time);
        req.queryString("timeout", "300000");
        req.queryString("msgHandle", msgHandle);
        try {
            HttpResponse<String> res = req.asString();
            if (res.getStatus() == 204) {
                return true;
            } else {
                logger.error("delete message error: {}", msgHandle, res.getBody());
            }
        } catch (UnirestException e) {
            logger.error("delete message error: {}", msgHandle, e);
        }
        return false;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

}
