package com.fjs.cronus.service;

import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.cronus.CustomerListDTO;
import com.fjs.cronus.dto.cronus.PanParamDTO;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.util.EntityToDto;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by msi on 2017/11/30.
 */
@Service
public class PanService {

    @Autowired
    CustomerInfoMapper customerInfoMapper;

    public QueryResult<CustomerListDTO> listByOffer(PanParamDTO pan, Integer userId, Integer companyId , String token, String system,
                                                Integer page, Integer size, List<String> mainCitys, List<Integer> subCompanyIds, Integer type,Integer mountLevle) {

        QueryResult<CustomerListDTO> result = new QueryResult<>();
        Map<String,Object> map=new HashedMap();
        List<CustomerListDTO> resultDto = new ArrayList<>();
        if (pan != null){
            //客户姓名
            if (StringUtils.isNotEmpty(pan.getCustomerName())) {
                map.put("customerName",pan.getCustomerName());
            }
            //电话
            if (StringUtils.isNotEmpty(pan.getTelephonenumber())) {
                map.put("telephonenumber", pan.getTelephonenumber());
            }
            //合作状态
            if (StringUtils.isNotEmpty(pan.getCustomerClassify())) {
                map.put("customer_classify", pan.getCustomerClassify());
            }
            //公司
            if (companyId != null) {
                map.put("companyId", companyId);
            }
            if (StringUtils.isNotEmpty(pan.getUtmSource())) {
                map.put("utmSource",pan.getUtmSource());
            }
            if (StringUtils.isNotEmpty(pan.getCity())) {
                map.put("city",pan.getCity());
            }
            if (StringUtils.isNotEmpty(pan.getCustomerSource())){
                map.put("customer_source",pan.getCustomerSource());
            }
            if (mountLevle != null){
                map.put("mountLevle",mountLevle);
            }
            map.put("mainCitys",mainCitys);
            map.put("subCompanyIds",subCompanyIds);
            map.put("type",type);
            map.put("start",(page-1)*size);
            map.put("size",size);
            List<CustomerInfo> customerInfoList = customerInfoMapper.publicOfferList(map);
            Integer total = customerInfoMapper.publicOfferCount(map);
            if (customerInfoList != null && customerInfoList.size() > 0) {
                for (CustomerInfo customerInfo : customerInfoList){
                    CustomerListDTO customerDto = new CustomerListDTO();
                    EntityToDto.customerEntityToCustomerListDto(customerInfo,customerDto);
                    resultDto.add(customerDto);
                }
            }
            result.setRows(resultDto);
            result.setTotal(total.toString());
        }
        return  result;
    }
}
