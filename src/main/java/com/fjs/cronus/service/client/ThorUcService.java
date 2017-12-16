package com.fjs.cronus.service.client;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.api.PhpApiDto;
import com.fjs.cronus.dto.api.*;
import com.fjs.cronus.dto.api.uc.*;
import com.fjs.cronus.dto.api.uc.SubCompanyCityDto;
import com.fjs.cronus.dto.uc.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6 0006. url = "http://192.168.1.124:1120",
 */
@FeignClient(value = "${client.feign.thor-backend}")
public interface ThorUcService {

    /**
     * UC登录接口
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/loginSystem", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ThorApiDTO loginByUC(@RequestBody JSONObject param);

    /**
     * 根据登录成功后返回的token，获取用户信息 
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/api/v1/getCurrentUserInfo", method = RequestMethod.GET)
    ThorApiDTO<UserInfoDTO> getUserInfoByToken(@RequestHeader("Authorization") String token, @RequestParam(value = "systemName") String systemName);


    /**
     * 当前用户是否是房金所员工, 0 本公司，1 合作渠道
     * @param token
     * @return
     */
    @RequestMapping(value = "/api/v1/checkUserCompany", method = RequestMethod.GET)
    ThorApiDTO<String> checkUserCompany(@RequestHeader("Authorization") String token);

    /**
     * 得到员工所在城市
     * @param token
     * @return
     */
    @RequestMapping(value = "/api/v1/getCityByUserid", method = RequestMethod.POST)
    ThorApiDTO<String> getCityByUserid(@RequestHeader("Authorization") String token, @RequestParam("user_id") Integer userId);



    /**
     * 根据userId获取用户的基本信息
     *
     * @param token
     * @param userId
     * @return
     */
    @RequestMapping(value = "/api/v1/getSystemUserInfo", method = RequestMethod.GET)
    ThorApiDTO<SimpleUserInfoDTO> getUserInfoById(@RequestHeader("Authorization") String token, @RequestParam(value = "userId") Integer userId);


    @RequestMapping(value = "/api/v1/getSystemUserInfo", method = RequestMethod.GET)
    ThorApiDTO<SimpleUserInfoDTO> getUserInfoByIdStr(@RequestHeader("Authorization") String token, @RequestParam(value = "userId") Integer userId);
    /**
     * 根据系统名获取有权限的模块名
     *
     * @param token
     * @param systemName
     * @return
     */
    @RequestMapping(value = "/api/v1/getModuleAuthBySystemName", method = RequestMethod.GET)
    String getModuleAuthBySystemName(@RequestHeader("Authorization") String token, @RequestParam(value = "systemName") String systemName);

    /**
     * 根据模块名获取有权限的方法名
     *
     * @param token
     * @param moduleName
     * @return
     */
    @RequestMapping(value = "/api/v1/getActionAuthByModuleName", method = RequestMethod.GET)
    String getActionAuthByModuleName(@RequestHeader("Authorization") String token, @RequestParam(value = "moduleName") String moduleName);


    /**
     * 获取分公司以及其下的团队列表
     */
    @RequestMapping(value = "/v1/getLinkSubTeams", method = RequestMethod.GET)
    String getcompanyTeams(@RequestHeader("Authorization") String token);


    /**
     * 获取业务员列表
     */
    @RequestMapping(value = "/api/v1/getUserInfoByIds", method = RequestMethod.POST)
    ThorQueryDto<List<PHPUserDto>> getUserInfoByIds(@RequestHeader("Authorization") String token, @RequestParam(value = "user_ids") String user_ids,
                                                    @RequestParam(value = "department_ids") String department_ids,
                                                    @RequestParam(value = "sub_company_id") Integer sub_company_id,
                                                    @RequestParam(value = "flag") String flag,
                                                    @RequestParam(value = "page") Integer page,
                                                    @RequestParam(value = "size") Integer size,
                                                    @RequestParam(value = "name") String name,
                                                    @RequestParam(value = "status") Integer status);
    /**
     * 数据权限
     *
     * @param token
     * @param systemName
     * @return
     */
    @RequestMapping(value = "/api/v1/getDataTypeAuthBySystem", method = RequestMethod.GET)
    String getDataTypeAuthBySystem(@RequestHeader("Authorization") String token, @RequestParam(value = "systemName") String systemName);

    /**
     * 获得团队列表
     *
     * @param token
     * @param subCompanyId
     * @return
     */
    @RequestMapping(value = "/api/v1/getSubTeamList", method = RequestMethod.GET)
    String getSubTeamList(@RequestHeader("Authorization") String token, @RequestParam(value = "sub_company_id") Integer subCompanyId);

    /**
     * 根据token返回个人ID,团队ID,分公司ID
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/api/v1/getUserTeamInfo", method = RequestMethod.GET)
    String getAllIdsoByToken(@RequestHeader("Authorization") String token);

    /**
     * 得到下属员工
     * @param token
     * @param userId
     * @param system
     *
     * @return
     */
    @RequestMapping(value = "/api/v1/getSubUserByUserId", method = RequestMethod.POST)
    PhpApiDto<List<String>> getSubUserByUserId(@RequestHeader("Authorization") String token, @RequestParam("user_id") Integer userId, @RequestParam(value = "system") String system, @RequestParam(value = "data_type") Integer dataType);

    /**
     * 通过属性得到用户信息
     * @param telephone
     * @param token
     * @param userId
     * @param name
     * @return
     */
    @RequestMapping(value = "/api/v1/getUserInfoByField", method = RequestMethod.POST)
    PhpApiDto<AppUserDto> getUserInfoByField(@RequestParam("telephone") String telephone, @RequestHeader("Authorization") String token, @RequestParam("user_id") Integer userId, @RequestParam("name") String name);

    /**
     * 获取用户所有信息
     * @param token
     * @param system
     * @return
     */
    @RequestMapping(value = "/api/v1/getAllUserInfo",method = RequestMethod.GET)
    public PHPLoginDto getAllUserInfo(@RequestHeader("Authorization") String token, @RequestParam("system") String system);

    /**
     * 获取部门信息
     * @param token
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/api/v1/getDepartmentTheaByWhere",method = RequestMethod.POST)
    public PhpApiDto getDepartmentTheaByWhere(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject);

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
                         @RequestParam(value = "sub_company_id", required = false) Integer sub_company_id,
                         @RequestParam(value = "sub_company_ids", required = false) String sub_company_ids);

    /**
     * 得到所有分公司业务员操作
     * @param token 认证信息
     * @param city 城市
     */
    @RequestMapping(value = "/api/v1/getAllSalesman", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    BaseUcDTO<List<Integer>> getAllSalesman(@RequestHeader("Authorization") String token, @RequestParam(value = "city") String city);

    /**
     * 获取所有能操作的公司
     * @param token
     * @return
     */
    @RequestMapping(value = "/api/v1/listAllEnableCompany", method = RequestMethod.GET)
    ThorApiDTO<List<CompanyDto>> listAllEnableCompany(@RequestHeader("Authorization") String token);

    /**
     * 根据id获取公司信息
     * @param token
     * @param companyId
     * @return
     */
    @RequestMapping(value = "/api/v1/editCompany", method = RequestMethod.GET)
    ThorApiDTO<CompanyDto> editCompany(@RequestHeader("Authorization") String token, @RequestParam(value = "companyId") Integer companyId);

    /**
     * 获取分公司
     * @param token
     * @param where
     * @param type
     * @param city
     * @param company_id
     * @return
     */
    @RequestMapping(value = "/api/v1/getSubCompanys",method = RequestMethod.POST)
    PhpApiDto<List<PhpDepartmentModel>> getSubCompanys(@RequestHeader("Authorization") String token,
                                                       @RequestParam(value = "where") String where,
                                                       @RequestParam(value = "type") Integer type,
                                                       @RequestParam(value = "city") String city,
                                                       @RequestParam(value = "company_id") Integer company_id);


    /**
     * 根据公司id获取id,name
     * @param token
     * @param companyId
     * @return
     */
    @RequestMapping(value = "/api/v1/selectCompanyById", method = RequestMethod.GET)
    ThorApiDTO<CompanyTheaSystemDto>  selectCompanyById(@RequestHeader("Authorization") String token, @RequestParam(value = "companyId") Integer companyId);

    /**
     * \根据用户id获取下属所有总公司信息
     * @param token
     * @param userId
     * @return
     */
    @RequestMapping(value = "/api/v1/getAllCompanyByUserId", method = RequestMethod.GET)
    PhpApiDto<List<SubCompanyDto>> getAllCompanyByUserId(@RequestHeader("Authorization") String token, @RequestParam(value = "userId") Integer userId,
                                                         @RequestParam(value = "systemName") String systemName);

    /**
     * 根据用户id获取下属所在的城市名称
     * @param token
     * @param userId
     * @return
     */
    @RequestMapping(value = "/api/v1/getSubcompanyByUserId",method = RequestMethod.GET)
    PhpApiDto<List<CityDto>> getSubcompanyByUserId(@RequestHeader("Authorization") String token, @RequestParam(value = "userId") Integer userId,
                                                   @RequestParam(value = "systemName") String systemName);

    /**
     * 根据城市获取所在的分公司
     * @param token
     * @param citys
     * @return
     */
    @RequestMapping(value = "/api/v1/getSubCompanyByCitys",method = RequestMethod.GET)
    /*PhpApiDto<List<CrmCitySubCompanyDto>> getSubCompanyByCitys(@RequestHeader("Authorization") String token, @RequestParam(value = "citys") String citys);*/
    ThorApiDTO<List<CrmCitySubCompanyDto>> getSubCompanyByCitys(@RequestHeader("Authorization") String token, @RequestParam(value = "citys") String citys);

    /**
     * 根据用户id获取下属所有分公司信息
     * @param token
     * @param userId
     * @param systemName
     * @return
     */
    @RequestMapping(value = "/api/v1/getAllSubCompanyByUserId", method = RequestMethod.GET)
    PhpApiDto<List<SubCompanyCityDto>> getAllSubCompanyByUserId(@RequestHeader("Authorization") String token, @RequestParam(value = "userId") Integer userId,
                                                                @RequestParam(value = "systemName") String systemName);

    /**
     * 根据角色查到用户信息
     * @param token
     * @param role_id
     * @param department_id
     * @param sub_company_id
     * @return
     */
    @RequestMapping(value = "/api/v1/getUserListByRoles", method = RequestMethod.POST)
    PhpQueryResultDto<List<PHPUserDto>> getUserListByRoles(@RequestHeader("Authorization") String token, @RequestParam(value = "role_id") Integer role_id, @RequestParam(value = "department_id") Integer department_id,
                                                           @RequestParam(value = "sub_company_id", required = false) Integer sub_company_id);

    /**
     * 根据company_id查找角色
     * @param token
     * @param name
     * @param value
     * @param company_id
     * @return
     */
    @RequestMapping(value = "/api/v1/getRoleInfo", method = RequestMethod.POST)
    BaseUcDTO<RoleDTO> getRoleInfo(@RequestHeader("Authorization") String token, @RequestParam(value = "name") String name, @RequestParam(value = "value") String value,
                                   @RequestParam(value = "company_id", required = false) Integer company_id);

    /**
     * 根据用户id获取角色名称
     * @param token
     * @param user_id
     * @return
     */
    @RequestMapping(value = "/api/v1/getRoleInfoByUser_id",method = RequestMethod.POST)
    public PhpApiDto<List> getRoleInfoByUser_id(@RequestHeader("Authorization") String token,
                                                @RequestParam(value = "user_id") String user_id);
}
