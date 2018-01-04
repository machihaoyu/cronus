package com.fjs.cronus.service.thea;

import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.api.SimpleUserInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.client.ThorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThorClientService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ThorService thorService;


    public SimpleUserInfoDTO getUserInfoById(String token, Integer userId)
    {
        CronusDto<SimpleUserInfoDTO> cronusDto = thorService.getUserInfoById(token,userId);
        if (cronusDto.getResult()==0 && cronusDto.getData()!=null)
        {
            return cronusDto.getData();
        }
        else {
            logger.warn(cronusDto.getMessage());
            throw new CronusException(CronusException.Type.MESSAGE_CONNECT_THOR_SYSTEM_ERROR, cronusDto.getMessage());
        }
    }


}
