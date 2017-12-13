package com.fjs.cronus.service.thea;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.api.thea.LoanDTO;
import com.fjs.cronus.api.thea.MailDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.uc.AllUserDTO;
import com.fjs.cronus.service.client.TheaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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
    public void inserLoan(LoanDTO loanDTO) {
        TheaApiDTO resultDto = theaService.inserLoan(loanDTO);
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

    public Integer serviceContractToUser(String token,String customerIds,Integer toUser){
        Integer result = 1;
        TheaApiDTO resultDto = theaService.serviceContractToUser(token,customerIds,toUser);
        if (resultDto.getData()!=null){
            result = Integer.valueOf(resultDto.getData().toString());
            if (result == 0){
                return  result;
            }
        }
        return result;
    }
    public Integer cancelAll(String token,String customerId,Integer newOwnnerId){
        Integer result = 1;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("customerId",customerId);
        jsonObject.put("newOwnnerId",newOwnnerId);
        TheaApiDTO resultDto = theaService.cancelAll(token,jsonObject);
        if (resultDto.getData()!=null){
            result = Integer.valueOf(resultDto.getData().toString());
            if (result == 0){
                return  result;
            }
        }
        return result;
    }

}
