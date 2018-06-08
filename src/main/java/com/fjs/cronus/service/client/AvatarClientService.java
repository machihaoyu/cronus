package com.fjs.cronus.service.client;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.config.FeignClientConfig;
import com.fjs.cronus.dto.avatar.AvatarApiDTO;
import com.fjs.cronus.dto.avatar.FirstBarDTO;
import com.fjs.cronus.dto.avatar.OrderNumberDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "${client.feign.avatar-backend}")
public interface AvatarClientService {

    /**
     * 获取所有一级吧.
     */
    @RequestMapping(value = "/fistBar/v1/getAllFirstBar", method = RequestMethod.GET)
    AvatarApiDTO<List<FirstBarDTO>> findAllSubCompany(@RequestHeader("Authorization") String token);

    /**
     * 获取订购数.
     */
    @RequestMapping(value = "/order/v1/queryOrderNumber", method = RequestMethod.POST)
    AvatarApiDTO<OrderNumberDTO> queryOrderNumber(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject);

    /**
     * 告诉商机系统发送短信.
     */
    @RequestMapping(value = "/purchase/v1/purchaseSmsNotice", method = RequestMethod.POST)
    AvatarApiDTO<Object> purchaseSmsNotice(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject);

    /**
     * 获取人id（会发短信给他）
     */
    @RequestMapping(value = "/fistBar/v1/getUserId", method = RequestMethod.POST)
    AvatarApiDTO<Object> getUserId(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject);
}
