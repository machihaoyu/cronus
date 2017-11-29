package com.fjs.cronus.dto.api.uc;

import java.io.Serializable;

/**
 * Created by msi on 2017/10/31.
 */
public class SubCompanyCityDto implements Serializable {

    private Integer subCompanyId;

    private String SubCompanyName;

    private String cityName;

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getSubCompanyName() {
        return SubCompanyName;
    }

    public void setSubCompanyName(String subCompanyName) {
        SubCompanyName = subCompanyName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "SubCompanyCityDto{" +
                "subCompanyId=" + subCompanyId +
                ", SubCompanyName='" + SubCompanyName + '\'' +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
