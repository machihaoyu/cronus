package com.fjs.cronus.dto.cronus;

import com.fjs.cronus.dto.api.uc.SubCompanyDto;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by msi on 2017/12/4.
 */
public class SellUserDTO implements Serializable{


    private String urlComeFrom;

    @ApiModelProperty(value = "可操作的公司",notes = "可操作的公司")
    private List<SubCompanyDto> company;


    public String getUrlComeFrom() {
        return urlComeFrom;
    }

    public void setUrlComeFrom(String urlComeFrom) {
        this.urlComeFrom = urlComeFrom;
    }

    public List<SubCompanyDto> getCompany() {
        return company;
    }

    public void setCompany(List<SubCompanyDto> company) {
        this.company = company;
    }
}
