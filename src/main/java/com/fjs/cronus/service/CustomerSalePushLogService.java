package com.fjs.cronus.service;

import com.fjs.cronus.dto.BasePageableVO;
import com.fjs.cronus.mappers.CustomerSalePushLogMapper;
import com.fjs.cronus.model.CustomerSalePushLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2017/9/18.
 */
@Service
public class CustomerSalePushLogService {

    @Autowired
    private CustomerSalePushLogMapper customerSalePushLogMapper;

    /**
     * 批量添加日志
     *
     * @param customerSalePushLogList
     * @return
     */
    public void insertList(List<CustomerSalePushLog> customerSalePushLogList) {
        customerSalePushLogMapper.insertList(customerSalePushLogList);
//        return count;
    }

    public Map<String, Object> findPageData(CustomerSalePushLog params, Integer pageNum, Integer pageSize) {
        Map<String, Object> result = new HashMap<>();

        Integer tempPageNum = 0;
        if (pageNum != null && pageNum >= 1) {
            tempPageNum = pageNum - 1;
        }
        if (pageSize == null || pageSize <= 0) pageSize = 10;
        params = params == null ? new CustomerSalePushLog() : params;

        Long total = customerSalePushLogMapper.getPageDataCount(params);
        total = total == null ? 0 : total;
        List<CustomerSalePushLog> pageData = null;
        if (total > 0) {
            pageData = customerSalePushLogMapper.findPageData(params, tempPageNum * pageSize, pageSize);
        }

        result.put("count", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        result.put("pageCount", BasePageableVO.calculate(total.intValue(), pageSize));
        result.put("list", pageData);
        return result;
    }
}
