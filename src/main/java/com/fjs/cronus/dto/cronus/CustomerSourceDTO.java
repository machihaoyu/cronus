package com.fjs.cronus.dto.cronus;

import com.fjs.cronus.dto.api.uc.SubCompanyDto;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by msi on 2017/12/2.
 */
public class CustomerSourceDTO implements Serializable {

    @ApiModelProperty(value = "来源",notes = "来源")
    private List<String> source;


    @ApiModelProperty(value = "总公司",notes = "总公司")
    private List<SubCompanyDto> companyDtos;

    public List<String> getSource() {
        return source;
    }

    public void setSource(List<String> source) {
        this.source = source;
    }

    public List<SubCompanyDto> getCompanyDtos() {
        return companyDtos;
    }

    public void setCompanyDtos(List<SubCompanyDto> companyDtos) {
        this.companyDtos = companyDtos;
    }
}
