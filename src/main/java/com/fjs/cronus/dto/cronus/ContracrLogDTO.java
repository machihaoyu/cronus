package com.fjs.cronus.dto.cronus;

import java.io.Serializable;

/**
 * Created by msi on 2017/12/4.
 */
public class ContracrLogDTO implements Serializable {

    private Integer contractId;

    private Integer status;


    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
