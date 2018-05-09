package com.fjs.cronus.service.client;


import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.api.thea.Config;
import com.fjs.cronus.api.thea.LoanDTO;
import com.fjs.cronus.api.thea.MailDTO;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.api.crius.CriusApiDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.thea.*;
import org.apache.tools.ant.taskdefs.Get;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created by msi on 2017/10/11. url = "http://192.168.1.124:1240"
 */

@FeignClient(value = "${client.feign.thea-backend}")
//@FeignClient(value = "${client.feign.thea-backend}",url = "192.168.1.124:1240")
public interface TheaService {


    @RequestMapping(value = " loan/v1/selectByCustomerId", method = RequestMethod.GET)
    public TheaApiDTO<LoanDTO> selectByCustomerId(@RequestHeader("Authorization") String token, @RequestParam(value = "customerId") Integer customerId);

    @RequestMapping(value = "/config/v1/name", method = RequestMethod.GET)
    public TheaApiDTO<String> getConfigByName(@RequestHeader("Authorization") String token,@RequestParam(value = "name") String name);

    @RequestMapping(value = "/loan/v1/insertLoan", method = RequestMethod.POST)
    public TheaApiDTO insertLoan(@RequestBody LoanDTO loanDTO, @RequestHeader("Authorization")String token);

    @RequestMapping(value = "/config/v1/special/name", method = RequestMethod.GET)
    public TheaApiDTO<String> findValueByName(@RequestHeader("Authorization") String token, @RequestParam(value = "name") String name);

    @RequestMapping(value = "/loan/v1/cancelLoanByCustomerId", method = RequestMethod.GET)
    public TheaApiDTO cancelLoanByCustomerId(@RequestHeader("Authorization") String token, @RequestParam(value = "customerIds") String customerIds);

    @RequestMapping(value = "/loan/v1/changeStatusByCustomerId", method = RequestMethod.POST)
    public TheaApiDTO changeStatusByCustomerId(@RequestHeader("Authorization") String token, @RequestBody JSONObject jsonObject);

    @RequestMapping(value = "/mail/v1/sendMail", method = RequestMethod.POST)
    TheaApiDTO<String> sendMail(@RequestHeader("Authorization") String token,@RequestBody MailDTO mailDTO);

    @RequestMapping(value = "/mail/v1/insertCleanMailBatch", method = RequestMethod.POST)
    TheaApiDTO<Integer> insertCleanMailBatch(@RequestHeader("Authorization") String token,@RequestBody MailBatchDTO mailBatchDTO);

    @RequestMapping(value = "/serviceContract/v1/serviceContract/cronus/toUser")
    TheaApiDTO serviceContractToUser(@RequestHeader("Authorization") String token,@RequestParam(value = "customerIds") String customerIds,@RequestParam(value = "toUser")Integer toUser);

    @RequestMapping(value = "/loan/v1/cancelAll")
    TheaApiDTO cancelAll(@RequestHeader("Authorization") String token,@RequestBody LoanDTO4 loanDTO4);

    @RequestMapping(value = "/config/v1/modify", method = RequestMethod.POST)
    public TheaApiDTO<Integer> updatebConfig(@RequestHeader("Authorization") String token, @RequestBody Config config);

//    @RequestMapping(value = "/config/v1/modify", method = RequestMethod.GET)
//    CriusApiDTO<Integer> updateConfig(@RequestParam("id") Integer id, @RequestParam("value") String value);

    @RequestMapping(value = "/config/v1/findByName", method = RequestMethod.GET)
    public TheaApiDTO<Config> findByName(@RequestHeader("Authorization") String token, @RequestParam(value = "name") String name);

    @RequestMapping(value = "/api/v1/workDayList?page=1&size=20", method = RequestMethod.GET)
    TheaApiDTO<QueryResult<WorkDayDTO>> getWorkDay(@RequestHeader("Authorization") String token);

    @RequestMapping(value = "/config/v1/addConfig", method = RequestMethod.POST)
    public CriusApiDTO addConfig(@RequestHeader("Authorization") String token,@RequestBody Config config);

    @RequestMapping(value = "/channel/v1/getMediaName", method = RequestMethod.POST)
    @ResponseBody
    public CriusApiDTO<Map<String, String>> getMediaName(@RequestHeader("Authorization") String token,@RequestBody JSONObject jsonObject);


    @RequestMapping(value = "/channel/v1/getMediaName", method = RequestMethod.GET)
    @ResponseBody
    public TheaApiDTO<List<String>> getChannelNameListByMediaName(@RequestHeader("Authorization") String token,@RequestParam(value = "mediaName") String mediaName);

    @RequestMapping(value = "/loan/v1/selectStatusByCustomerIds",method = RequestMethod.GET)
    @ResponseBody
    public TheaApiDTO selectStatusByCustomerIds(@RequestHeader("Authorization") String token,@RequestParam(value = "customerIds") String customerIds);

    @RequestMapping(value = "/loan/v1/deleteLoanByCustomerId",method = RequestMethod.POST)
    @ResponseBody
    public TheaApiDTO deleteLoanByCustomerId(@RequestHeader("Authorization") String token, @RequestBody AllLoanDTO allLoanDTO);

    @RequestMapping(value = "/loan/v1/invalidLoans", method = RequestMethod.POST)
    TheaApiDTO invalidLoans(@RequestHeader("Authorization")String token,@RequestBody String customerIds);

    @RequestMapping(value = "/loan/v1/addLoan", method = RequestMethod.POST)
    public TheaApiDTO addLoan(@RequestBody LoanDTO6 loanDTO, @RequestHeader("Authorization")String token);

    @RequestMapping(value = "/media/v1/getAllMedia",method = RequestMethod.GET)
    TheaApiDTO getAllMedia(String token);

    @RequestMapping(value = "/channel/v1/getInfoByChannelName", method = RequestMethod.POST)
    TheaApiDTO<BaseChannelDTO> getInfoByChannelName(JSONObject jsonObject);
}
