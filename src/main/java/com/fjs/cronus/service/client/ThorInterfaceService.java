package com.fjs.cronus.service.client;

import com.fjs.cronus.config.FeignClientConfig;
import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.dto.uc.PhpQueryResultDTO;
import com.fjs.cronus.dto.uc.UserModelDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @param token 认证信息
     * @param user_ids 用户编号
     * @param department_ids 部门编号
     * @param sub_company_id 分公司编号
     * @param flag eqh或者neq
     * @param page 页数
     * @param size 每页显示
     * @param name 用户姓名
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


    /**
     * 通过属性得到用户信息
     * @see /swagger-ui.html#!/php-api-user-controller/getUserInfoByFieldUsingPOST
     * @param token 认证信息
     * @param telephone 手机号
     * @param user_id 用户编号
     * @param name 用户姓名
     * @return
     */
    @RequestMapping(value = "/api/v1/getUserInfoByField", method = RequestMethod.POST)
    BaseUcDTO getUserInfoByField(@RequestHeader("Authorization") String token,
                                 @RequestParam(value = "telephone",required = false) String telephone,
                                 @RequestParam(value = "user_id",required = false) Integer user_id,
                                 @RequestParam(value = "name",required = false) String name);


    /**
     * 得到用户到列表
     * @param token 认证信息
     * @param page 页数
     * @param size 页码
     * @param where 条件
     * @param type  类型
     * @return
     */
    @RequestMapping(value = "/api/v1/getUserInfo",method = RequestMethod.POST)
    @ResponseBody
    PhpQueryResultDTO getUserInfo(@RequestHeader("Authorization") String token,
                                  @RequestParam(value = "page",defaultValue = "0",required = false) Integer page,
                                  @RequestParam(value = "size",defaultValue = "10",required = false) Integer size,
                                  @RequestParam(value = "where",required = false,defaultValue = "") String where,
                                  @RequestParam(value="type",required = true,defaultValue = "1") Integer type);


    /**
     * 获取团队长信息
     * @param token 认证信息
     * @return
     */
    @RequestMapping(value = "/api/v1/getUpTdzByRole_id",method = RequestMethod.GET)
    @ResponseBody
    BaseUcDTO getUpTdzByRole_id(@RequestHeader("Authorization") String token);



    /**
     * 查询此业务员的团队长
     * @param token
     * @param user_id
     * @return
     */
    @RequestMapping(value = "/api/v1/getUpTdz",method = RequestMethod.POST)
    List<UserModelDTO> getUpTdz(@RequestHeader("Authorization") String token,
                                @RequestParam(value = "user_id",defaultValue ="0",required = false) Integer user_id);

}


