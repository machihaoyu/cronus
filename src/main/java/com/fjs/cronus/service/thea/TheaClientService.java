package com.fjs.cronus.service.thea;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.api.thea.LoanDTO;
import com.fjs.cronus.api.thea.MailDTO;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.api.crius.CriusApiDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.thea.LoanDTO4;
import com.fjs.cronus.dto.thea.MailBatchDTO;
import com.fjs.cronus.dto.thea.WorkDayDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.client.TheaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by msi on 2017/11/30.
 */
@Service
public class TheaClientService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TheaService theaService;

    @Value("${token.current}")
    private String publicToken;

    public String findValueByName(String token, String name) {

        TheaApiDTO<String> resultDto = theaService.findValueByName(token, name);

        String result = resultDto.getData();

        return result;
    }

    /**
     * 获取交易系统配置
     *
     * @param name
     * @return
     */
    public String getConfigByName(String name) {
        String result = "";
        TheaApiDTO<String> resultDto = theaService.getConfigByName(publicToken, name);
        if (resultDto.getResult() == 0) {
            result = resultDto.getData();
        } else throw new CronusException(CronusException.Type.MESSAGE_CONNECTTHEASYSTEM_ERROR, resultDto.getMessage());
        return result;
    }

    /**
     * 新增交易
     *
     * @param loanDTO
     */
    public String insertLoan(LoanDTO loanDTO, String token) {
        logger.warn("---添加交易Telephonenumber:---"+loanDTO.getTelephonenumber());
        TheaApiDTO resultDto = theaService.insertLoan(loanDTO, token);
        return resultDto.getMessage();
    }

    /**
     * 调用交易系统消息接口
     *
     * @param token
     * @param content
     * @param createUser
     * @param fromId
     * @param fromName
     * @param toId
     */
    public void sendMail(String token, String content, Integer createUser, Integer fromId, String fromName, Integer toId) {

        MailDTO mailDTO = new MailDTO();
        mailDTO.setContent(content);
        mailDTO.setCreateUser(createUser);
        mailDTO.setFromId(fromId);
        mailDTO.setFromName(fromName);
        mailDTO.setToId(toId);
        TheaApiDTO<String> resultDto = theaService.sendMail(token, mailDTO);
    }

    public void sendMailBatch(String token, MailBatchDTO mailBatchDTO) {
        theaService.insertCleanMailBatch(token, mailBatchDTO);
    }

    public Integer serviceContractToUser(String token, String customerIds, Integer toUser) {
        Integer result = 1;
        TheaApiDTO resultDto = theaService.serviceContractToUser(token, customerIds, toUser);
        return result;
    }

    public Integer cancelAll(String token, String customerId, Integer newOwnnerId) {
        Integer result = 1;

        LoanDTO4 loanDTO4 = new LoanDTO4();
        loanDTO4.setIds(customerId);
        loanDTO4.setNewOwnnerId(newOwnnerId);
        TheaApiDTO resultDto = theaService.cancelAll(token, loanDTO4);
        if (resultDto != null) {
            result = Integer.valueOf(resultDto.getResult());
            if (result == 0) {
                return result;
            }
        }
        return result;
    }

    public List<WorkDayDTO> getWorkDay(String token) {
        TheaApiDTO<QueryResult<WorkDayDTO>> resultDto = theaService.getWorkDay(token);
        QueryResult<WorkDayDTO> result = resultDto.getData();
        return result.getRows();
    }


    public Map<String,String> getMediaName(String token,JSONObject jsonObject){
        Map<String, String> map = new HashMap<>();
        CriusApiDTO<Map<String, String>> resultDto = theaService.getMediaName(token,jsonObject);
        map = resultDto.getData();
        return map;
    }

    public List<String> getChannelNameListByMediaName(String token,String mediaName){

        List<String> list = new ArrayList<>();
        TheaApiDTO<List<String>>  resultDto= theaService.getChannelNameListByMediaName(token,mediaName);
        list = resultDto.getData();
        return  list;
    }

    public boolean selectStatusByCustomerIds(String token,String customerIds ){

        boolean flag = false;

        TheaApiDTO<Boolean>  resultDto = theaService.selectStatusByCustomerIds(token,customerIds);
        if (resultDto.getData() != null){
            flag = resultDto.getData();
        }
        return flag;
    }
}
