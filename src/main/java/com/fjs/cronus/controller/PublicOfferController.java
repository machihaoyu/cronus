package com.fjs.cronus.controller;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.api.PhpApiDto;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.dto.api.uc.CityDto;
import com.fjs.cronus.dto.cronus.PanParamDTO;
import com.fjs.cronus.dto.uc.SubCompanyCityDto;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.dto.cronus.CustomerListDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.service.CustomerInfoService;
import com.fjs.cronus.service.PanService;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.service.uc.UcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by msi on 2017/11/30.
 */
@Controller
@Api(description = "公盘客户控制器")
@RequestMapping("/api/v1")
public class PublicOfferController {


    private  static  final Logger logger = LoggerFactory.getLogger(PublicOfferController.class);
    @Autowired
    UcService ucService;
    @Autowired
    TheaClientService theaClientService;
    @Autowired
    CustomerInfoService customerInfoService;
    @Autowired
    PanService panService;
    @ApiOperation(value="获取公盘列表", notes="获取公盘列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerName", value = "客户姓名", required = false, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "telephonenumber", value = "手机号", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "utmSource", value = "渠道来源(除公盘外的必须传)", required = false, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "houseStatus", value = "有 无房产", required = false, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "customerClassify", value = "跟进状态(暂未接通 无意向 有意向待跟踪 资质差无法操作 空号 外地 同行 内部员工 其他)", required = false, paramType = "query",  dataType = "string"),
            @ApiImplicitParam(name = "customerSource", value = "客户来源", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "mountLevle", value = "1：0-20万，2：20-50万，3:50-100万，4:100-500万，5：大于五百万 ", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "city", value = "城市", required = false, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "公盘类型（外地公盘填1）", required = false, paramType = "query",  dataType = "int"),
            @ApiImplicitParam(name = "page", value = "查询第几页", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "显示多少", required = false, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/Offerlist", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<CustomerListDTO>> Offerlist(@RequestParam(required = false) String customerName,
                                                             @RequestParam(required = false) String telephonenumber,
                                                             @RequestParam(required = false) String utmSource,
                                                             @RequestParam(required = false) String houseStatus,
                                                             @RequestParam(required = false) String customerClassify,
                                                             @RequestParam(required = false) String customerSource,
                                                             @RequestParam(required = false) String city,
                                                             @RequestParam(required = false) Integer mountLevle ,
                                                             @RequestParam(required = false) Integer type,
                                                             @RequestParam(required = false) Integer page,
                                                             @RequestParam(required = false) Integer size,
                                                             @RequestHeader("Authorization")String token){

        CronusDto<QueryResult<CustomerListDTO>> cronusDto  = new CronusDto<>();

        QueryResult<CustomerListDTO> queryResult=null;
        try{
            //从token中获取用户信息
            PanParamDTO pan = new PanParamDTO();
            UserInfoDTO userInfoDTO = ucService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
            Integer userId=null;
            Integer companyId=null;
            if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())){
                userId=Integer.parseInt(userInfoDTO.getUser_id());
            }
            List<String> mainCitys=new ArrayList<String>();//主要城市
            List<Integer> subCompanyIds=new ArrayList<>();//异地分公司
            if (StringUtils.isNotEmpty(utmSource)){
                pan.setUtmSource(utmSource);
            }else {
                //获取下属的城市
                List<CityDto> subsCitys=ucService.getSubcompanyByUserId(token,userId,CommonConst.SYSTEMNAME);
                List<String> subCitys=new ArrayList<String>();
                if (!CollectionUtils.isEmpty(subsCitys)){
                    for (CityDto cityDto:subsCitys){
                        if (StringUtils.isNotEmpty(cityDto.getName())){
                            subCitys.add(cityDto.getName());
                        }
                    }
                }
                //获取主要城市
                String mainCity = theaClientService.findValueByName(token,CommonConst.MAIN_CITY);
                String[] mainCityArray=mainCity.split(",");
                int mainCitySize=mainCityArray.length;
                for (int i=0;i<mainCitySize;i++){
                    mainCitys.add(mainCityArray[i]);
                }
                mainCitys.retainAll(subCitys);
                int citySize=mainCitys.size();
                for(int i=0;i<citySize;i++){
                    String cityName=mainCitys.get(i);
                    cityName=cityName+"''";
                }
                //获取下属的分公司
                List<SubCompanyCityDto> subCompanyDtos=ucService.getAllSubCompanyByUserId(token,userId,CommonConst.SYSTEMNAME);
                List<String> subCompanys=new ArrayList<String>();
                List<String> remoteCitys=new ArrayList<String>();
                if (!CollectionUtils.isEmpty(subCompanyDtos)){
                    for (SubCompanyCityDto subCompanyCityDto:subCompanyDtos){
                        if (StringUtils.isNotEmpty(subCompanyCityDto.getCityName())){
                            subCompanys.add(subCompanyCityDto.getCityName());
                        }
                    }
                }
                //获取异地分公司
                String remoteCity=theaClientService.findValueByName(token,CommonConst.REMOTE_CITY);
                String[] remoteCityArray=remoteCity.split(",");
                int remoteCitySize=remoteCityArray.length;
                for (int i=0;i<remoteCitySize;i++){
                    remoteCitys.add(remoteCityArray[i]);
                }
                subCompanys.retainAll(remoteCitys);
                //获取下属的异地分公司
                List<SubCompanyCityDto> subCompanyCityDtoList2=new ArrayList<>();
                for (SubCompanyCityDto subCompanyCityDto:subCompanyDtos){
                    if (subCompanys.contains(subCompanyCityDto.getCityName()) && !subCompanyCityDtoList2.contains(subCompanyCityDto)){
                        subCompanyCityDtoList2.add(subCompanyCityDto);
                        subCompanyIds.add(subCompanyCityDto.getSubCompanyId());
                    }
                }
//                for (SubCompanyCityDto subCompanyCityDto2:subCompanyCityDtoList2){
//                    System.out.println(subCompanyCityDto2.toString());
//                }
            }
            pan.setCustomerName(customerName);
            pan.setTelephonenumber(telephonenumber);
            pan.setHouseStatus(houseStatus);
            pan.setCustomerClassify(customerClassify);
            pan.setCustomerSource(customerSource);
            pan.setCity(city);
            queryResult  =panService.listByOffer(pan,userId,companyId,token,CommonConst.SYSTEMNAME,page,size,mainCitys,subCompanyIds,type,mountLevle);
            cronusDto.setData(queryResult);
            cronusDto.setResult(CommonMessage.SUCCESS.getCode());
            cronusDto.setMessage(CommonMessage.SUCCESS.getCodeDesc());
        }catch (Exception e){
            logger.error("--------------->Offerlist客户信息操作失败",e);
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  cronusDto;
    }

    @ApiOperation(value="公盘领取", notes="公盘领取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query",  dataType = "int"),
    })
    @RequestMapping(value = "/pullPan", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto pullPan(@RequestParam(required = true) Integer customerId, HttpServletRequest request){
        CronusDto theaApiDTO=new CronusDto();
        String token=request.getHeader("Authorization");
        PHPLoginDto resultDto = ucService.getAllUserInfo(token,CommonConst.SYSTEMNAME);
        String[] authority=resultDto.getAuthority();
        if(authority.length>0){
            List<String> authList= Arrays.asList(authority);
            if (authList.contains(CommonConst.PULL_LOAN_URL)){
                theaApiDTO.setResult(CommonMessage.PULL_FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.NO_AUTHORIZE);
                return theaApiDTO;
            }
        }
        UserInfoDTO userInfoDTO=ucService.getUserIdByToken(token,CommonConst.SYSTEMNAME);
        CustomerInfo customerInfo = null;
        try{
            Integer userId=null;
            if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())){
                userId=Integer.parseInt(userInfoDTO.getUser_id());
            }
            //领取操作
            boolean updateResult = panService.pullPan(customerId,userId,token,userInfoDTO.getName());
            if (updateResult == true) {
                theaApiDTO.setResult(CommonMessage.PULL_SUCCESS.getCode());
                theaApiDTO.setMessage(CommonMessage.PULL_SUCCESS.getCodeDesc());
            } else {
                logger.error("-------------->pullPan领取失败");
                theaApiDTO.setResult(CommonMessage.PULL_FAIL.getCode());
                theaApiDTO.setMessage(CommonMessage.PULL_FAIL.getCodeDesc());
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
            }
        }catch (Exception e){
            logger.error("-------------->pullPan领取失败",e);
            theaApiDTO.setResult(CommonMessage.PULL_FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.PULL_FAIL.getCodeDesc());
        }
        return theaApiDTO;
    }



}
