package com.fjs.cronus.dto.saas;

/**
 * Created by Administrator on 2017/7/14 0014.
 */
public class SaasIndexDTO {

    private String registerNum;    //12,注册人数
    private String applyNum;    //12, 申请人数
    private String customerNum;    //13, 总意向客户数
    private String agreementNum;    //14, 总协议数
    private String contractNum;    //15,  总合同书
    private String loanNum;    //16,     总贷款金额(元)
    private String receivablesMoney;    //17,总还款数,
    private String allCustomerNum;    //18, 已推送用户
    private String allProductNum;    //19   总产品数

    public String getRegisterNum() {
        return registerNum;
    }

    public void setRegisterNum(String registerNum) {
        this.registerNum = registerNum;
    }

    public String getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(String applyNum) {
        this.applyNum = applyNum;
    }

    public String getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(String customerNum) {
        this.customerNum = customerNum;
    }

    public String getAgreementNum() {
        return agreementNum;
    }

    public void setAgreementNum(String agreementNum) {
        this.agreementNum = agreementNum;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getLoanNum() {
        return loanNum;
    }

    public void setLoanNum(String loanNum) {
        this.loanNum = loanNum;
    }

    public String getReceivablesMoney() {
        return receivablesMoney;
    }

    public void setReceivablesMoney(String receivablesMoney) {
        this.receivablesMoney = receivablesMoney;
    }

    public String getAllCustomerNum() {
        return allCustomerNum;
    }

    public void setAllCustomerNum(String allCustomerNum) {
        this.allCustomerNum = allCustomerNum;
    }

    public String getAllProductNum() {
        return allProductNum;
    }

    public void setAllProductNum(String allProductNum) {
        this.allProductNum = allProductNum;
    }
}
