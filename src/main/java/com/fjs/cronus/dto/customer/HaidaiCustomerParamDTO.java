package com.fjs.cronus.dto.customer;

/**
 * 修改海贷魔方客户信息参数DTO
 * Created by crm on 2017/5/20.
 */
public class HaidaiCustomerParamDTO {

    private Integer user_id;
    private Integer customerId;
    private String telephone;
    private String name;
    private Integer loanAmount;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Integer loanAmount) {
        this.loanAmount = loanAmount;
    }
}
