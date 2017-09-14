package com.fjs.cronus.util;

import com.fjs.cronus.dto.cronus.CustomerDto;
import com.fjs.cronus.model.CustomerInfo;
import org.springframework.util.StringUtils;

/**
 * Created by msi on 2017/9/14.
 */
public class EntityToDto {


    public static void customerEntityToCustomerDto(CustomerInfo customerInfo, CustomerDto dto){

            dto.setId(customerInfo.getId().toString());
        if (!StringUtils.isEmpty(customerInfo.getTelephonenumber())){
            dto.setTelephonenumber(customerInfo.getTelephonenumber());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerName())){
            dto.setCustomerName(customerInfo.getCustomerName());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerLevel())){
            dto.setCustomerLevel(customerInfo.getCustomerLevel());
        }
        if (!StringUtils.isEmpty(customerInfo.getSparePhone())){
            dto.setSparePhone(customerInfo.getSparePhone());
        }
        if (!StringUtils.isEmpty(customerInfo.getAge())){
            dto.setAge(customerInfo.getAge());
        }
        if (!StringUtils.isEmpty(customerInfo.getMarriage())){
            dto.setMarriage(customerInfo.getMarriage());
        }
        if (!StringUtils.isEmpty(customerInfo.getIdCard())){
            dto.setIdCard(customerInfo.getIdCard());
        }
        if (!StringUtils.isEmpty(customerInfo.getProvinceHuji())){
            dto.setProvinceHuji(customerInfo.getProvinceHuji());
        }

        if (!StringUtils.isEmpty(customerInfo.getSex())){
            dto.setSex(customerInfo.getSex());
        }

        if (!StringUtils.isEmpty(customerInfo.getCustomerAddress())){
            dto.setCustomerAddress(customerInfo.getCustomerAddress());
        }

        if (!StringUtils.isEmpty(customerInfo.getPerDescription())){
            dto.setPerDescription(customerInfo.getPerDescription());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseStatus())){
            dto.setHouseStatus(customerInfo.getHouseStatus());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseAmount())){
            dto.setHouseAmount(customerInfo.getHouseAmount());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseType())){
            dto.setHouseType(customerInfo.getHouseType());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseValue())){
            dto.setHouseValue(customerInfo.getHouseValue());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseArea())){
            dto.setHouseArea(customerInfo.getHouseArea());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseAge())){
            dto.setHouseAge(customerInfo.getHouseAge());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseLoan())){
            dto.setHouseLoan(customerInfo.getHouseLoan());
        }

        if (!StringUtils.isEmpty(customerInfo.getHouseLocation())){
            dto.setHouseLocation(customerInfo.getHouseLocation());
        }

        if (!StringUtils.isEmpty(customerInfo.getCity())){
            dto.setCity(customerInfo.getCity());
        }
        if (!StringUtils.isEmpty(customerInfo.getCustomerClassify())){
            dto.setCustomerClassify(customerInfo.getCustomerClassify());
        }

        if (!StringUtils.isEmpty(customerInfo.getCallbackStatus())){
            dto.setCallbackStatus(customerInfo.getCallbackStatus());
        }

        if (!StringUtils.isEmpty(customerInfo.getCallbackTime())){
            //date 转
            dto.setCallbackTime(DateUtils.format(customerInfo.getCallbackTime()));
        }

        if (!StringUtils.isEmpty(customerInfo.getSubCompanyId())){
            dto.setSubCompanyId(customerInfo.getSubCompanyId().toString());
        }

        if (!StringUtils.isEmpty(customerInfo.getCreateTime())){
            dto.setCreateTime(DateUtils.format(customerInfo.getCreateTime(),DateUtils.FORMAT_LONG));
        }

        if (!StringUtils.isEmpty(customerInfo.getLastUpdateTime())){
            dto.setLastUpdateTime(DateUtils.format(customerInfo.getLastUpdateTime()));
        }

        if (!StringUtils.isEmpty(customerInfo.getCreateUser())){
            dto.setCreateUser(customerInfo.getCreateUser().toString());
        }

        if (!StringUtils.isEmpty(customerInfo.getLastUpdateUser())){
            dto.setLastUpdateUser(customerInfo.getLastUpdateUser().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getIsDeleted())){
            dto.setIsDeleted(customerInfo.getIsDeleted().toString());
        }
        if (!StringUtils.isEmpty(customerInfo.getHouseAlone())){
            dto.setHouseAlone(customerInfo.getHouseAlone());
        }







    }



}
