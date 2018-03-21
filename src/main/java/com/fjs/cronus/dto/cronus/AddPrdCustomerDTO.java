package com.fjs.cronus.dto.cronus;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Created by msi on 2017/12/6.
 */
public class AddPrdCustomerDTO implements Serializable {

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
    @ApiModelProperty(value = "沟通内容")
    private String content;
    @ApiModelProperty(value = "转入标识（0不转入 1转入）")
    private Integer type;
    @ApiModelProperty(value = "性别")
    private String sex;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
