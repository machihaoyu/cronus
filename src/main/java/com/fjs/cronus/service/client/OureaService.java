package com.fjs.cronus.service.client;

import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.ourea.CrmPushCustomerDTO;
import com.fjs.cronus.dto.ourea.OureaDTO;
import com.fjs.cronus.dto.thea.MailBatchDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by yinzf on 2018/6/28.
 */
@FeignClient(value = "${client.feign.ourea-backend}")
//@FeignClient(value = "${client.feign.ourea-backend}",url = "192.168.1.124:1460")
public interface OureaService {

    /**
     * 推送客户DD链
     * @param token
     * @param crmPushCustomerDTO
     * @return
     */
    @RequestMapping(value = "/apiwithout/api/Crm/v1/crmPushCustomer", method = RequestMethod.POST)
    OureaDTO crmPushCustomer(@RequestHeader("Authorization") String token, @RequestBody CrmPushCustomerDTO crmPushCustomerDTO);
}
