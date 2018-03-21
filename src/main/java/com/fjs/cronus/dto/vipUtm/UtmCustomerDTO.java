package com.fjs.cronus.dto.vipUtm;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by msi on 2018/2/26.
 */
public class UtmCustomerDTO implements Serializable{

    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "客户姓名")
    private String customerName;
    @ApiModelProperty(value = "称呼男=先生，nv=女士")
    private String sex;
    @ApiModelProperty(value = "手机号码")
    private String telephonenumber;
    @ApiModelProperty(value = "金额")
    private BigDecimal loanAmount;
    @ApiModelProperty(value = "有无房产")
    private String houseStatus;
    @ApiModelProperty(value = "客户来源")
    private String customerSource;
    @ApiModelProperty(value = "渠道")
    private String utmSource;
    @ApiModelProperty(value = "所在城市")
    private String city;
    @ApiModelProperty(value = "拥有人姓名")
    private String ownUserName;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "是否保留  0不保留1保留2已签合同", required = false)
    private Integer remain;

    @ApiModelProperty(value = "客户状态 意向客户 协议客户 成交客户", required = false)
    private  String level;

    @ApiModelProperty(value = "跟进时间", required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date last_update_time;//跟进时间

    @ApiModelProperty(value = "确认状态(1-没有确认,2-有效客户,3-无效客户,4-老客户无需确认)", required = false)
    private Integer confirm;

    @ApiModelProperty(value = "沟通时间 为null就是没有沟通", required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date communicateTime;

    @ApiModelProperty(value = "跟进状态", required = false)
    private String cooptionstatus;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOwnUserName() {
        return ownUserName;
    }

    public void setOwnUserName(String ownUserName) {
        this.ownUserName = ownUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Date getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(Date last_update_time) {
        this.last_update_time = last_update_time;
    }

    public Integer getConfirm() {
        return confirm;
    }

    public void setConfirm(Integer confirm) {
        this.confirm = confirm;
    }

    public Date getCommunicateTime() {
        return communicateTime;
    }

    public void setCommunicateTime(Date communicateTime) {
        this.communicateTime = communicateTime;
    }

    public String getCooptionstatus() {
        return cooptionstatus;
    }

    public void setCooptionstatus(String cooptionstatus) {
        this.cooptionstatus = cooptionstatus;
    }
}
