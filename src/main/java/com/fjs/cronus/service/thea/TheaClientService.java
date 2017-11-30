package com.fjs.cronus.service.thea;

import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.uc.AllUserDTO;
import com.fjs.cronus.service.client.TheaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by msi on 2017/11/30.
 */
@Service
public class TheaClientService {

    @Autowired
    TheaService theaService;



    public String findValueByName(String token,String name){

        TheaApiDTO<String> resultDto = theaService.findValueByName(token,name);

        String result =  resultDto.getData();

        return  result;
    }

}
