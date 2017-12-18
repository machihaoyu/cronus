package com.fjs.cronus.api;

import com.fjs.cronus.model.thea.DatumIntegrModelDTO;
import com.fjs.cronus.service.thea.DatumIntegrModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by chenjie on 2017/12/15.
 */
@RestController
@Api(description = "提供给thea服务的接口")
public class TheaApiController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DatumIntegrModelService datumIntegrModelService;

    @ApiOperation(value = "根据客户id判断是否有身份证、房产证、户口本、收入证明、婚姻证明材料接口", notes = "根据客户id判断是否有身份证、房产证、户口本、收入证明、婚姻证明材料接口API")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
        @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/api/v1/judgeDatum", method = RequestMethod.GET)
    public DatumIntegrModelDTO judgeDatum(@RequestHeader("Authorization") String token, @RequestParam("customerId") Long customerId){
        DatumIntegrModelDTO datumIntegrModelDTO = datumIntegrModelService.judgeDatum(customerId);
        return datumIntegrModelDTO;
    }

}
