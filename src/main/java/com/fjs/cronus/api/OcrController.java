package com.fjs.cronus.api;

import com.fjs.cronus.dto.ocr.*;
import com.fjs.cronus.service.client.TalosService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Administrator on 2017/8/17 0017.
 */
@RestController
@RequestMapping("/ocr")
public class OcrController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TalosService talosService;

    @Value("${sale.url}")
    private String saleUrl;

    @Value("${sale.key}")
    private String saleKey;

    @Autowired
    private RestTemplate restTemplate;

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
    @RequestMapping(value = "/api/v1/saveIdCard", method = RequestMethod.POST)
    public void saveIdCard(@RequestBody IdCardDTO idCardDTO, @RequestHeader(name = "Authorization") String token){
        String url = saleUrl + "addOrcInfo";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("id", idCardDTO.getId());
        param.add("customer_id",idCardDTO.getCustomer_id());
        param.add("customer_name",idCardDTO.getCustomer_name());
        param.add("customer_telephone",idCardDTO.getCustomer_telephone());
        param.add("card_name",idCardDTO.getCard_name());
        param.add("card_sex",idCardDTO.getCard_sex());
        param.add("card_nation",idCardDTO.getCard_nation());
        param.add("card_birth",idCardDTO.getCard_birth());
        param.add("card_address",idCardDTO.getCard_address());
        param.add("card_num",idCardDTO.getCard_num());
        param.add("card_sign_org",idCardDTO.getCard_sign_org());
        param.add("card_valid_start",idCardDTO.getCard_valid_start());
        param.add("card_valid_end",idCardDTO.getCard_valid_end());
        param.add("crm_attach_id",idCardDTO.getCrm_attach_id());
        param.add("create_user_id",idCardDTO.getCreate_user_id());
        param.add("create_user_name",idCardDTO.getCreate_user_name());
        param.add("status","草稿");//未效验状态
        param.add("update_user_id",idCardDTO.getCreate_user_id());
        param.add("update_user_name",idCardDTO.getCreate_user_name());
        param.add("type",idCardDTO.getType());
        param.add("side",idCardDTO.getSide());
        LOGGER.warn("保存身份证信息接口_OcrController_saveIdCard : idCardDTO = " + ReflectionToStringBuilder.toString(idCardDTO) + ", token = " + token);
        String res = restTemplate.postForObject(url, param, String.class);
        LOGGER.warn("保存身份证信息接口_OcrController_saveIdCard ：res = " + res);
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
    @RequestMapping(value = "/api/v1/saveDriverLicense", method = RequestMethod.POST)
    public void saveDriverLicense(@RequestBody DriverLicenseDTO driverLicenseDTO, @RequestHeader(name = "Authorization") String token){
        String url = saleUrl + "addOrcInfo";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("id", driverLicenseDTO.getId());
        param.add("customer_id",driverLicenseDTO.getCustomer_id());
        param.add("customer_name",driverLicenseDTO.getCustomer_name());
        param.add("customer_telephone",driverLicenseDTO.getCustomer_telephone());
        param.add("driver_name",driverLicenseDTO.getDriver_name());
        param.add("driver_num",driverLicenseDTO.getDriver_num());
        param.add("driver_vehicle_type",driverLicenseDTO.getDriver_vehicle_type());
        param.add("driver_start_date",driverLicenseDTO.getDriver_start_date());
        param.add("driver_end_date",driverLicenseDTO.getDriver_end_date());
        param.add("crm_attach_id",driverLicenseDTO.getCrm_attach_id());
        param.add("create_user_id",driverLicenseDTO.getCreate_user_id());
        param.add("create_user_name",driverLicenseDTO.getCreate_user_name());
        param.add("status","草稿");
        param.add("update_user_id",driverLicenseDTO.getCreate_user_id());
        param.add("update_user_name",driverLicenseDTO.getCreate_user_name());
        param.add("type",driverLicenseDTO.getType());
        LOGGER.warn("保存驾驶证信息接口_OcrController_saveDriverLicense : driverLicenseDTO = " + ReflectionToStringBuilder.toString(driverLicenseDTO) + ", token = " + token);
        String res = restTemplate.postForObject(url, param, String.class);
        LOGGER.warn("保存驾驶证信息接口_OcrController_saveDriverLicense ：res = " + res);
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
    @RequestMapping(value = "/api/v1/saveDriverVehicle", method = RequestMethod.POST)
    public void saveDriverVehicle(@RequestBody DriverVehicleDTO driverVehicleDTO, @RequestHeader(name = "Authorization") String token){
        String url = saleUrl + "addOrcInfo";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("id", driverVehicleDTO.getId());
        param.add("customer_id",driverVehicleDTO.getCustomer_id());
        param.add("customer_name",driverVehicleDTO.getCustomer_name());
        param.add("customer_telephone",driverVehicleDTO.getCustomer_telephone());
        param.add("driver_owner",driverVehicleDTO.getDriver_owner());
        param.add("driver_plate_num",driverVehicleDTO.getDriver_plate_num());
        param.add("driver_vehicle_type",driverVehicleDTO.getDriver_vehicle_type());
        param.add("driver_vin",driverVehicleDTO.getDriver_vin());
        param.add("driver_engine_num",driverVehicleDTO.getDriver_engine_num());
        param.add("driver_register_date",driverVehicleDTO.getDriver_register_date());
        param.add("crm_attach_id",driverVehicleDTO.getCrm_attach_id());
        param.add("create_user_id",driverVehicleDTO.getCreate_user_id());
        param.add("create_user_name",driverVehicleDTO.getCreate_user_name());
        param.add("status","草稿");
        param.add("update_user_id",driverVehicleDTO.getCreate_user_id());
        param.add("update_user_name",driverVehicleDTO.getCreate_user_name());
        param.add("type",driverVehicleDTO.getType());
        LOGGER.warn("保存行驶证信息接口_OcrController_saveDriverVehicle : driverVehicleDTO = " + ReflectionToStringBuilder.toString(driverVehicleDTO) + ", token = " + token);
        String res = restTemplate.postForObject(url, param, String.class);
        LOGGER.warn("保存行驶证信息接口_OcrController_saveDriverVehicle ： res = " + res);
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
    @RequestMapping(value = "/api/v1/saveHouseholdRegister", method = RequestMethod.POST)
    public void saveHouseholdRegister(@RequestBody HouseholdRegisterDTO householdRegisterDTO, @RequestHeader(name = "Authorization") String token){
        String url = saleUrl + "addOrcInfo";
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("key",saleKey);
        param.add("id", householdRegisterDTO.getId());
        param.add("customer_id",householdRegisterDTO.getCustomer_id());
        param.add("customer_name",householdRegisterDTO.getCustomer_name());
        param.add("customer_telephone",householdRegisterDTO.getCustomer_telephone());
        param.add("household_name",householdRegisterDTO.getHousehold_name());
        param.add("household_sex",householdRegisterDTO.getHousehold_sex());
        param.add("household_native_place",householdRegisterDTO.getHousehold_native_place());
        param.add("household_birthday",householdRegisterDTO.getHousehold_birthday());
        param.add("household_id_number",householdRegisterDTO.getHousehold_id_number());
        param.add("household_people",householdRegisterDTO.getHousehold_people());
        param.add("crm_attach_id",householdRegisterDTO.getCrm_attach_id());
        param.add("create_user_id",householdRegisterDTO.getCreate_user_id());
        param.add("create_user_name",householdRegisterDTO.getCreate_user_name());
        param.add("status","草稿");
        param.add("update_user_id",householdRegisterDTO.getCreate_user_id());
        param.add("update_user_name",householdRegisterDTO.getCreate_user_name());
        param.add("type",householdRegisterDTO.getType());
        LOGGER.warn("保存户口本信息接口_OcrController_saveHouseholdRegister : householdRegisterDTO = " + ReflectionToStringBuilder.toString(householdRegisterDTO) + ", token = " + token);
        String res = restTemplate.postForObject(url, param, String.class);
        LOGGER.warn("保存户口本信息接口_OcrController_saveHouseholdRegister : res = " + res);
    }


}
