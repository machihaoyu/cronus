package com.fjs.cronus.service.uc;

import com.fjs.cronus.dto.loan.LoanDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.client.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by msi on 2017/10/11.
 */

@Service
public class LoaService {

    @Autowired
    LoanService loanService;
    public LoanDTO selectByCustomerId( String token,Integer customerId){

        LoanDTO loanDTO = new LoanDTO();
        if (customerId == null){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
       TheaApiDTO<LoanDTO> resultDto = loanService.selectByCustomerId(token,customerId);

        if (resultDto.getData() == null){
            throw new CronusException(CronusException.Type.CRM_DATAAUTH_ERROR);
        }
        loanDTO = resultDto.getData();
        return  loanDTO;
    }

}
