package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * Created by xulu on 2017/6/6 0006.
 */
public class SignDTO implements Serializable {

    private String customer_seal;
    private String company_seal;

    public SignDTO() {
    }

    public String getCustomer_seal() {
        return customer_seal;
    }

    public void setCustomer_seal(String customer_seal) {
        this.customer_seal = customer_seal;
    }

    public String getCompany_seal() {
        return company_seal;
    }

    public void setCompany_seal(String company_seal) {
        this.company_seal = company_seal;
    }
}
