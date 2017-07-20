package com.fjs.cronus.api;

import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.dto.uc.PhpQueryResultDTO;
import com.fjs.cronus.service.client.ThorInterfaceService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2017/7/19 0019.
 */
@RestController
@RequestMapping("/uc")
public class ThorController {

    public final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ThorInterfaceService thorInterfaceService;


    @ApiOperation(value="验证手机号是否被注册接口", notes="验证手机号是否被注册接口API")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
        @ApiImplicitParam(name = "phone", value = "手机号码", required = true, paramType = "query",  dataType = "string")
    })
    @RequestMapping(value = "/api/v1/checkMobile", method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO checkMobile(@RequestHeader String Authorization, @RequestParam String phone) {
        try {
            BaseUcDTO baseUcDTO = thorInterfaceService.postUCByCheckMobile(Authorization, phone);
            return baseUcDTO;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new BaseUcDTO(9000,  e.getMessage(), null);
        }
    }



    @ApiOperation(value="根据用户id集合得到对应员工姓名JSON接口", notes="根据用户id集合得到对应员工姓名JSON接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "user_ids", value = "用户编号，逗号隔开", required = true, paramType = "query",  dataType = "string")
    })
    @RequestMapping(value = "/api/v1/getUserNames", method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO getUserNames(@RequestHeader String Authorization, @RequestParam String user_ids) {
        try {
            BaseUcDTO baseUcDTO = thorInterfaceService.getUserNames(Authorization, user_ids);
            return baseUcDTO;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new BaseUcDTO(9000,  e.getMessage(), null);
        }
    }


    @ApiOperation(value="根据信息查到用户信息接口", notes="根据信息查到用户信息接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "department_id", value = "部门编号", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "sub_company_id", value = "分公司Id", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "role_id", value = "角色id", required = true, paramType = "query", dataType = "Integer"),
    })
    @RequestMapping(value = "/api/v1/getUserListByRoles",method = RequestMethod.POST)
    @ResponseBody
    public PhpQueryResultDTO getUserListByRoles (@RequestHeader String Authorization,
                                                 @RequestParam(value = "role_id",defaultValue = "0") Integer role_id,
                                                 @RequestParam(value = "department_id",defaultValue = "0") Integer department_id,
                                                 @RequestParam(value = "sub_company_id",defaultValue = "0") Integer sub_company_id) {
        try{
            return thorInterfaceService.getUserListByRoles(Authorization, role_id, department_id, sub_company_id);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return PhpQueryResultDTO.getExcetion(9000, e.getMessage());
        }
    }



    @ApiOperation(value="根据信息查到用户id集合查询用户信息接口", notes="根据信息查到用户id集合查询用户信息接口API")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "user_ids", value = "用户编号", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "department_ids", value = "部门编号", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "sub_company_id", value = "分公司编号编号", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "flag", value = "eqh或者neq", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页数", required = false, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页显示", required = false, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "name", value = "用户姓名", required = false, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/api/v1/getUserInfoByIds",method = RequestMethod.POST)
    @ResponseBody
    public PhpQueryResultDTO getUserInfoByIds (@RequestHeader String Authorization,
                                               @RequestParam(required = false) String user_ids,
                                               @RequestParam(required = false)String department_ids,
                                               @RequestParam(required = false) Integer sub_company_id,
                                               @RequestParam(required = false) String flag,
                                               @RequestParam(required = false) Integer page,
                                               @RequestParam(required = false) Integer size,
                                               @RequestParam(required = false)String name){
        try{
            return thorInterfaceService.getUserInfoByIds(Authorization, user_ids,department_ids,sub_company_id,flag,page,size,name);

        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return PhpQueryResultDTO.getExcetion(9000, e.getMessage());
        }
    }


}
