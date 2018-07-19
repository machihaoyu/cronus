package com.fjs.cronus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * b端客户列表
 * Created by yinzf on 2018/7/11.
 */
public class CustomerDTO2 {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "客户姓名")
    private String customerName;
    @ApiModelProperty(value = "手机号码")
    private String telephonenumber;
    @ApiModelProperty(value = "获客媒体")
    private String utmSource;
    @ApiModelProperty(value = "金额")
    private BigDecimal loanAmount;
    @ApiModelProperty(value = "有无房产")
    private String houseStatus;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "是否保留  0不保留1保留")
    private Integer remain;
    @ApiModelProperty(value = "客户状态 (意向客户,协议客户,成交客户)")
    private  String level;
    @ApiModelProperty(value = "跟进状态(暂未接通，无意向，有意向待跟踪，资质差无法操作，\n" +
            "\t\t空号，外地，同业，内部员工，其他)")
    private String cooptionstatus;
    @ApiModelProperty(value = "确认状态(1-没有确认,2-有效客户,3-无效客户,4-老客户无需确认)")
    private Integer confirm;
    @ApiModelProperty(value = "沟通时间 为null就是没有沟通")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date communicateTime;

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

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
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

    public String getCooptionstatus() {
        return cooptionstatus;
    }

    public void setCooptionstatus(String cooptionstatus) {
        this.cooptionstatus = cooptionstatus;
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
}
