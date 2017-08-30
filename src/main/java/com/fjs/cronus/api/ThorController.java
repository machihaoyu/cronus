package com.fjs.cronus.api;

import com.fjs.cronus.dto.ocr.ReqParamDTO;
import com.fjs.cronus.dto.uc.*;
import com.fjs.cronus.service.client.TalosService;
import com.fjs.cronus.service.client.ThorInterfaceService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19 0019.
 */
@RestController
@RequestMapping("/uc")
public class ThorController {

    public final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ThorInterfaceService thorInterfaceService;


    @Autowired
    private TalosService talosService;//TODO 删除;

    /**
     * 调用第三方图文识别
     * @param token 认证信息;
     * @param reqParamDTO 请求参数DTO;
     */
    @ApiOperation(value="OCR识别接口", notes="OCR识别接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", defaultValue = "Bearer ", required = true, paramType = "header", dataType = "string"),
            @ApiImplicitParam(name = "reqParamDTO", value = "", required = true, paramType = "body", dataType = "ReqParamDTO")
    })
    @RequestMapping(value = "/api/v1/ocrService", method = RequestMethod.POST)
    public void ocrService(@RequestHeader(name = "Authorization") String token, @RequestBody ReqParamDTO reqParamDTO){
        ReqParamDTO reqParamDTO1 = new ReqParamDTO();
        BeanUtils.copyProperties(reqParamDTO, reqParamDTO1);
        reqParamDTO1.setImgBase64(null);
        LOGGER.warn("OCR识别接口_OcrController_ocrService : token = " +  token + ", reqParamDTO = " + ReflectionToStringBuilder.toString(reqParamDTO1));
        talosService.ocrService(reqParamDTO, token);
    }
    //TODO 删除;


    @ApiOperation(value="登录系统带用户情报接口", notes="登录系统带用户情报接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "system", value = "系统名", required = true, paramType = "query",  dataType = "string")
    })
    @RequestMapping(value = "/loginWithUserInfo")
    @ResponseBody
    public Object loginWithUserInfo(@RequestParam String username, @RequestParam String password, @RequestParam String system) {
        long l = System.currentTimeMillis();
        try {
            LOGGER.warn("33333(UC登录):");
            PhpLoginDTO phpLoginDTO = thorInterfaceService.loginWithUserInfo(username, password, system);
            LOGGER.warn("44444(UC登录):" + (System.currentTimeMillis() - l));
            return phpLoginDTO;
        } catch (Exception e) {
            LOGGER.warn("44444(UC登录):" + (System.currentTimeMillis() - l));
            LOGGER.error(e.getMessage(), e);
            return new BaseUcDTO(9000,  e.getMessage(), null);
        }
    }


    @ApiOperation(value="验证手机号是否被注册接口", notes="验证手机号是否被注册接口API")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
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
        @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
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

    @ApiOperation(value = "检查是否是业务员接口",notes = "检查是否是业务员接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "id", value = "编号", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/api/v1/checkIfSaler",method = RequestMethod.POST)
    @ResponseBody
    public Object checkIfSaler(@RequestHeader String Authorization,@RequestParam Integer id){
        try{
            return thorInterfaceService.postUcForCheckedSaler(Authorization, id);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }

    @ApiOperation(value = "验证用户权限接口", notes = "验证用户权限接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "url地址", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "user_id", value = "user_id", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/api/v1/checkUserAuthority", method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO checkUserAuthority(@RequestParam String url, @RequestParam Integer user_id){
        try{
            BaseUcDTO baseUcDTO = thorInterfaceService.checkUserAuthority(url, user_id);
            return baseUcDTO;
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }

    @ApiOperation(value = "修改密码接口", notes = "修改密码接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "user_id", value = "用户编号", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "old_pw", value = "原密码", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "new_pw", value = "新密码", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "ok_pw", value = "第二次输入新密码", required = true, paramType = "query", dataType = "string")
    })
    @RequestMapping(value = "/api/v1/editPassword", method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO editPassword(@RequestHeader String Authorization, @RequestParam Integer user_id, @RequestParam String old_pw, @RequestParam String new_pw, @RequestParam String ok_pw){
        try{
            BaseUcDTO baseUcDTO = thorInterfaceService.editPassword( Authorization, user_id, old_pw, new_pw, ok_pw);
            return baseUcDTO;
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }

    @ApiOperation(value = "编辑用户信息接口", notes = "编辑用户信息接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "user_id", value = "用户编号", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "sex", value = "性别", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "telephone", value = "电话号码", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "address", value = "地址", required = false, paramType = "query", dataType = "string")
    })
    @RequestMapping(value = "/api/v1/editUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO editUserInfo(@RequestHeader String Authorization,
                                  @RequestParam Integer user_id,
                                  @RequestParam(required = false) String sex,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(required = false) String telephone,
                                  @RequestParam(required = false) String address){
        try{
            BaseUcDTO baseUcDTO = thorInterfaceService.editUserInfo( Authorization, user_id, sex, email, telephone, address);
            return baseUcDTO;
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }

    @ApiOperation(value = "得到所有分公司业务员操作接口", notes = "得到所有分公司业务员操作接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "city", value = "例如city=上海市", required = false, paramType = "query", dataType = "string")
    })
    @RequestMapping(value = "/api/v1/getAllSalesman", method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO getAllSalesman(@RequestHeader String Authorization, @RequestParam(required = false) String city){
        try{
            BaseUcDTO baseUcDTO = thorInterfaceService.getAllSalesman( Authorization, city);
            return baseUcDTO;
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }

    @ApiOperation(value = "得到所有业务员不包括userId接口", notes = "得到所有业务员不包括userId接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "user_id", value = "用户编号", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/api/v1/getAllUser", method = RequestMethod.POST)
    @ResponseBody
    public Object getAllUser(@RequestHeader String Authorization, @RequestParam Integer user_id){
        try{
            AllUserDTO allUserDTO = thorInterfaceService.getAllUser( Authorization, user_id);
            return allUserDTO;
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }

    @ApiOperation(value = "得到员工所在地址接口", notes = "得到员工所在地址接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "user_id", value = "用户编号", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/api/v1/getCityByUserid", method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO getCityByUserid(@RequestHeader String Authorization, @RequestParam Integer user_id){
        try{
            BaseUcDTO baseUcDTO = thorInterfaceService.getCityByUserid( Authorization, user_id);
            return baseUcDTO;
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }

    @ApiOperation(value = "获取员工信息接口", notes = "获取员工信息接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "user_id", value = "用户编号", required = false, paramType = "query", dataType = "string")
    })
    @RequestMapping(value = "/api/v1/getRoleInfoByUser_id", method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO getRoleInfoByUser_id(@RequestHeader String Authorization,
                                          @RequestParam(required = false) String user_id){
        try{
            BaseUcDTO baseUcDTO = thorInterfaceService.getRoleInfoByUser_id( Authorization, user_id);
            return baseUcDTO;
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }

    @ApiOperation(value = "查询业务员信息接口", notes = "查询业务员信息接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "user_id", value = "用户编号", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "name", value = "姓名", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "company_id", value = "分公司Id", required = false, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/api/v1/getSaleInfos", method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO getSaleInfos(@RequestHeader String Authorization,
                                  @RequestParam(required = false) Integer user_id,
                                  @RequestParam(required = false) String name,
                                  @RequestParam(required = false) Integer company_id){
        try{
            BaseUcDTO baseUcDTO = thorInterfaceService.getSaleInfos( Authorization, user_id, name, company_id);
            return baseUcDTO;
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }

    @ApiOperation(value = "根据手机号得到业务员信息接口", notes = "根据手机号得到业务员信息接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query", dataType = "string")
    })
    @RequestMapping(value = "/api/v1/getSalesmanByPhone", method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO getSalesmanByPhone(@RequestHeader String Authorization, @RequestParam String phone){
        try{
            BaseUcDTO baseUcDTO = thorInterfaceService.getSalesmanByPhone( Authorization, phone);
            return baseUcDTO;
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }

    @ApiOperation(value = "查询角色信息接口", notes = "查询角色信息接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "name", value = "字段名称", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "value", value = "字段值", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "company_id", value = "总公司", required = false, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/api/v1/getRoleInfo", method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO getRoleInfo(@RequestHeader String Authorization,
                                 @RequestParam String name,
                                 @RequestParam String value,
                                 @RequestParam(required = false) Integer company_id){
        try{
            BaseUcDTO baseUcDTO = thorInterfaceService.getRoleInfo(Authorization, name, value, company_id);
            return baseUcDTO;
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }


    @ApiOperation(value = "根据部门iD或分公司ID获取用户集合接口", notes = "根据部门iD或分公司ID获取用户集合接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "company_id", value = "总公司Id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "department_id", value = "部门id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "role_id", value = "角色id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "status", value = "状态", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "sub_company_id", value = "子公司ID", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/api/v1/getUserIds", method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO getRoleInfo(@RequestHeader String Authorization,
                                 @RequestParam(required = false) Integer company_id,
                                 @RequestParam(required = false) Integer department_id,
                                 @RequestParam(required = false) Integer role_id,
                                 @RequestParam(required = false) Integer status,
                                 @RequestParam(required = false) Integer sub_company_id){
        try{
            return thorInterfaceService.getUserIds(Authorization, company_id, department_id, role_id, status, sub_company_id);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }


    @ApiOperation(value = "查询角色信息接口", notes = "查询角色信息接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "where", value = "a:1:{s:5:\"title\";s:13:\"这是标题1\";}", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "类型1或者2", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/api/v1/getRoleByWhere", method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO getRoleByWhere(@RequestHeader String Authorization,
                                    @RequestParam(required = false) String where,
                                    @RequestParam Integer type){
        try{
            BaseUcDTO baseUcDTO = thorInterfaceService.getRoleByWhere( Authorization, where, type);
            return baseUcDTO;
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }

    @ApiOperation(value = "得到department列表接口", notes = "得到department列表接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "where", value = "a:1:{s:5:\"title\";s:13:\"这是标题1\";}", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "类型1或者2", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/api/v1/getDepartmentByWhere", method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO getDepartmentByWhere(@RequestHeader String Authorization,
                                          @RequestParam(required = false) String where,
                                          @RequestParam Integer type){
        try{
            BaseUcDTO baseUcDTO = thorInterfaceService.getDepartmentByWhere( Authorization, where, type);
            return baseUcDTO;
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }


    @ApiOperation(value = "得到下属部门Id接口", notes = "得到下属部门Id接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "department_id", value = "部门编号", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/api/v1/getSubDepartmentId", method = RequestMethod.POST)
    @ResponseBody
    public Object getSubDepartmentId(@RequestHeader String Authorization,
                                     @RequestParam Integer department_id){
        try{
            return thorInterfaceService.getSubDepartmentId( Authorization, department_id);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }


    @ApiOperation(value = "得到department列表接口", notes = "得到department列表接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "where", value = "a:1:{s:5:\"title\";s:13:\"这是标题1\";}", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "类型1或者2", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/api/v1/getDepartment", method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO getDepartment(@RequestHeader String Authorization,
                                   @RequestParam(required = false) String where,
                                   @RequestParam Integer type){
        try{
            BaseUcDTO baseUcDTO = thorInterfaceService.getDepartment( Authorization, where, type);
            return baseUcDTO;
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }


    @ApiOperation(value = "得到SubCompanys接口", notes = "得到SubCompanys接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "city", value = "城市", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "where", value = "a:1:{s:5:\"title\";s:13:\"这是标题1\";}", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "类型1或者2", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "company_id", value = "总公司", required = false, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/api/v1/getSubCompanys", method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO getSubCompanys(@RequestHeader String Authorization,
                                    @RequestParam(required = false) String city,
                                    @RequestParam(required = false) String where,
                                    @RequestParam Integer type,
                                    @RequestParam(required = false) Integer company_id){
        try{
            BaseUcDTO baseUcDTO = thorInterfaceService.getSubCompanys(Authorization, city, where, type, company_id);
            return baseUcDTO;
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }


    @ApiOperation(value = "得到下属部门列表接口", notes = "得到下属部门列表接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "department_id", value = "部门编号", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "data_type", value = "类型4：查询全部或者其他", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/api/v1/getSubDepartmentList", method = RequestMethod.POST)
    @ResponseBody
    public Object getSubDepartmentList(@RequestHeader String Authorization,
                                       @RequestParam(required = false) Integer department_id,
                                       @RequestParam Integer data_type){
        try{
            return thorInterfaceService.getSubDepartmentList( Authorization, department_id, data_type);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            return new BaseUcDTO(9000,e.getMessage(),null);
        }
    }


    @ApiOperation(value="根据信息查到用户信息接口", notes="根据信息查到用户信息接口API")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
        @ApiImplicitParam(name = "department_id", value = "部门编号", required = false, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "sub_company_id", value = "分公司Id", required = false, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "role_id", value = "角色id", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/api/v1/getUserListByRoles",method = RequestMethod.POST)
    @ResponseBody
    public PhpQueryResultDTO getUserListByRoles (@RequestHeader String Authorization,
                                                 @RequestParam(required = false) Integer role_id,
                                                 @RequestParam(required = false) Integer department_id,
                                                 @RequestParam(required = false) Integer sub_company_id) {
        try{
            return thorInterfaceService.getUserListByRoles(Authorization, role_id, department_id, sub_company_id);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return PhpQueryResultDTO.getExcetion(9000, e.getMessage());
        }
    }



    @ApiOperation(value="根据信息查到用户id集合查询用户信息接口", notes="根据信息查到用户id集合查询用户信息接口API")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
        @ApiImplicitParam(name = "user_ids", value = "用户编号", required = false, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "department_ids", value = "部门编号", required = false, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "sub_company_id", value = "分公司编号编号", required = false, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "flag", value = "eqh或者neq", required = false, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "page", value = "页数", required = false, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "size", value = "每页显示", required = false, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "name", value = "用户姓名", required = false, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "status", value = "状态", required = false, paramType = "query", dataType = "int")
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
                                               @RequestParam(required = false)String name,
                                               @RequestParam(required = false) Integer status){
        try{
            return thorInterfaceService.getUserInfoByIds(Authorization, user_ids,department_ids,sub_company_id,flag,page,size,name, status);

        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return PhpQueryResultDTO.getExcetion(9000, e.getMessage());
        }
    }


    @ApiOperation(value="通过用户属性得到用户信息接口", notes="通过用户属性得到用户信息接口API")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
        @ApiImplicitParam(name = "telephone", value = "手机号", required = false, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "user_id", value = "用户编号", required = false, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "name", value = "用户姓名", required = false, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/api/v1/getUserInfoByField",method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO getUserInfoByField(@RequestHeader String Authorization,
                                        @RequestParam(value = "telephone",required = false) String telephone,
                                        @RequestParam(value = "user_id",required = false) Integer user_id,
                                        @RequestParam(value = "name",required = false) String name){
        try{
            return thorInterfaceService.getUserInfoByField(Authorization, telephone, user_id, name);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return new BaseUcDTO(9000,  e.getMessage(), null);
        }
    }


    @ApiOperation(value="得到用户到列表接口", notes="得到用户到列表接口API")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
        @ApiImplicitParam(name = "page", value = "page", required = false, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "size", value = "size", required = false, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "where", value = "a:1:{s:5:\"title\";s:13:\"这是标题1\";}", required = false, paramType = "query", dataType = "string"),
        @ApiImplicitParam(name = "type", value = "类型1或者2", required = false, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/api/v1/getUserInfo",method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO getUserInfo(@RequestHeader String Authorization,
                                 @RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer size,
                                 @RequestParam(required = false) String where,
                                 @RequestParam(required = false) Integer type){
        try{
            return thorInterfaceService.getUserInfo(Authorization, page, size, where, type);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return new BaseUcDTO(9000,  e.getMessage(), null);
        }
    }


    @ApiOperation(value="根据roleId获取获取团队长信息接口", notes="根据roleId获取获取团队长信息接口API")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
    })
    @RequestMapping(value = "/api/v1/getUpTdzByRole_id",method = RequestMethod.GET)
    @ResponseBody
    public BaseUcDTO getUpTdzByRole_id(@RequestHeader String Authorization){
        try{
            return thorInterfaceService.getUpTdzByRole_id(Authorization);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return new BaseUcDTO(9000,  e.getMessage(), null);
        }
    }



    @ApiOperation(value="根据业务员id查询所属的团队长接口", notes="根据业务员id查询所属的团队长接口API")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
        @ApiImplicitParam(name = "user_id", value = "用户编号", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/api/v1/getUpTdz",method = RequestMethod.POST)
    @ResponseBody
    public Object getUpTdz(@RequestHeader String Authorization, @RequestParam(required = false) Integer user_id){
        try {
            UserModelDTO upTdz = thorInterfaceService.getUpTdz(Authorization, user_id);
            return new BaseUcDTO(0,  null, upTdz);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new BaseUcDTO(9000,  e.getMessage(), null);
        }
    }

    @ApiOperation(value="获取团队长下用户信息接口", notes="获取团队长下用户信息接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
            @ApiImplicitParam(name = "city", value = "城市", required = false, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/api/v1/getCRMUser",method = RequestMethod.GET)
    @ResponseBody
    public Object getCRMUser(@RequestHeader String Authorization, @RequestParam(required = false) Integer user_id, @RequestParam(required = false) String city){
        try {
            BaseUcDTO<List<CrmUserDTO>> crmUser = thorInterfaceService.getCRMUser(Authorization, city);
            return new BaseUcDTO(0,  null, crmUser.getRetData());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new BaseUcDTO(9000,  e.getMessage(), null);
        }
    }


    /**
     *
     * 切换系统
     */
    @ApiOperation(value="切换系统接口", notes="切换系统接口API")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
        @ApiImplicitParam(name = "uid", value = "用户id", required = true, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "system", value = "系统名", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/api/v1/getSwitchSystem",method = RequestMethod.POST)
    @ResponseBody
    public Object getSwitchSystem(@RequestHeader String Authorization, @RequestParam Integer uid, @RequestParam String system) {
        try {
            return thorInterfaceService.getSwitchSystem(Authorization, uid, system);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new BaseUcDTO(9000,  e.getMessage(), null);
        }
    }



    //解决header跨域问题
    @ApiOperation(value="得到下属员工接口", notes="得到下属员工接口API")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
        @ApiImplicitParam(name = "user_id", value = "用户编号", required = false, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "data_type", value = "data_type1，2，3，4", required = false, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/api/v1/getSubUserByUserId",method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO getSubUserByUserId(@RequestHeader String Authorization, @RequestParam(required = false) Integer user_id,
                                        @RequestParam(required = false) Integer data_type){
        try{
            return thorInterfaceService.getSubUserByUserId(Authorization, user_id, data_type);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return new BaseUcDTO(9000,  e.getMessage(), null);
        }
    }


    @ApiOperation(value="得到团队长下业务员操作接口", notes="得到团队长下业务员操作接口API")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string"),
        @ApiImplicitParam(name = "user_id", value = "用户编号", required = false, paramType = "query", dataType = "int"),
        @ApiImplicitParam(name = "department_id", value = "部门编号", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/api/v1/getSubUserByTDZ",method = RequestMethod.POST)
    @ResponseBody
    public BaseUcDTO getSubUserByTDZ(@RequestHeader String Authorization, @RequestParam(required = false) Integer user_id,
                                     @RequestParam(required = false) Integer department_id) {
        try {
            return thorInterfaceService.getSubUserByTDZ(Authorization, user_id, department_id);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return new BaseUcDTO(9000,  e.getMessage(), null);
        }
    }


    @ApiOperation(value="得到团队长下业务员操作接口", notes="得到团队长下业务员操作接口API")
    @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 39656461-c539-4784-b622-feda73134267", dataType = "string")
    @RequestMapping(value = "/api/v1/getUpYwyByRole_id",method = RequestMethod.GET)
    @ResponseBody
    public BaseUcDTO getUpYwyByRole_id(@RequestHeader String Authorization) {
        try {
            return thorInterfaceService.getUpYwyByRole_id(Authorization);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return new BaseUcDTO(9000,  e.getMessage(), null);
        }
    }


}
