package com.fjs.cronus.service.client;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.config.FeignClientConfig;
import com.fjs.cronus.dto.avatar.AvatarApiDTO;
import com.fjs.cronus.dto.avatar.FirstBarDTO;
import com.fjs.cronus.dto.avatar.OrderNumberDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "${client.feign.avatar-backend}", configuration = FeignClientConfig.class)
public interface AvatarClientService {

    @RequestMapping(value = "/fistBar/v1/getAllFirstBar", method = RequestMethod.GET)
    AvatarApiDTO<List<FirstBarDTO>> findAllSubCompany(@RequestHeader("Authorization") String token);


    @RequestMapping(value = "/order/v1/queryOrderNumber", method = RequestMethod.POST)
    AvatarApiDTO<OrderNumberDTO> queryOrderNumber(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject);

}
