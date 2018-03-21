package com.fjs.cronus.util;


import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.model.CustomerInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * http连接池
 * Created by msi on 2017/12/11.
 */
public class MultiThreadedHttpConnection {
	private static final Logger logger = LoggerFactory.getLogger(MultiThreadedHttpConnection.class);
	private static final String contextType = "text/html,application/xml;charset=UTF-8";
	private static final String contextTypeJson = "application/json;charset=UTF-8";

	private static PoolingHttpClientConnectionManager httpPool = new PoolingHttpClientConnectionManager();
	private static int maxTotal = 200;
	private static int maxPerRout = 50;
	private static int connecttimeOut = 10000;
	private static int readTimeOut = 60000;
	private static MultiThreadedHttpConnection mthc = new MultiThreadedHttpConnection();
	private static DefaultHttpRequestRetryHandler handler = new DefaultHttpRequestRetryHandler(1, false);
	private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeOut).setConnectTimeout(connecttimeOut)
			.build(); // 设置请求和传输超时时间

	static {
		httpPool.setMaxTotal(maxTotal);// 连接池最大并发连接数
		httpPool.setDefaultMaxPerRoute(maxPerRout);// 单路由最大并发数
	}

	private MultiThreadedHttpConnection() {

	}

	public static MultiThreadedHttpConnection getInstance() {
		return mthc;
	}


	public CronusDto  sendDataByPost(String url, String param) {
		CronusDto result = new CronusDto();
		if (param == null || url == null) {
			result.setData("请求参数缺失");
			return result;
		}

		Integer statusCode = -1;
		HttpPost post;
		try {
			post = new HttpPost(url.trim());
		} catch (Exception e) {
			logger.error("url error " + url, e.getMessage());
			result.setResult(ResultResource.CODE_OTHER_ERROR);
			result.setMessage("url error " + url);
			return result;
		}
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool).setRetryHandler(handler).build();
		logger.warn("post request: " + url + param);
		post.setConfig(requestConfig);

		CloseableHttpResponse response = null;

		try {
			if (param != null)
				post.setEntity(new StringEntity(param));
			post.setHeader("Accept", contextType);
			response = httpClient.execute(post);
			statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				logger.error("connect " + url + " error httpCode : " + statusCode);
				result.setMessage("请求失败  code : " + statusCode);
				result.setResult(statusCode);
				return result;
			}
			String resultStr = EntityUtils.toString(response.getEntity());
			logger.warn("post response : " + resultStr);
			result.setData(resultStr);
			result.setResult(ResultResource.CODE_SUCCESS);

		} catch (UnsupportedEncodingException e) {
			logger.error("sendDataByPost UnsupportedEncodingException ", e);
		} catch (IOException e) {
			checkException(result, e);
		} finally {
			closeResponse(response);
		}

		return result;
	}

	public CronusDto sendPostByMap(String url, Map<String, String> paramMap) {
		CronusDto result = new CronusDto();

		if (paramMap == null || url == null) {
			result.setData("请求参数缺失");
			return result;
		}

		Integer statusCode = -1;
		HttpPost post;

		try {
			post = new HttpPost(url.trim());
		} catch (Exception e) {
			logger.error("url error " + url, e.getMessage());
			result.setResult(ResultResource.CODE_OTHER_ERROR);
			result.setMessage("url error " + url);
			return result;
		}

		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool).setRetryHandler(handler).build();
		logger.warn("post request: " + url + "  params : " + mapToString(paramMap));
		post.setConfig(requestConfig);

		CloseableHttpResponse response = null;

		try {
			List<NameValuePair> nvps = new ArrayList<>();
			if (paramMap != null) {
				for (Map.Entry<String, String> m : paramMap.entrySet()) {
					nvps.add(new BasicNameValuePair(m.getKey(), m.getValue()));
				}
			}

			post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			post.setHeader("Accept", contextType);
			response = httpClient.execute(post);
			statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				logger.error("connect " + url + " error httpCode : " + statusCode);
				result.setMessage("请求失败  code : " + statusCode);
				result.setResult(statusCode);
				return result;
			}
			String resultStr = EntityUtils.toString(response.getEntity());
			logger.warn("post response : " + resultStr);
			result.setData(resultStr);
			result.setResult(ResultResource.CODE_SUCCESS);

		} catch (UnsupportedEncodingException e) {
			logger.error("sendDataByPost UnsupportedEncodingException ", e);
		} catch (IOException e) {
			checkException(result, e);
		} finally {
			closeResponse(response);
		}

		return result;
	}
	private void closeResponse(CloseableHttpResponse response) {
		try {
			if (response != null) {
				response.close();
			}
		} catch (IOException e) {
			logger.error("closeableHttpResponse close error", e);
		}
	}

	private void checkException(CronusDto<?> result, Exception e) {
		logger.error("IOException ", e);

		if (e instanceof SocketTimeoutException) {
			result.setResult(ResultResource.CODE_OTHER_ERROR);
			result.setMessage("请求超时");
		} else if (e instanceof SocketException) {
			String messge = e.getMessage();
			if (StringUtils.isNotEmpty(messge) && StringUtils.contains(messge, "reset")) {
				result.setResult(ResultResource.CODE_OTHER_ERROR);
				result.setMessage("请求超时");
			} else {
				result.setResult(ResultResource.CODE_OTHER_ERROR);
				result.setMessage("网络连接失败");
			}
		} else {
			result.setResult(ResultResource.CODE_OTHER_ERROR);
			result.setMessage("网络连接失败");
		}
	}


	private String mapToString(Map<String, String> paramMap) {
		if (paramMap == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer("");
		for (Map.Entry<String, String> e : paramMap.entrySet()) {
			sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
		}
		return sb.toString();
	}

	public CronusDto<String> sendDataByGetReturnString(String url) {
		CronusDto<String> result = new CronusDto<String>();
		if (url == null) {
			result.setMessage("请求参数缺失");
			return result;
		}
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpPool).setRetryHandler(handler).build();
		Integer statusCode = -1;
		logger.warn("request: " + url);
		HttpGet get = null;
		try {
			get = new HttpGet(url.trim());
		} catch (Exception e) {
			logger.error("url error " + url, e.getMessage());
			result.setResult(1);
			result.setMessage("url error " + url);
			return result;
		}
		get.setConfig(requestConfig);

		CloseableHttpResponse response = null;
		try {
			get.setHeader("Accept", contextType);
			response = httpClient.execute(get);
			statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != 200) {
				logger.error("connect " + url + " error httpCode : " + statusCode);
				result.setResult(1);
				result.setMessage(statusCode + " error");
				return result;
			}
			String resultStr = EntityUtils.toString(response.getEntity());
			logger.warn("response : " + resultStr);
			result.setData(resultStr);
			result.setResult(0);
		} catch (Exception e) {
			checkException(result, e);
		} finally {
			closeResponse(response);
		}
		return result;
	}

	public static void main(String[] args) {
		CustomerInfo customerInfo = new CustomerInfo();
		customerInfo.setTelephonenumber("");
		JSONObject jsonObject = (JSONObject)JSONObject.toJSON(customerInfo);
	    final String ocdcUrl = "https://www.haidaimf.com/interface/changeCustPhone.do";
		CronusDto<String> cronusDto = MultiThreadedHttpConnection.getInstance().sendDataByPost(ocdcUrl,jsonObject.toJSONString());
		System.out.println(cronusDto.getData());

	}
	
}
