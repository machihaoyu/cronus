package com.fjs.cronus.service;


import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.zip.CRC32;

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
        switch (ocr_type){
            //身份证
            case 1:
                resultDto = ocrIdentityService.getOcrInfoList(create_user_id,customer_telephone,customer_name,status,page,size,order);
                break;
            case 2:
                resultDto = ocrHouseholdRegisterService.getOcrInfoList(create_user_id,customer_telephone,customer_name,status,page,size,order);
                break;
            case 3:
                resultDto = driverLicenseService.getOcrInfoList(create_user_id,customer_telephone,customer_name,status,page,size,order);
                break;
            case 4:
                resultDto = driverVehicleService.getOcrInfoList(create_user_id,customer_telephone,customer_name,status,page,size,order);
                break;
            case 5:
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

           switch (ocr_type){
               //身份证
               case 1:
                   resultDto = ocrIdentityService.editOcrInfo(id);
                   break;
               case 2:
                   resultDto = ocrHouseholdRegisterService.editOcrInfo(id);
                   break;
             /*  case 3:
                   resultDto = driverLicenseService.editOcrInfo(Integer id,Integer ocr_type);
                   break;
               case 4:
                   resultDto = driverVehicleService.editOcrInfo(Integer id,Integer ocr_type);
                   break;
               case 5:
                   resultDto = houseRegisterService.editOcrInfo(Integer id,Integer ocr_type);
                   break;*/
               default:
                  // resultDto = ocrIdentityService.editOcrInfo(Integer id,Integer ocr_type);
                   break;
           }
            return  resultDto;
       }
}
