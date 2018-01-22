package com.fjs.cronus.service.client;

import com.fjs.cronus.api.thea.Config;
import com.fjs.cronus.config.FeignClientConfig;
import com.fjs.cronus.dto.Echo.EchoDTO;
import com.fjs.cronus.dto.Echo.MsgTmplDTO;
import com.fjs.cronus.dto.Echo.StationMsgReqDTO;
import com.fjs.cronus.dto.api.crius.CriusApiDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Created by msi on 2018/1/17.
 */

@FeignClient(value = "${client.feign.echo-backend}", configuration = FeignClientConfig.class)
//@FeignClient(value = "${client.feign.echo-backend}", url = "http://192.168.1.128:1330")
public interface EchoClientService {


    @RequestMapping(value = "/apiwithout/v1/queryMsgTmpl", method = RequestMethod.GET)
    public EchoDTO<MsgTmplDTO> queryMsgTmpl(@RequestHeader("Authorization") String token, @RequestParam(value = "tmplType") String  tmplType);



    @RequestMapping(value = "/api/v1/addStationMsg", method = RequestMethod.POST)
    public String addStationMsg(@RequestHeader("Authorization") String token, @RequestBody StationMsgReqDTO stationMsgReqDTO);


}
