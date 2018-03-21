package com.fjs.cronus.dto.cronus;

import java.io.Serializable;
import java.util.List;

/**
 * Created by msi on 2017/12/4.
 */
public class LogArrDTO implements Serializable {


    private Integer customerId;

    private List<ServiceLogDTO> serviceLogDTOS;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public List<ServiceLogDTO> getServiceLogDTOS() {
        return serviceLogDTOS;
    }

    public void setServiceLogDTOS(List<ServiceLogDTO> serviceLogDTOS) {
        this.serviceLogDTOS = serviceLogDTOS;
    }
}
