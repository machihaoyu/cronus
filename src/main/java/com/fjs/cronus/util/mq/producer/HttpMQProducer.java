package com.fjs.cronus.util.mq.producer;

import com.aliyun.openservices.ons.api.impl.authority.AuthUtil;
import com.fjs.cronus.util.mq.util.MD5;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;

public class HttpMQProducer {

	@Value("${mq.ak}")
	private String accessKey;

	@Value("${mq.sk}")
	private String secretKey;

	@Value("${mq.producerId}")
	private String producerId;

	@Value("${mq.url}")
	private String url;

	@Value("${mq.topic}")
	private String topic;

	private static final String NEWLINE = "\n";
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

//	/**
//	 * 发送普通消息
//	 *
//	 * @param msg
//	 * @param tag
//	 * @param key
//	 * @return
//	 */
//	public boolean send(String msg, String tag, String key, Long startDeliverTime) {
//		return send(msg, tag, key, null);
//	}

	/**
	 * 发送消息
	 * 
	 * @param msg 消息体（Json）
	 * @param tag 消息分支
	 * @param key 消息key
	 * @param startDeliverTime 推送时间(实时直接传入null)
	 * @return
	 */
	public boolean send(String msg, String tag, String key, Long startDeliverTime) {
		long time = System.currentTimeMillis();
		HttpRequestWithBody req = Unirest.post(url);
		String signString = topic + NEWLINE + producerId + NEWLINE + MD5.getInstance().getMD5String(msg) + NEWLINE
				+ time;
		String sign = AuthUtil.calSignature(signString.getBytes(StandardCharsets.UTF_8), secretKey);
		req.header("Signature", sign);
		req.header("AccessKey", accessKey);
		req.header("ProducerID", producerId);
		req.queryString("topic", topic);
		req.queryString("time", time);
		if(StringUtils.isNotBlank(tag)){
			req.queryString("tag", tag);
		}
		if(StringUtils.isNotBlank(key)){
			req.queryString("key", key);
		}
		if (startDeliverTime != null) {
			req.queryString("startdelivertime", startDeliverTime);
		}
		req.body(msg);

		try {
			HttpResponse<String> res = req.asString();
			if (res.getStatus() == 201) {
//				System.out.println(res.getBody());
				return true;
			} else {
				logger.error(res.getBody());
				logger.error("post message error: {}", msg, res.getBody());
			}
		} catch (UnirestException e) {
			logger.error("post message error: {}", msg, e);
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

	public String getProducerId() {
		return producerId;
	}

	public void setProducerId(String producerId) {
		this.producerId = producerId;
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
