package com.fjs.cronus.service;


import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.OcrInfoEnum;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.exception.CronusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * Created by msi on 2017/9/25.
 */
@Service
public class OcrInfoService {


    @Autowired
    OcrIdentityService ocrIdentityService;
    @Autowired
    OcrHouseholdRegisterService ocrHouseholdRegisterService;
    @Autowired
    DriverLicenseService driverLicenseService;
    @Autowired
    OcrDriverVehicleService driverVehicleService;
    @Autowired
    HouseRegisterService houseRegisterService;

    public QueryResult getOcrInfoList(Integer create_user_id, String customer_telephone, String customer_name, String status,
                                      Integer ocr_type, Integer page, Integer size, String order){
        QueryResult resultDto = new QueryResult();
        if (ocr_type == null){
            ocr_type = 1;
        }
        switch (OcrInfoEnum.getByValue(ocr_type)){
            //身份证
            case ocr_identity:
                resultDto = ocrIdentityService.getOcrInfoList(create_user_id,customer_telephone,customer_name,status,page,size,order);
                break;
            case ocr_householdregister:
                resultDto = ocrHouseholdRegisterService.getOcrInfoList(create_user_id,customer_telephone,customer_name,status,page,size,order);
                break;
            case ocr_driverlicense:
                resultDto = driverLicenseService.getOcrInfoList(create_user_id,customer_telephone,customer_name,status,page,size,order);
                break;
            case ocr_drivervehicle:
                resultDto = driverVehicleService.getOcrInfoList(create_user_id,customer_telephone,customer_name,status,page,size,order);
                break;
            case ocr_houseRegister:
                resultDto = houseRegisterService.getOcrInfoList(create_user_id,customer_telephone,customer_name,status,page,size,order);
                break;
            default:
                resultDto = ocrIdentityService.getOcrInfoList(create_user_id,customer_telephone,customer_name,status,page,size,order);
                break;
        }
        return  resultDto;
    }

       public CronusDto editOcrInfo(Integer id,Integer ocr_type){
           CronusDto resultDto = new CronusDto();
           if (id == null){
               throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
           }
           if (ocr_type == null){
               ocr_type = 1;
           }
           switch (OcrInfoEnum.getByValue(ocr_type)){
               //身份证
               case ocr_identity:
                   resultDto = ocrIdentityService.editOcrInfo(id);
                   break;
               case ocr_householdregister:
                   resultDto = ocrHouseholdRegisterService.editOcrInfo(id);
                   break;
               case ocr_driverlicense:
                   resultDto = driverLicenseService.editOcrInfo(id);
                   break;
               case ocr_drivervehicle:
                   resultDto = driverVehicleService.editOcrInfo(id);
                   break;
               case ocr_houseRegister:
                   resultDto = houseRegisterService.editOcrInfo(id);
                   break;
               default:
                   resultDto = ocrIdentityService.editOcrInfo(id);
                   break;
           }
            return  resultDto;
       }
    public CronusDto editOcrInfoOK(JSONObject jsonObject,String token){
        CronusDto resultDto = new CronusDto();
        Integer ocr_type = jsonObject.getInteger("ocr_type");
        if (ocr_type == null){
            ocr_type = 1;
        }
        switch (OcrInfoEnum.getByValue(ocr_type)){
            //身份证
            case ocr_identity:
                resultDto = ocrIdentityService.editOcrInfoOK(jsonObject,token);
                break;
            case ocr_householdregister:
                resultDto = ocrHouseholdRegisterService.editOcrInfoOK(jsonObject,token);
                break;
            case ocr_driverlicense:
                resultDto = driverLicenseService.editOcrInfoOK(jsonObject,token);
                break;
              case ocr_drivervehicle:
                resultDto = driverVehicleService.editOcrInfoOK(jsonObject,token);
                break;
            case ocr_houseRegister:
                resultDto = houseRegisterService.editOcrInfoOK(jsonObject,token);
                break;
            default:
                resultDto = ocrIdentityService.editOcrInfoOK(jsonObject,token);
                break;
        }
        return  resultDto;
    }
}
