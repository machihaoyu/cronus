package com.fjs.cronus.service.client;

import com.fjs.cronus.config.FeignClientConfig;
import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.dto.uc.PhpQueryResultDTO;
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

    /**
     * 根据信息查到用户信息
     * @see /swagger-ui.html#!/php-api-user-controller/getUserListByRolesUsingPOST
     * @param token 认证信息
     * @param role_id 角色id
     * @param department_id 部门编号
     * @param sub_company_id 分公司Id
     * @return
     */
    @RequestMapping(value = "/api/v1/getUserListByRoles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    PhpQueryResultDTO getUserListByRoles(@RequestHeader("Authorization") String token,
                                         @RequestParam(value = "role_id",defaultValue = "0") Integer role_id,
                                         @RequestParam(value = "department_id",defaultValue = "0") Integer department_id,
                                         @RequestParam(value = "sub_company_id",defaultValue = "0") Integer sub_company_id);


    /**
     * 根据信息查到用户信息
     * @see /swagger-ui.html#!/php-api-user-controller/getUserInfoByIdsUsingPOST
     * @param token
     * @param user_ids
     * @param department_ids
     * @param sub_company_id
     * @param flag
     * @param page
     * @param size
     * @param name
     * @return
     */
    @RequestMapping(value = "/api/v1/getUserInfoByIds", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    PhpQueryResultDTO getUserInfoByIds(@RequestHeader("Authorization") String token,
                                       @RequestParam(value = "user_ids",required = false) String user_ids,
                                       @RequestParam(value = "department_ids", required = false)String department_ids,
                                       @RequestParam(value = "sub_company_id", required = false) Integer sub_company_id,
                                       @RequestParam(value = "flag", required = false) String flag,
                                       @RequestParam(value = "page", required = false) Integer page,
                                       @RequestParam(value = "size", required = false) Integer size,
                                       @RequestParam(value = "name", required = false)String name);
}
