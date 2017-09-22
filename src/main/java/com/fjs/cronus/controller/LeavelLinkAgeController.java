package com.fjs.cronus.controller;

import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.ThreePcLinkAgeDTO;
import com.fjs.cronus.dto.cronus.ThreelinkageDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.CityService;
import com.fjs.cronus.service.LicensePlateService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by msi on 2017/9/19.
 */
@Controller
@RequestMapping("/api/v1")
public class LeavelLinkAgeController {
    private  static  final Logger logger = LoggerFactory.getLogger(LeavelLinkAgeController.class);
    @Autowired
    CityService cityService;
    @Autowired
    LicensePlateService licensePlateService;
    @ApiOperation(value="getPCCityTwoLinkAge",notes="城市俩级联动")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            })
    @RequestMapping(value = "/getPCCityTwoLinkAge", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<List<ThreePcLinkAgeDTO>> getPCCityTwoLinkAge() {
        CronusDto resultDto = new CronusDto();

        try {
            List<ThreePcLinkAgeDTO> cityDtoList = cityService.getPCCityTwoLinkAge();
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setData(cityDtoList);
        } catch (Exception e) {
            logger.error("--------------->getPCCityTwoLinkAge",e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }

        return resultDto;
    }

    @ApiOperation(value="getPCCityThreeLinkAge",notes="三级联动")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            })
    @RequestMapping(value = "/getPCCityThreeLinkAge", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<List<ThreelinkageDTO>> getPCCityThreeLinkAge() {
        CronusDto resultDto = new CronusDto();

        try {
            List<ThreePcLinkAgeDTO> cityDtoList = cityService.getPCCityThreeLinkAge();
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setData(cityDtoList);
        } catch (Exception e) {
            logger.error("--------------->getCityByProvinceDirec",e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return resultDto;
    }
    @ApiOperation(value="getCarPlate",notes="车牌归属地俩级联动")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            })
    @RequestMapping(value = "/getCarPlate", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<List<ThreePcLinkAgeDTO>> getCarPlate() {
        CronusDto resultDto = new CronusDto();

        try {
            List<ThreePcLinkAgeDTO> cityDtoList = licensePlateService.getCarPlate();
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setData(cityDtoList);
        } catch (Exception e) {
            logger.error("--------------->getPCCityTwoLinkAge",e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return resultDto;
    }
}
