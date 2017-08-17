package com.fjs.cronus.api;

import com.fjs.cronus.dto.ocr.*;
import com.fjs.cronus.service.client.TalosService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2017/8/17 0017.
 */
@RestController
@RequestMapping("/ocr/api")
public class OcrController {

    @Autowired
    private TalosService talosService;

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
    @RequestMapping(value = "/v1/", method = RequestMethod.POST)//TODO 地址;
    public void ocrService(@RequestHeader(name = "Authorization") String token, @RequestBody ReqParamDTO reqParamDTO){
        talosService.ocrService(reqParamDTO, token);
    }


    /**
     * 保存身份证信息
     * @param idCardDTO 身份证实体类;
     * @param token 认证信息;
     */
    @ApiOperation(value="保存身份证信息接口", notes="保存身份证信息接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", defaultValue = "Bearer ", required = true, paramType = "header", dataType = "string"),
            @ApiImplicitParam(name = "idCardDTO", value = "", required = true, paramType = "body", dataType = "IdCardDTO")
    })
    @RequestMapping(value = "/v1/saveIdCard", method = RequestMethod.POST)
    public void saveIdCard(@RequestBody IdCardDTO idCardDTO, @RequestHeader(name = "Authorization") String token){
        //TODO 调用PHP接口;
    }


    /**
     * 保存驾驶证信息
     * @param driverLicenseDTO 驾驶证实体类
     * @param token 认证信息
     */
    @ApiOperation(value="保存驾驶证信息接口", notes="保存驾驶证信息接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", defaultValue = "Bearer ", required = true, paramType = "header", dataType = "string"),
            @ApiImplicitParam(name = "driverLicenseDTO", value = "", required = true, paramType = "body", dataType = "DriverLicenseDTO")
    })
    @RequestMapping(value = "/v1/saveDriverLicense", method = RequestMethod.POST)
    public void saveDriverLicense(@RequestBody DriverLicenseDTO driverLicenseDTO, @RequestHeader(name = "Authorization") String token){
        //TODO 调用PHP接口;
    }


    /**
     * 保存行驶证信息
     * @param driverVehicleDTO 行驶证实体类
     * @param token 认证信息
     */
    @ApiOperation(value="保存行驶证信息接口", notes="保存行驶证信息接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", defaultValue = "Bearer ", required = true, paramType = "header", dataType = "string"),
            @ApiImplicitParam(name = "driverVehicleDTO", value = "", required = true, paramType = "body", dataType = "DriverVehicleDTO")
    })
    @RequestMapping(value = "/v1/saveDriverVehicle", method = RequestMethod.POST)
    public void saveDriverVehicle(@RequestBody DriverVehicleDTO driverVehicleDTO, @RequestHeader(name = "Authorization") String token){
        //TODO 调用PHP接口;
    }


    /**
     * 保存户口本信息
     * @param householdRegisterDTO 户口本实体类
     * @param token 认证信息
     */
    @ApiOperation(value="保存户口本信息接口", notes="保存户口本信息接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", defaultValue = "Bearer ", required = true, paramType = "header", dataType = "string"),
            @ApiImplicitParam(name = "householdRegisterDTO", value = "", required = true, paramType = "body", dataType = "HouseholdRegisterDTO")
    })
    @RequestMapping(value = "/v1/saveHouseholdRegister", method = RequestMethod.POST)
    public void saveHouseholdRegister(@RequestBody HouseholdRegisterDTO householdRegisterDTO, @RequestHeader(name = "Authorization") String token){
        //TODO 调用PHP接口;
    }


}
