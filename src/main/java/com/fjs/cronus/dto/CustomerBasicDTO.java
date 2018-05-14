package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * Created by gf on 2018/5/4.
 */
public class CustomerBasicDTO implements Serializable {
    private Integer id;

    private String telephonenumber;

    private String customerSource;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }

    public String getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(String customerSource) {
        this.customerSource = customerSource;
    }
}
