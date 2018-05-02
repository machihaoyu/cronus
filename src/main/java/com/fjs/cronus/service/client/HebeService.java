package com.fjs.cronus.service.client;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.config.FeignClientConfig;
import com.fjs.cronus.dto.api.CommonApiDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;


/**
 * 短信接口
 * Created by Administrator on 2017/7/19 0019. url = "http://192.168.1.124:1050",
 */
//@FeignClient(value = "${client.feign.thor-backend}", url = "http://192.168.1.128:1050")
@FeignClient(value = "${client.feign.hebe-backend}", configuration = FeignClientConfig.class)
public interface HebeService {

    @RequestMapping(value = "/v1/sendMessage",method = RequestMethod.POST)
    @ResponseBody
    CommonApiDTO sendMessage(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObj);
}

