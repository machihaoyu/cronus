package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * Created by xulu on 2017/6/5 0005.
 */
public class ElecContractRequestDTO implements Serializable {

    Integer userId;
    String contractId;
    String code;

    public ElecContractRequestDTO() {}

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
