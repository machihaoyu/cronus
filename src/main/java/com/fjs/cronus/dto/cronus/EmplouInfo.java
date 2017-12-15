package com.fjs.cronus.dto.cronus;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by msi on 2017/11/28.
 */
public class EmplouInfo implements Serializable{

    @ApiModelProperty(value = "企业名称",notes = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "成立年限",notes = "成立年限")
    private String years;

    @ApiModelProperty(value = "年流水",notes = "年流水")
    private String turnover;

    @ApiModelProperty(value = "注册资本",notes = "注册资本")
    private String registerMoney;

    @ApiModelProperty(value = "认缴资本",notes = "认缴资本")
    private String subscribedMoney;

    @ApiModelProperty(value = "角色1法人 2股东 3高管",notes = "角色1法人 2股东 3高管")
    private Integer roles;

    @ApiModelProperty(value = "角色2股东占股多少",notes = "角色2股东占股多少")
    private String shares;
    @ApiModelProperty(value = "角色3高管 职位",notes = "角色3高管 职位")
    private String position;

    @ApiModelProperty(value = "经营状态：1存续，2在业，3吊销，4注销，5迁出，6迁入，7停业，8清算",notes = "经营状态：1存续，2在业，3吊销，4注销，5迁出，6迁入，7停业，8清算")
    private Integer status;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

    public String getRegisterMoney() {
        return registerMoney;
    }

    public void setRegisterMoney(String registerMoney) {
        this.registerMoney = registerMoney;
    }

    public String getSubscribedMoney() {
        return subscribedMoney;
    }

    public void setSubscribedMoney(String subscribedMoney) {
        this.subscribedMoney = subscribedMoney;
    }

    public Integer getRoles() {
        return roles;
    }

    public void setRoles(Integer roles) {
        this.roles = roles;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
