package com.fjs.cronus.service.client;


import com.fjs.cronus.api.thea.LoanDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Created by msi on 2017/10/11.
 */

//@FeignClient(value = "THEA-BACKEND-ZSC", url = "http://192.168.2.79:8099")
@FeignClient(value = "${client.feign.thea-backend}", url = "http://192.168.1.124:1240")
public interface TheaService {


    @RequestMapping(value = " loan/v1/selectByCustomerId", method = RequestMethod.GET)
    public TheaApiDTO<LoanDTO> selectByCustomerId(@RequestHeader("Authorization") String token, @RequestParam(value = "customerId") Integer customerId);

    @RequestMapping(value = "/config/v1/name", method = RequestMethod.GET)
    public TheaApiDTO<String> getConfigByName(@RequestParam(value = "name") String name);

    @RequestMapping(value = "/loan/v1/insertLoan", method = RequestMethod.POST)
    public TheaApiDTO inserLoan(LoanDTO loanDTO);

    @RequestMapping(value = "/config/v1/name", method = RequestMethod.GET)
    public TheaApiDTO<String> findValueByName(@RequestHeader("Authorization") String token, @RequestParam(value = "name") String name);

    @RequestMapping(value = "/loan/v1/cancelLoanByCustomerId", method = RequestMethod.GET)
    public TheaApiDTO cancelLoanByCustomerId(@RequestHeader("Authorization") String token, @RequestParam(value = "customerId") Integer customerId);

    @RequestMapping(value = "/loan/v1/changeStatusByCustomerId", method = RequestMethod.POST)
    public TheaApiDTO changeStatusByCustomerId(@RequestHeader("Authorization") String token, @RequestParam(value = "customerId") Integer customerId);

}
