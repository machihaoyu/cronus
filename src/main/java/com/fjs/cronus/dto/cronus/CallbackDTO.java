package com.fjs.cronus.dto.cronus;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by msi on 2017/10/12.
 */
public class CallbackDTO implements Serializable {
    @ApiModelProperty(value = "customerId客户id")
    private  Integer customerId;

    @ApiModelProperty(value = "操作描述")
    private  String callbackDescription;
    @ApiModelProperty(value = "callbackStatus回访状态，正常需要问题")
    private  String callbackStatus;
    @ApiModelProperty(value = "备注")
    private  String callbackRemark;
    @ApiModelProperty(value = "问题答案列表")
    private List<QuestionsDTO> question;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCallbackDescription() {
        return callbackDescription;
    }

    public void setCallbackDescription(String callbackDescription) {
        this.callbackDescription = callbackDescription;
    }

    public String getCallbackStatus() {
        return callbackStatus;
    }

    public void setCallbackStatus(String callbackStatus) {
        this.callbackStatus = callbackStatus;
    }

    public String getCallbackRemark() {
        return callbackRemark;
    }

    public void setCallbackRemark(String callbackRemark) {
        this.callbackRemark = callbackRemark;
    }

    public List<QuestionsDTO> getQuestion() {
        return question;
    }

    public void setQuestion(List<QuestionsDTO> question) {
        this.question = question;
    }
}
