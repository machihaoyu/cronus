package com.fjs.cronus.dto.uc;

import java.util.List;

/**
 * Created by msi on 2017/10/24.
 */
public class CrmCitySubCompanyDto {


    private String city;

    private List<CrmSubcompanyDto> subcompanys;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<CrmSubcompanyDto> getSubcompanys() {
        return subcompanys;
    }

    public void setSubcompanys(List<CrmSubcompanyDto> subcompanys) {
        this.subcompanys = subcompanys;
    }
}
