package com.fjs.cronus.service.client;

import com.fjs.cronus.config.FeignClientConfig;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.SimpleUserInfoDTO;
import com.fjs.cronus.dto.uc.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * UC用户中心接口
 * Created by Administrator on 2017/7/19 0019. url = "http://192.168.1.124:1120",
 */
@FeignClient(value = "${client.feign.thor-backend}",url = "http://192.168.1.124:1120",  configuration = FeignClientConfig.class)
public interface ThorInterfaceService {


    /**
     * 登录系统带用户情报
     * @param username 用户名
     * @param password 密码
     * @param system 系统名
     */
    @RequestMapping(value = "/loginWithUserInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)//TODO uc并未指派请求类型，暂时用post请求
    PhpLoginDTO loginWithUserInfo(@RequestParam(value = "username") String username,
                                  @RequestParam(value = "password") String password,
                                  @RequestParam(value = "system") String system);


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
     * 检查是否是业务员
     * @param token 认证信息
     * @param id 编号
     */
    @RequestMapping(value = "/api/v1/checkIfSaler", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Map<String ,Object> postUcForCheckedSaler(@RequestHeader("Authorization") String token, @RequestParam(value = "id") Integer id);

    /**
     * 验证用户权限
     * @param url url地址
     * @param user_id
     */
    @RequestMapping(value = "/api/v1/checkUserAuthority", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO checkUserAuthority(@RequestParam(value = "url") String url, @RequestParam(value = "user_id") Integer user_id);

    /**
     * 修改密码
     * @param token 认证信息
     * @param user_id 用户编号
     * @param old_pw 原密码
     * @param new_pw 新密码
     * @param ok_pw 第二次输入新密码
     */
    @RequestMapping(value = "/api/v1/editPassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO editPassword(@RequestHeader("Authorization") String token, @RequestParam(value = "user_id") Integer user_id, @RequestParam(value = "old_pw") String old_pw, @RequestParam(value = "new_pw") String new_pw,
                           @RequestParam(value = "ok_pw") String ok_pw);

    /**
     * 编辑用户信息
     * @param token 认证信息
     * @param user_id 用户编号
     * @param sex 性别
     * @param email 邮箱
     * @param telephone 电话号码
     * @param address 地址
     */
    @RequestMapping(value = "/api/v1/editUserInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO editUserInfo(@RequestHeader("Authorization") String token, @RequestParam(value = "user_id") Integer user_id, @RequestParam(value = "sex") String sex, @RequestParam(value = "email") String email,
                           @RequestParam(value = "telephone") String telephone, @RequestParam(value = "address") String address);


    /**
     * 得到所有分公司业务员操作
     * @param token 认证信息
     * @param city 城市
     */
    @RequestMapping(value = "/api/v1/getAllSalesman", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO getAllSalesman(@RequestHeader("Authorization") String token, @RequestParam(value = "city") String city);

    /**
     * 得到所有业务员不包括userId
     * @param token 认证信息
     * @param user_id 用户编号
     */
    @RequestMapping(value = "/api/v1/getAllUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    AllUserDTO getAllUser(@RequestHeader("Authorization") String token, @RequestParam(value = "user_id") Integer user_id);

    /**
     * 得到员工所在地址
     * @param token 认证信息
     * @param user_id 用户编号
     */
    @RequestMapping(value = "/api/v1/getCityByUserid", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO getCityByUserid(@RequestHeader("Authorization") String token, @RequestParam(value = "user_id") Integer user_id);

    /**
     * 获取员工信息
     * @param token 认证信息
     * @param user_id 用户编号
     */
    @RequestMapping(value = "/api/v1/getRoleInfoByUser_id", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO getRoleInfoByUser_id(@RequestHeader("Authorization") String token, @RequestParam(value = "user_id") String user_id);

    /**
     * 查询业务员信息
     * @param token 认证信息
     * @param user_id 用户编号
     * @param name 姓名
     * @param company_id 分公司Id
     */
    @RequestMapping(value = "/api/v1/getSaleInfos", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO getSaleInfos(@RequestHeader("Authorization") String token, @RequestParam(value = "user_id") Integer user_id, @RequestParam(value = "name") String name, @RequestParam(value = "company_id") Integer company_id);

    /**
     * 根据手机号得到业务员信息
     * @param token 认证信息
     * @param phone 手机号
     */
    @RequestMapping(value = "/api/v1/getSalesmanByPhone", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO getSalesmanByPhone(@RequestHeader("Authorization") String token, @RequestParam(value = "phone") String phone);

    /**
     * 得到员工姓名
     * @see /swagger-ui.html#!/php-api-user-controller/getUserNamesUsingPOST
     * @param token 认证信息
     * @param user_ids 用户编号，逗号隔开
     * @return
     */
    @RequestMapping(value = "/api/v1/getUserNames", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO getUserNames(@RequestHeader("Authorization") String token,
                           @RequestParam(value = "user_ids") String user_ids);

    /**
     * 查询角色信息
     * @param token 认证信息
     * @param name 字段名称
     * @param value 字段值
     */
    @RequestMapping(value = "/api/v1/getRoleInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO getRoleInfo(@RequestHeader("Authorization") String token,
                          @RequestParam(value = "name") String name,
                          @RequestParam(value = "value") String value,
                          @RequestParam(value = "company_id") Integer company_id);

    /**
     * 获取用户集合
     * @param token
     * @param company_id
     * @param department_id
     * @return
     */
    @RequestMapping(value = "/api/v1/getUserIds", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO getUserIds(@RequestHeader("Authorization") String token,
                         @RequestParam(value = "company_id", required = false) Integer company_id,
                         @RequestParam(value = "department_id", required = false) Integer department_id,
                         @RequestParam(value = "role_id", required = false) Integer role_id,
                         @RequestParam(value = "status", required = false) Integer status,
                         @RequestParam(value = "sub_company_id", required = false) Integer sub_company_id);

    /**
     * 查询角色信息
     * @param token 认证信息
     * @param where
     * @param type 类型1或者2
     */
    @RequestMapping(value = "/api/v1/getRoleByWhere", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO getRoleByWhere(@RequestHeader("Authorization") String token,
                             @RequestParam(value = "where") String where,
                             @RequestParam(value = "type") Integer type);

    /**
     * 得到department列表
     * @param token 认证信息
     * @param where
     * @param type 类型1或者2
     */
    @RequestMapping(value = "/api/v1/getDepartmentByWhere", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO getDepartmentByWhere(@RequestHeader("Authorization") String token,
                                   @RequestParam(value = "where") String where,
                                   @RequestParam(value = "type") Integer type);


    /**
     * 得到下属部门Id
     * @param token 认证信息
     * @param department_id 部门编号
     */
    @RequestMapping(value = "/api/v1/getSubDepartmentId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List getSubDepartmentId(@RequestHeader("Authorization") String token,
                            @RequestParam(value = "department_id") Integer department_id);


    /**
     * 得到department列表
     * @param token 认证信息
     * @param where
     * @param type 类型1或者2
     */
    @RequestMapping(value = "/api/v1/getDepartment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO getDepartment(@RequestHeader("Authorization") String token,
                            @RequestParam(value = "where") String where,
                            @RequestParam(value = "type") Integer type);


    /**
     * 得到SubCompanys
     * @param token 认证信息
     * @param city 城市
     * @param where
     * @param type 类型1或者2
     */
    @RequestMapping(value = "/api/v1/getSubCompanys", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO getSubCompanys(@RequestHeader("Authorization") String token,
                             @RequestParam(value = "city") String city,
                             @RequestParam(value = "where") String where,
                             @RequestParam(value = "type") Integer type,
                             @RequestParam(value = "company_id") Integer company_id);


    /**
     * 得到下属部门列表
     * @param token 认证信息
     * @param department_id 部门编号
     * @param data_type 类型4：查询全部或者其他
     */
    @RequestMapping(value = "/api/v1/getSubDepartmentList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List getSubDepartmentList(@RequestHeader("Authorization") String token,
                              @RequestParam(value = "department_id") Integer department_id,
                              @RequestParam(value = "data_type") Integer data_type);



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
                                       @RequestParam(value = "name", required = false)String name,
                                       @RequestParam(value = "status", required = false) Integer status);


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
    BaseUcDTO<UserInfoDTO> getUserInfoByField(@RequestHeader("Authorization") String token,
                                 @RequestParam(value = "telephone",required = false) String telephone,
                                 @RequestParam(value = "user_id",required = false) Integer user_id,
                                 @RequestParam(value = "name",required = false) String name);


    /**
     * 得到用户到列表
     * @see /swagger-ui.html#!/php-api-user-controller/getUserInfoUsingPOST
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
     * @see /swagger-ui.html#!/php-api-user-controller/getUpTdzByRole_idUsingGET
     * @param token 认证信息
     * @return
     */
    @RequestMapping(value = "/api/v1/getUpTdzByRole_id",method = RequestMethod.GET)
    @ResponseBody
    BaseUcDTO getUpTdzByRole_id(@RequestHeader("Authorization") String token);


    /**
     * 获取团队长下用户信息成功
     * @see /swagger-ui.html#!/php-api-user-controller/getCRMUserUsingGET
     * @param token 认证信息
     * @return
     */
    @RequestMapping(value = "/api/v1/getCRMUser",method = RequestMethod.GET)
    @ResponseBody
    BaseUcDTO<List<CrmUserDTO>> getCRMUser(@RequestHeader("Authorization") String token, @RequestParam(value = "city") String city);



    /**
     * 查询此业务员的团队长
     * @see /swagger-ui.html#!/php-api-user-controller/getUpTdzUsingPOST
     * @param token
     * @param user_id
     * @return
     */
    @RequestMapping(value = "/api/v1/getUpTdz",method = RequestMethod.POST)
    UserModelDTO getUpTdz(@RequestHeader("Authorization") String token,
                                @RequestParam(value = "user_id",defaultValue ="0",required = false) Integer user_id);


    /**
     * 切换系统
     * @see /swagger-ui.html#!/php-api-user-controller/getSwitchSystemUsingPOST
     * @param token
     * @param uid
     * @param system
     * @return
     */
    @RequestMapping(value = "/api/v1/getSwitchSystem",method = RequestMethod.POST)
    SwitchSystemDTO getSwitchSystem(@RequestHeader("Authorization") String token,
                                    @RequestParam(value = "uid") Integer uid,
                                    @RequestParam(value = "system") String system);


    /** 得到下属员工
     * @see /swagger-ui.html#!/php-api-user-controller/getSubUserByUserIdUsingPOST
     * @param token
     * @param user_id
     * @param data_type
     * @return
     */
    @RequestMapping(value = "/api/v1/getSubUserByUserId",method = RequestMethod.POST)
    BaseUcDTO getSubUserByUserId(@RequestHeader("Authorization") String token,
                                 @RequestParam(value = "user_id") Integer user_id,
                                 @RequestParam(value = "system") String system);


    /**
     * 得到团队长下业务员操作
     * @see /swagger-ui.html#!/php-api-user-controller/getSubUserByTDZUsingPOST
     * @param token
     * @param user_id
     * @param department_id
     * @return
     */
    @RequestMapping(value = "/api/v1/getSubUserByTDZ",method = RequestMethod.POST)
    BaseUcDTO getSubUserByTDZ(@RequestHeader("Authorization") String token,
                              @RequestParam(value = "user_id", defaultValue = "0", required = false) Integer user_id,
                              @RequestParam(value = "department_id", defaultValue = "0", required = false)Integer department_id);


    /**
     * 获取所有业务员的信息（包括团队长）
     * @param token 认证信息
     */
    @RequestMapping(value = "/api/v1/getUpYwyByRole_id",method = RequestMethod.GET)
    BaseUcDTO getUpYwyByRole_id(@RequestHeader("Authorization") String token);

    /**
     * 通过token获取用户基本信息
     * @param Authorization
     * @param systemName
     * @return
     */
    @RequestMapping(value = "/api/v1/getCurrentUserInfo",method = RequestMethod.GET)
    String getCurrentUserInfo(@RequestHeader("Authorization") String Authorization,
                              @RequestParam(value = "systemName") String systemName);

    /**
     * 根据userId获取用户的基本信息
     *
     * @param token
     * @param userId
     * @return
     */
    @RequestMapping(value = "/api/v1/getSystemUserInfo", method = RequestMethod.GET)
    CronusDto<SimpleUserInfoDTO> getUserInfoById(@RequestHeader("Authorization") String token, @RequestParam(value = "userId") Integer userId);
}

