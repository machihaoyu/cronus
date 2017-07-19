package com.fjs.cronus.service.client;

import com.fjs.cronus.config.FeignClientConfig;
import com.fjs.cronus.dto.uc.BaseUcDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * UC用户中心接口
 * Created by Administrator on 2017/7/19 0019.
 */
@FeignClient(value = "thor-backend-dev", configuration = FeignClientConfig.class)  //TODO 换成配置
public interface ThorInterfaceService {


    /**
     * 验证手机号是否被注册
     * @return
     */
    @RequestMapping(value = "/api/v1/checkMobile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO postUCByCheckMobile(@RequestHeader("Authorization") String token, @RequestParam(value = "phone") String phone);


}
