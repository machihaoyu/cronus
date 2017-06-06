package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * Created by xulu on 2017/6/5 0005.
 */
public class ElecSignRequestDTO implements Serializable {

    Integer userId;
    String agreementId;
    String code;

    public ElecSignRequestDTO() {}

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
