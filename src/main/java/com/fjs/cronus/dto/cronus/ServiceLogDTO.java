package com.fjs.cronus.dto.cronus;

import java.io.Serializable;
import java.util.List;

/**
 * Created by msi on 2017/12/4.
 */
public class ServiceLogDTO implements Serializable {

    private Integer serviceContracrId;

    private List<ContracrLogDTO> contracrLogDTOS;

    private List<ContracrLogDTO> contracrLogDTOS2;//对应合同的状态

    public List<ContracrLogDTO> getContracrLogDTOS2() {
        return contracrLogDTOS2;
    }

    public void setContracrLogDTOS2(List<ContracrLogDTO> contracrLogDTOS2) {
        this.contracrLogDTOS2 = contracrLogDTOS2;
    }

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
