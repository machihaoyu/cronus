package com.fjs.cronus.dto.thea;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yinzf on 2017/10/17.
 */
public class CommunicationLogDTO {
    @ApiModelProperty(value = "有无房产", required = false)
    private String houseStatus;
    @ApiModelProperty(value = "确认贷款金额", required = false)
    private BigDecimal loanAmount;
    @ApiModelProperty(value = "资金用途", required = false)
    private String purpose;
    @ApiModelProperty(value = "创建时间", required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    public String getHouseStatus() {
        return houseStatus;
    }

    public void setHouseStatus(String houseStatus) {
        this.houseStatus = houseStatus;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
