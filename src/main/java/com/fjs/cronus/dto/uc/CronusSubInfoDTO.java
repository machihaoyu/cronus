package com.fjs.cronus.dto.uc;

import java.io.Serializable;

/**
 * Created by msi on 2018/1/4.
 */
public class CronusSubInfoDTO implements Serializable{

    private Integer id;

    private Integer subCompanyId;


    private String cityName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
