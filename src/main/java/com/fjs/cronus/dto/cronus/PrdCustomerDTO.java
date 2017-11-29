package com.fjs.cronus.dto.cronus;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yinzf on 2017/11/1.
 */
public class PrdCustomerDTO {
    private Integer id;
    @ApiModelProperty(value = "客户姓名")
    private String customerName;
    @ApiModelProperty(value = "客户类型")
    private String customerType;
    @ApiModelProperty(value = "性别")
    private String c_sex;
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
    @ApiModelProperty(value = "跟进时间", required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date communicateTime;
    @ApiModelProperty(value = "客户id")
    private Integer c_id;
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

    public String getC_sex() {
        return c_sex;
    }

    public void setC_sex(String c_sex) {
        this.c_sex = c_sex;
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

    public Date getCommunicateTime() {
        return communicateTime;
    }

    public void setCommunicateTime(Date communicateTime) {
        this.communicateTime = communicateTime;
    }

    public Integer getC_id() {
        return c_id;
    }

    public void setC_id(Integer c_id) {
        this.c_id = c_id;
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

    @Override
    public String toString() {
        return "PrdCustomerDTO{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", customerType='" + customerType + '\'' +
                ", c_sex='" + c_sex + '\'' +
                ", telephonenumber='" + telephonenumber + '\'' +
                ", loanAmount=" + loanAmount +
                ", city='" + city + '\'' +
                ", houseStatus='" + houseStatus + '\'' +
                ", customerSource='" + customerSource + '\'' +
                ", utmSource='" + utmSource + '\'' +
                ", level='" + level + '\'' +
                ", content='" + content + '\'' +
                ", communicateTime=" + communicateTime +
                ", c_id=" + c_id +
                ", type=" + type +
                ", sex='" + sex + '\'' +
                '}';
    }
}
