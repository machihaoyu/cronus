package com.fjs.cronus.service.client;

import com.fjs.cronus.config.FeignClientConfig;
import com.fjs.cronus.dto.loan.LoanDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by msi on 2017/10/11.
 */

/*@FeignClient(value = "${client.feign.loan-backend}", configuration = FeignClientConfig.class)*/
public interface LoanService {


    @RequestMapping(value = " /loan/v1/selectByCustomerId", method = RequestMethod.GET)
    public TheaApiDTO<LoanDTO> selectByCustomerId(@RequestHeader("Authorization") String token, @RequestParam(value = "customerId") Integer customerId);
}
