package com.fjs.cronus.dto.cronus;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by msi on 2017/12/16.
 */
public class AddPullCustomerDTO implements Serializable {
    @ApiModelProperty(value = "id（必填）")
    private Integer id;
    @ApiModelProperty(value = "客户名（修改原始盘必填）")
    private String name;
    @ApiModelProperty(value = "手机号码(修改原始盘必填)")
    private String telephone;
    @ApiModelProperty(value = "贷款金额（修改原始盘必填）")
    private BigDecimal loanAmount;
    @ApiModelProperty(value = "客户城市")
    private String custCity;
    @ApiModelProperty(value = "房产城市")
    private String custHouseCity;
    @ApiModelProperty(value = "状态 转入crm状态: 0正常， -1重复，-2无效,  1转入 crm成功 ")
    private Integer status;
    @ApiModelProperty(value = "客户来源")
    private String customerSource;
    @ApiModelProperty(value = "渠道来源")
    private String utmSource;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getCustCity() {
        return custCity;
    }

    public void setCustCity(String custCity) {
        this.custCity = custCity;
    }

    public String getCustHouseCity() {
        return custHouseCity;
    }

    public void setCustHouseCity(String custHouseCity) {
        this.custHouseCity = custHouseCity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(String customerSource) {
        this.customerSource = customerSource;
    }

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }
}
