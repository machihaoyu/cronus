package com.fjs.cronus.service.client;

import com.fjs.cronus.config.FeignClientConfig;
import com.fjs.cronus.dto.avatar.AvatarApiDTO;
import com.fjs.cronus.dto.avatar.FirstBarDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "${client.feign.avatar-backend}", configuration = FeignClientConfig.class)
public interface AvatarClientService {

    @RequestMapping(value = "/channel/v1/getInfoByChannelN", method = RequestMethod.POST)
    AvatarApiDTO<List<FirstBarDTO>> findAllSubCompany(@RequestHeader("Authorization") String token);

}
