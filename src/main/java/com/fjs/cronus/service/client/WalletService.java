package com.fjs.cronus.service.client;

import com.fjs.cronus.config.FeignClientConfig;
import com.fjs.cronus.dto.api.WalletApiDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Wallet(房金云 C端-钱包服务) APIs
 * Created by gf on 2018/5/3. url = "http://192.168.1.124:1420",
 */
//@FeignClient(value = "${client.feign.wallet-backend}", url = "http://192.168.1.124:1420")
//@FeignClient(value = "${client.feign.wallet-backend}", configuration = FeignClientConfig.class)
public interface WalletService {

    /**
     * 确认该笔申请有效时调用
     * /apiwithout/v1/theaapi/confirmEffective
     * @param token
     * @param telephone
     * @return
     */
//    @RequestMapping(value = "/apiwithout/v1/theaapi/confirmEffective", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    WalletApiDTO confirmEffective(@RequestHeader("Authorization") String token, @RequestParam(value = "telephone") String telephone);


}
