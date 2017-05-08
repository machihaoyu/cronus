package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * Created by crm on 2017/4/29.
 */
public class MineTotalDTO extends BaseToStringDTO implements Serializable {

    private static final long serialVersionUID = 4291885347271050604L;

    private Integer customer;//:5,
    private Integer customer_agreement;//:1,
    private Integer customer_contract;//:4,
    private Integer receivables;//:216739

    public Integer getCustomer() {
        return customer;
    }

    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    public Integer getCustomer_agreement() {
        return customer_agreement;
    }

    public void setCustomer_agreement(Integer customer_agreement) {
        this.customer_agreement = customer_agreement;
    }

    public Integer getCustomer_contract() {
        return customer_contract;
    }

    public void setCustomer_contract(Integer customer_contract) {
        this.customer_contract = customer_contract;
    }

    public Integer getReceivables() {
        return receivables;
    }

    public void setReceivables(Integer receivables) {
        this.receivables = receivables;
    }

}
