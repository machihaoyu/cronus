package com.fjs.cronus.service.thea;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.api.thea.LoanDTO;
import com.fjs.cronus.api.thea.MailDTO;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.thea.LoanDTO4;
import com.fjs.cronus.dto.thea.MailBatchDTO;
import com.fjs.cronus.dto.thea.WorkDayDTO;
import com.fjs.cronus.dto.uc.AllUserDTO;
import com.fjs.cronus.model.Mail;
import com.fjs.cronus.service.client.TheaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by msi on 2017/11/30.
 */
@Service
public class TheaClientService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TheaService theaService;


    public String findValueByName(String token, String name) {

        TheaApiDTO<String> resultDto = theaService.findValueByName(token, name);

        String result = resultDto.getData();

        return result;
    }

    /**
     * 获取交易系统配置
     * @param name
     * @return
     */
    public String getConfigByName(String name) {
        TheaApiDTO<String> resultDto = theaService.getConfigByName(name);
        String result = resultDto.getData();
        return result;
    }

    /**
     * 新增交易
     * @param loanDTO
     */
    public void inserLoan(LoanDTO loanDTO,String token) {
        TheaApiDTO resultDto = theaService.inserLoan(loanDTO,token);
    }

    /**
     * 调用交易系统消息接口
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

    public void sendMailBatch(String token,MailBatchDTO mailBatchDTO)
    {
        theaService.insertCleanMailBatch(token, mailBatchDTO);
    }

    public Integer serviceContractToUser(String token,String customerIds,Integer toUser){
        Integer result = 1;
        TheaApiDTO resultDto = theaService.serviceContractToUser(token,customerIds,toUser);
        if (resultDto!=null){
            result = Integer.valueOf(resultDto.getResult());
            if (result == 0){
                return  result;
            }
        }
        return result;
    }
    public Integer cancelAll(String token,String customerId,Integer newOwnnerId){
        Integer result = 1;

        LoanDTO4 loanDTO4 = new LoanDTO4();
        loanDTO4.setIds(customerId);
        loanDTO4.setNewOwnnerId(newOwnnerId);
        TheaApiDTO resultDto = theaService.cancelAll(token,loanDTO4);
        if (resultDto!=null){
            result = Integer.valueOf(resultDto.getResult());
            if (result == 0){
                return  result;
            }
        }
        return result;
    }

    public List<WorkDayDTO> getWorkDay(String token) {
        TheaApiDTO<QueryResult<WorkDayDTO>> resultDto = theaService.getWorkDay(token);
        QueryResult<WorkDayDTO> result = resultDto.getData();
        return result.getRows();
    }

}
