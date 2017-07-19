package com.fjs.cronus.api;

import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.service.client.ThorInterfaceService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2017/7/19 0019.
 */
@RestController
@RequestMapping("/uc")
public class ThorController {

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
        BaseUcDTO baseUcDTO = thorInterfaceService.postUCByCheckMobile(Authorization, phone);
        return baseUcDTO;
    }


}
