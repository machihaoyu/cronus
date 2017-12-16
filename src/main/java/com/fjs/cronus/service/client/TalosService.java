package com.fjs.cronus.service.client;

import com.fjs.cronus.config.FeignClientConfig;
import com.fjs.cronus.dto.ocr.ReqParamDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 图文识别接口
 * Created by Administrator on 2017/8/17 0017. url = "http://192.168.1.124:1120",
 */
@FeignClient(value = "${client.feign.talos-backend}", url = "http://192.168.1.124:1200",configuration = FeignClientConfig.class)
public interface TalosService {

    @RequestMapping(value = "/talos/api/v1/ocrService", method = RequestMethod.POST)
    void ocrService(@RequestBody ReqParamDTO reqParamDTO, @RequestHeader(name = "Authorization") String token);

}
