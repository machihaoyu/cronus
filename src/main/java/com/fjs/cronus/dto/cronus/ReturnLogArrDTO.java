package com.fjs.cronus.dto.cronus;

import com.fjs.cronus.dto.api.crius.Contract;
import com.fjs.cronus.dto.api.crius.ServiceContract;
import com.fjs.cronus.model.CustomerInfo;

import java.io.Serializable;
import java.util.List;

public class ReturnLogArrDTO  implements Serializable{


    private CustomerInfo customerInfo;

    private List<Contract> contractInfos1;

    private List<Contract> contractInfos2;

    private List<ServiceContract> agreementInfos;


    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public List<Contract> getContractInfos1() {
        return contractInfos1;
    }

    public void setContractInfos1(List<Contract> contractInfos1) {
        this.contractInfos1 = contractInfos1;
    }

    public List<Contract> getContractInfos2() {
        return contractInfos2;
    }

    public void setContractInfos2(List<Contract> contractInfos2) {
        this.contractInfos2 = contractInfos2;
    }

    public List<ServiceContract> getAgreementInfos() {
        return agreementInfos;
    }

    public void setAgreementInfos(List<ServiceContract> agreementInfos) {
        this.agreementInfos = agreementInfos;
    }
}
