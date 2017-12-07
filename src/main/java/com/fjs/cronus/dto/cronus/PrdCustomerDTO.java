package com.fjs.cronus.dto.cronus;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by yinzf on 2017/11/1.
 */
public class PrdCustomerDTO {
    private Integer id;
    @ApiModelProperty(value = "客户姓名")
    private String customerName;
    @ApiModelProperty(value = "客户类型")
    private String customerType;
    @ApiModelProperty(value = "手机号码")
    private String telephonenumber;
    @ApiModelProperty(value = "意向金额")
    private BigDecimal loanAmount;
    @ApiModelProperty(value = "所在城市")
    private String city;
    @ApiModelProperty(value = "有无房产")
    private String houseStatus;
    @ApiModelProperty(value = "客户来源")
    private String customerSource;
    @ApiModelProperty(value = "渠道")
    private String utmSource;
    @ApiModelProperty(value = "优先级别")
    private String level;
    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "沟通记录")
    List<PrdComunicationDTO> comunication;
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }



    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHouseStatus() {
        return houseStatus;
    }

    public void setHouseStatus(String houseStatus) {
        this.houseStatus = houseStatus;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public List<PrdComunicationDTO> getComunication() {
        return comunication;
    }

    public void setComunication(List<PrdComunicationDTO> comunication) {
        this.comunication = comunication;
    }

    @Override
    public String toString() {
        return "PrdCustomerDTO{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", customerType='" + customerType + '\'' +
                ", telephonenumber='" + telephonenumber + '\'' +
                ", loanAmount=" + loanAmount +
                ", city='" + city + '\'' +
                ", houseStatus='" + houseStatus + '\'' +
                ", customerSource='" + customerSource + '\'' +
                ", utmSource='" + utmSource + '\'' +
                ", level='" + level + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
