package com.fjs.cronus.dto.cronus;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yinzf on 2017/10/24.
 */
public class PullCustomerDTO {
    @ApiModelProperty(value = "id（必填）")
    private Integer id;
    @ApiModelProperty(value = "客户名（修改原始盘必填）")
    private String name;
    @ApiModelProperty(value = "手机号码(修改原始盘必填)")
    private String telephone;
    @ApiModelProperty(value = "贷款金额（修改原始盘必填）")
    private BigDecimal loanAmount;
    private Integer saleId;
    @ApiModelProperty(value = "负责人")
    private String ownUserName;
    @ApiModelProperty(value = "负责城市")
    private String city;
    @ApiModelProperty(value = "客户城市")
    private String custCity;
    @ApiModelProperty(value = "房产城市")
    private String custHouseCity;
    @ApiModelProperty(value = "状态")
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间")
    private Date lastUpdateTime;
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

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public String getOwnUserName() {
        return ownUserName;
    }

    public void setOwnUserName(String ownUserName) {
        this.ownUserName = ownUserName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "PullCustomerDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", telephone='" + telephone + '\'' +
                ", loanAmount=" + loanAmount +
                ", saleId=" + saleId +
                ", ownUserName='" + ownUserName + '\'' +
                ", city='" + city + '\'' +
                ", custCity='" + custCity + '\'' +
                ", custHouseCity='" + custHouseCity + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", customerSource='" + customerSource + '\'' +
                ", utmSource='" + utmSource + '\'' +
                '}';
    }
}
