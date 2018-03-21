package com.fjs.cronus.dto.api.crius;


import com.fjs.cronus.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by pc on 2017/9/14.
 */
public class Contract extends BaseModel implements Serializable{
    @ApiModelProperty(value = "合同编号")
    @NotNull
    @NotBlank
    private String contractNumber;
    @ApiModelProperty(value = "服务合同ID")
    private Integer serviceContractId;
    @ApiModelProperty(value = "借款人")
    @NotNull
    @NotBlank
    private String borrower;
    @ApiModelProperty(value = "借款人身份证")
    @NotNull
    @NotBlank
    @Size(min = 15,max = 18)
    private String identity;
    @ApiModelProperty(value = "户籍地址")
    @NotNull
    @NotBlank
    private String perAddress;
    @ApiModelProperty(value = "联系电话")
    @NotNull
    @NotBlank
    private String phone;
    @ApiModelProperty(value = "联系地址")
    @NotNull
    @NotBlank
    private String address;
    @ApiModelProperty(value = "合同类型")
    @NotNull
    @FlagValidator(productType = {1,2,4})
    private Integer contractType;
    @ApiModelProperty(value = "产品类型")
    @NotNull
    @NotBlank
    private String productType;
    @ApiModelProperty(value = "产品名称")
    @NotNull
    @NotBlank
    private String productName;
    @ApiModelProperty(value = "渠道名称")
    private String channelName;
    @ApiModelProperty(value = "借款金额")
    @NotNull
    @DecimalMin("0")
    private BigDecimal borrowMoney;
    @ApiModelProperty(value = "借款期限")
    @NotNull
    @Min(0)
    private Integer duration;
    @ApiModelProperty(value = "月利率")
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal monthRate;
    @ApiModelProperty(value = "年利率")
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal yearRate;
    @ApiModelProperty(value = "管理费")
    private BigDecimal mangerMoney;
    @ApiModelProperty(value = "返费支出总值")
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal channelMoney;
    @ApiModelProperty(value = "材料费总值")
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal packing;
    @ApiModelProperty(value = "返费收入总值")
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal returnFee;
    @ApiModelProperty(value = "服务费总值")
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal serviceMoney;
    @ApiModelProperty(value = "还款方式")
    @NotNull
    @Min(1)
    @Max(4)
    private Integer payType;
    @ApiModelProperty(value = "抵押:房产地址")
    @NotNull
    @NotBlank
    @Length(min = 1,max = 100)
    private String houseAddress;
    @ApiModelProperty(value = "抵押:户龄")
    @NotNull
    private BigDecimal houseAge;
    @ApiModelProperty(value = "抵押:房产面积")
    @NotNull
    private BigDecimal houseArea;
    @ApiModelProperty(value = "抵押:房产估值")
    @NotNull
    private BigDecimal houseValue;
    @ApiModelProperty(value = "原始创建人")
    private Integer oldCreateUserId;
    @ApiModelProperty(value = "到期时间")
    @NotNull
    @Future
    private Date expireTime;
    @ApiModelProperty(value = "合同状态")
    private Integer status;
    @ApiModelProperty(value = "放款金额")
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal giveMoney;
    @ApiModelProperty(value = "放款时间")
    @NotNull
    @Future
    private Date giveTime;
    @ApiModelProperty(value = "放款金额二")
    private BigDecimal giveMoneyTwo;
    @ApiModelProperty(value = "放款时间二")
    private Date giveTimeTwo;
    @ApiModelProperty(value = "审核流程")
    private Integer checkProcess;
    @ApiModelProperty(value = "0:提交  1:驳回")
    private Integer checkStatus;
    @ApiModelProperty(value = "审核轮数")
    private Integer checkNum;
    @ApiModelProperty(value = "多个参与人")
    private String partake;
    @ApiModelProperty(value = "成本期限单位")
    private String durationUnit;
    @ApiModelProperty(value = "收益年利率")
    private BigDecimal sYearRate;
    @ApiModelProperty(value = "收益期限")
    private BigDecimal sDuration;
    @ApiModelProperty(value = "收益期限单位")
    private String sDurationUnit;
    @ApiModelProperty(value = "是否利差合同")
    private Integer isSpecial;


    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Integer getServiceContractId() {
        return serviceContractId;
    }

    public void setServiceContractId(Integer serviceContractId) {
        this.serviceContractId = serviceContractId;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPerAddress() {
        return perAddress;
    }

    public void setPerAddress(String perAddress) {
        this.perAddress = perAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getContractType() {
        return contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public BigDecimal getBorrowMoney() {
        return borrowMoney;
    }

    public void setBorrowMoney(BigDecimal borrowMoney) {
        this.borrowMoney = borrowMoney;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public BigDecimal getMonthRate() {
        return monthRate;
    }

    public void setMonthRate(BigDecimal monthRate) {
        this.monthRate = monthRate;
    }

    public BigDecimal getYearRate() {
        return yearRate;
    }

    public void setYearRate(BigDecimal yearRate) {
        this.yearRate = yearRate;
    }

    public BigDecimal getMangerMoney() {
        return mangerMoney;
    }

    public void setMangerMoney(BigDecimal mangerMoney) {
        this.mangerMoney = mangerMoney;
    }

    public BigDecimal getChannelMoney() {
        return channelMoney;
    }

    public void setChannelMoney(BigDecimal channelMoney) {
        this.channelMoney = channelMoney;
    }

    public BigDecimal getPacking() {
        return packing;
    }

    public void setPacking(BigDecimal packing) {
        this.packing = packing;
    }

    public BigDecimal getReturnFee() {
        return returnFee;
    }

    public void setReturnFee(BigDecimal returnFee) {
        this.returnFee = returnFee;
    }

    public BigDecimal getServiceMoney() {
        return serviceMoney;
    }

    public void setServiceMoney(BigDecimal serviceMoney) {
        this.serviceMoney = serviceMoney;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }

    public BigDecimal getHouseAge() {
        return houseAge;
    }

    public void setHouseAge(BigDecimal houseAge) {
        this.houseAge = houseAge;
    }

    public BigDecimal getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(BigDecimal houseArea) {
        this.houseArea = houseArea;
    }

    public BigDecimal getHouseValue() {
        return houseValue;
    }

    public void setHouseValue(BigDecimal houseValue) {
        this.houseValue = houseValue;
    }

    public Integer getOldCreateUserId() {
        return oldCreateUserId;
    }

    public void setOldCreateUserId(Integer oldCreateUserId) {
        this.oldCreateUserId = oldCreateUserId;
    }


    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getGiveMoney() {
        return giveMoney;
    }

    public void setGiveMoney(BigDecimal giveMoney) {
        this.giveMoney = giveMoney;
    }

    public Date getGiveTime() {
        return giveTime;
    }

    public void setGiveTime(Date giveTime) {
        this.giveTime = giveTime;
    }

    public BigDecimal getGiveMoneyTwo() {
        return giveMoneyTwo;
    }

    public void setGiveMoneyTwo(BigDecimal giveMoneyTwo) {
        this.giveMoneyTwo = giveMoneyTwo;
    }

    public Date getGiveTimeTwo() {
        return giveTimeTwo;
    }

    public void setGiveTimeTwo(Date giveTimeTwo) {
        this.giveTimeTwo = giveTimeTwo;
    }

    public Integer getCheckProcess() {
        return checkProcess;
    }

    public void setCheckProcess(Integer checkProcess) {
        this.checkProcess = checkProcess;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Integer getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(Integer checkNum) {
        this.checkNum = checkNum;
    }

    public String getPartake() {
        return partake;
    }

    public void setPartake(String partake) {
        this.partake = partake;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public BigDecimal getsYearRate() {
        return sYearRate;
    }

    public void setsYearRate(BigDecimal sYearRate) {
        this.sYearRate = sYearRate;
    }

    public BigDecimal getsDuration() {
        return sDuration;
    }

    public void setsDuration(BigDecimal sDuration) {
        this.sDuration = sDuration;
    }

    public String getsDurationUnit() {
        return sDurationUnit;
    }

    public void setsDurationUnit(String sDurationUnit) {
        this.sDurationUnit = sDurationUnit;
    }

    public Integer getIsSpecial() {
        return isSpecial;
    }

    public void setIsSpecial(Integer isSpecial) {
        this.isSpecial = isSpecial;
    }

}
