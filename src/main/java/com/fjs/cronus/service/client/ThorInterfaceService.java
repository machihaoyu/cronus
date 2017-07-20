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
     * @see  /swagger-ui.html#!/php-api-user-controller/checkMobileUsingPOST
     * @param token 认证信息
     * @param phone 手机号
     * @return
     */
    @RequestMapping(value = "/api/v1/checkMobile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO postUCByCheckMobile(@RequestHeader("Authorization") String token, @RequestParam(value = "phone") String phone);


    /**
     * 得到员工姓名
     * @see /swagger-ui.html#!/php-api-user-controller/getUserNamesUsingPOST
     * @param token 认证信息
     * @param user_ids 用户编号，逗号隔开
     * @return
     */
    @RequestMapping(value = "/api/v1/getUserNames", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO getUserNames(@RequestHeader("Authorization") String token, @RequestParam(value = "user_ids") String user_ids);
}
