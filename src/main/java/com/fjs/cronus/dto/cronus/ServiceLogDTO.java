package com.fjs.cronus.dto.cronus;

import java.io.Serializable;
import java.util.List;

/**
 * Created by msi on 2017/12/4.
 */
public class ServiceLogDTO implements Serializable {

    private Integer serviceContracrId;

    private List<ContracrLogDTO> contracrLogDTOS;

    public Integer getServiceContracrId() {
        return serviceContracrId;
    }

    public void setServiceContracrId(Integer serviceContracrId) {
        this.serviceContracrId = serviceContracrId;
    }

    public List<ContracrLogDTO> getContracrLogDTOS() {
        return contracrLogDTOS;
    }

    public void setContracrLogDTOS(List<ContracrLogDTO> contracrLogDTOS) {
        this.contracrLogDTOS = contracrLogDTOS;
    }
}
