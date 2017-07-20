package com.fjs.cronus.api;

import com.fjs.cronus.dto.uc.BaseUcDTO;
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
            return new BaseUcDTO(0,  e.getMessage(), null);
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
            return new BaseUcDTO(0,  e.getMessage(), null);
        }
    }


}
