package com.fjs.cronus.dto.cronus;

import java.io.Serializable;

/**
 * Created by msi on 2017/11/30.
 */

/**
 * pan.setCustomerName(customerName);
 pan.setTelephonenumber(telephonenumber);
 pan.setHouseStatus(houseStatus);
 pan.setCustomerClassify(customerClassify);
 pan.setCustomerSource(customerSource);
 pan.setCity(city);
 */
public class PanParamDTO implements Serializable{

    private String utmSource;

    private String customerName;

    private String telephonenumber;

    private String houseStatus;

    private String customerClassify;

    private String customerSource;

    private String city;

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }

    public String getHouseStatus() {
        return houseStatus;
    }

    public void setHouseStatus(String houseStatus) {
        this.houseStatus = houseStatus;
    }

    public String getCustomerClassify() {
        return customerClassify;
    }

    public void setCustomerClassify(String customerClassify) {
        this.customerClassify = customerClassify;
    }

    public String getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(String customerSource) {
        this.customerSource = customerSource;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
