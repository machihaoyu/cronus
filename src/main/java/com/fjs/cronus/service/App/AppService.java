package com.fjs.cronus.service.App;

import com.fjs.cronus.dto.App.ReceiveAndKeepCountDTO;
import com.fjs.cronus.dto.CronusDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by msi on 2017/12/28.
 */
@Service
public class AppService {




    public CronusDto<ReceiveAndKeepCountDTO> getReceiveAndKeepCount(Integer customerId){

        CronusDto resultDto = new CronusDto();
        ReceiveAndKeepCountDTO receiveAndKeepCountDTO = new ReceiveAndKeepCountDTO();




        return resultDto;
    }

}
