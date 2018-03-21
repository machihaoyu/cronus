package com.fjs.cronus.service.callback;

import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.api.ThorApiDTO;
import com.fjs.cronus.dto.api.uc.CompanyTheaSystemDto;
import com.fjs.cronus.dto.callback.CallBackCustomerDTO;
import com.fjs.cronus.dto.callback.CustomerBaseInfoDTO;
import com.fjs.cronus.dto.callback.CustomerHouseInfoDTO;
import com.fjs.cronus.dto.uc.DepartmentDto;
import com.fjs.cronus.mappers.CustomerCallBackMapper;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.service.client.ThorService;
import com.fjs.cronus.util.DEC3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by qiaoxin on 2018/3/20.
 */
@Service
public class CallBackService {

    @Value("${token.current}")
    private String publicToken;

    @Autowired
    CustomerCallBackMapper customerCallBackMapper;

    @Autowired
    ThorService thorService;

    public QueryResult<CallBackCustomerDTO> getCustomerList(String callbackStartTime, String callbackEndTime,
                                                            String createStartTime, String createEndTime,
                                                            String customerName, String ownUserName, String isHaveOwner,
                                                            String city, String callbackStatus,
                                                            Integer companyId, Integer subCompanyId,
                                                            String type,Integer start, Integer size, Integer cycle ) {

        QueryResult<CallBackCustomerDTO> queryResult = new QueryResult<>();


        // 设定查询条件
        Map<String, Object> paramsMap = new HashMap<>();

        if (callbackStartTime != null) {
            Date date = getDateFromString(callbackStartTime + " 00:00:00");
            if (date != null) {
                paramsMap.put("callbackStartTime", date);
            }
        }
        if (callbackEndTime != null) {
            Date date = getDateFromString(callbackStartTime + " 23:59:59");
            if (date != null) {
                paramsMap.put("callbackEndTime", date);
            }
        }
        if (createStartTime != null) {
            Date date = getDateFromString(createStartTime + " 00:00:00");
            if (date != null) {
                paramsMap.put("createStartTime", date);
            }
        }
        if (createEndTime != null) {
            Date date = getDateFromString(createEndTime + " 23:59:59");
            if (date != null) {
                paramsMap.put("createEndTime", date);
            }
        }
        if (customerName != null) {
            paramsMap.put("customer_name",customerName );
        }
        if (ownUserName != null) {
            paramsMap.put("ownUserName",ownUserName );
        }
        if (isHaveOwner != null) {
            paramsMap.put("isHaveOwner",isHaveOwner );
        }
        if (city != null) {
            paramsMap.put("city", city );
        }
        if (callbackStatus != null) {
            paramsMap.put("callbackStatus", callbackStatus );
        }
        if (companyId != null) {
            paramsMap.put("companyId", companyId );
        }
        if (subCompanyId != null) {
            paramsMap.put("sub_company_id", subCompanyId );
        }
        paramsMap.put("start", start );
        paramsMap.put("size", size );

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1 * cycle);
        paramsMap.put("cycleTime", c.getTime());
        paramsMap.put("type", type);

        long count = customerCallBackMapper.customerListCount(paramsMap);
        queryResult.setTotal(String.valueOf(count));

        List<CustomerInfo> customerInfoList = customerCallBackMapper.customerList(paramsMap);
        List<CallBackCustomerDTO> callBackCustomerDTOList = convertToDTO(customerInfoList, cycle);

        queryResult.setRows(callBackCustomerDTOList);

        return queryResult;
    }

    public CustomerHouseInfoDTO getCustomerHouseInfo(Integer customerId) {
        CustomerInfo customerInfo = customerCallBackMapper.selectCustomerById(customerId);
        CustomerHouseInfoDTO customerHouseInfoDTO = new CustomerHouseInfoDTO();

        if (customerInfo != null) {
            customerHouseInfoDTO.setHouseAge(customerInfo.getHouseAge());
            customerHouseInfoDTO.setHouseAlone(customerInfo.getHouseAlone());
            customerHouseInfoDTO.setHouseAmount(customerInfo.getHouseAmount());
            customerHouseInfoDTO.setHouseArea(customerInfo.getHouseArea());
            customerHouseInfoDTO.setHouseLoan(customerInfo.getHouseLoan());
            customerHouseInfoDTO.setHouseLoanValue(customerInfo.getHouseLoanValue());
            customerHouseInfoDTO.setHouseLocation(customerInfo.getHouseLocation());
            customerHouseInfoDTO.setHouseType(customerInfo.getHouseType());
            customerHouseInfoDTO.setHouseValue(customerInfo.getHouseValue());

        }

        return customerHouseInfoDTO;
    }


    public CustomerBaseInfoDTO getCustomerBaseInfo(Integer customerId) {
        CustomerInfo customerInfo = customerCallBackMapper.selectCustomerById(customerId);
        CustomerBaseInfoDTO customerBaseInfoDTO = new CustomerBaseInfoDTO();

        if (customerInfo != null) {
            customerBaseInfoDTO.setAge(customerInfo.getAge());
            customerBaseInfoDTO.setCallbackStatus(customerInfo.getCallbackStatus());
            customerBaseInfoDTO.setCity(customerInfo.getCity());
            customerBaseInfoDTO.setCustomerAddress(customerInfo.getCustomerAddress());
            customerBaseInfoDTO.setCustomerClassify(customerInfo.getCustomerClassify());
            customerBaseInfoDTO.setCustomerSource(customerInfo.getCustomerSource());
            customerBaseInfoDTO.setIdCard(customerInfo.getIdCard());
            customerBaseInfoDTO.setCustomerType(customerInfo.getCustomerType());
            customerBaseInfoDTO.setLoanMoney(customerInfo.getLoanAmount().toString());
            customerBaseInfoDTO.setMarriage(customerInfo.getMarriage());
            customerBaseInfoDTO.setOwnerUserName(customerInfo.getOwnUserName());
            customerBaseInfoDTO.setPerDescription(customerInfo.getPerDescription());
            customerBaseInfoDTO.setSex(customerInfo.getSex());
            customerBaseInfoDTO.setName(customerInfo.getCustomerName());
            customerBaseInfoDTO.setProvinceResidence(customerInfo.getProvinceHuji());
            customerBaseInfoDTO.setUtmSource(customerInfo.getUtmSource());
            customerBaseInfoDTO.setSparePhone(customerInfo.getSparePhone());
        }

        return customerBaseInfoDTO;
    }

    @Transactional
    public int updateCallbackStatus(String callback_status, String per_description, Integer customerId) {
        Integer result = customerCallBackMapper.updateCallbackStatus(callback_status, per_description, customerId);
        return result;
    }

    private Date getDateFromString(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date currentDate = sdf.parse(date);
            return currentDate;
        } catch (Exception e) {
            return null;
        }
    }

    private List<CallBackCustomerDTO> convertToDTO(List<CustomerInfo> customerInfoList, Integer cycle) {
        List<CallBackCustomerDTO> callBackCustomerDTOList = new ArrayList<>();

        if (customerInfoList != null) {
            for (CustomerInfo customerInfo : customerInfoList) {
                CallBackCustomerDTO callBackCustomerDTO = new CallBackCustomerDTO();

                callBackCustomerDTO.setId(customerInfo.getId());
                callBackCustomerDTO.setCallbackStatus(customerInfo.getCallbackStatus());
                callBackCustomerDTO.setName(customerInfo.getCustomerName());
                if (customerInfo.getTelephonenumber() != null) {
                    String encodeTelephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber()).substring(0,7) + "****";
                    callBackCustomerDTO.setTelephone(encodeTelephone);
                }
                callBackCustomerDTO.setOwnerUserName(customerInfo.getOwnUserName());

                String subCompanyName = "";
                String companyName = "";

                if (customerInfo.getSubCompanyId() != null) {
                    ThorApiDTO<DepartmentDto> subCompanyDTO =
                            thorService.findDepartmentById("bearer " + publicToken, customerInfo.getSubCompanyId());
                    if (subCompanyDTO.getData() != null) {
                        subCompanyName = subCompanyDTO.getData().getName();
                    }
                }

                if (customerInfo.getCompanyId() != null) {
                    ThorApiDTO<CompanyTheaSystemDto> companyDTO =
                            thorService.selectCompanyById("bearer " + publicToken, customerInfo.getCompanyId());
                    if (companyDTO.getData() != null) {
                        companyName = companyDTO.getData().getCompanyName();
                    }
                }
                callBackCustomerDTO.setCity(customerInfo.getCity());
                callBackCustomerDTO.setSubCompanyName(subCompanyName + "(" + companyName + ")");
                callBackCustomerDTO.setLoanMoney(customerInfo.getLoanAmount().toString());
                callBackCustomerDTO.setType(customerInfo.getCustomerType());
                callBackCustomerDTO.setCallbackStatus(customerInfo.getCallbackStatus());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (customerInfo.getCreateTime() != null) {
                    callBackCustomerDTO.setCreateTime(sdf.format(customerInfo.getCreateTime()));
                }
                if (customerInfo.getCallbackTime() != null) {
                    callBackCustomerDTO.setCallbackTime(sdf.format(customerInfo.getCallbackTime()));
                    callBackCustomerDTO.setOperateStatus(2);
                } else {
                    callBackCustomerDTO.setOperateStatus(1);
                    callBackCustomerDTO.setCallbackTime("空");
                }
                callBackCustomerDTOList.add(callBackCustomerDTO);
            }
        }

        return callBackCustomerDTOList;
    }
}
