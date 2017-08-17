package com.fjs.cronus.service.client;

import com.fjs.cronus.config.FeignClientConfig;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 图文识别接口
 * Created by Administrator on 2017/8/17 0017. url = "http://192.168.1.124:1120",
 */
@FeignClient(value = "${client.feign.talos-backend}", configuration = FeignClientConfig.class)
public interface TalosService {





}
