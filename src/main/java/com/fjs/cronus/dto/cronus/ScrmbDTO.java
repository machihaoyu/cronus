package com.fjs.cronus.dto.cronus;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by msi on 2017/12/22.
 */
public class ScrmbDTO  implements Serializable{

    @ApiModelProperty(value = "客户id", required = false)
    private Integer customerId;

    @ApiModelProperty(value = "确认状态(1-没有确认,2-有效客户,3-无效客户,4-老客户无需确认)", required = false)
    private Integer confirm;

    @ApiModelProperty(value = "沟通时间 为null就是没有沟通", required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date communicateTime;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
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
