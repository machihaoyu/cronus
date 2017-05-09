package com.fjs.cronus.dto.login;

/**
 * Created by crm on 2017/5/9.
 */
public class AutoAuthorityDTO {

    private Integer product_editAll;
    private Integer contract_abnormal;
    private Integer agreement_addToReceivable;

    public Integer getProduct_editAll() {
        return product_editAll;
    }

    public void setProduct_editAll(Integer product_editAll) {
        this.product_editAll = product_editAll;
    }

    public Integer getContract_abnormal() {
        return contract_abnormal;
    }

    public void setContract_abnormal(Integer contract_abnormal) {
        this.contract_abnormal = contract_abnormal;
    }

    public Integer getAgreement_addToReceivable() {
        return agreement_addToReceivable;
    }

    public void setAgreement_addToReceivable(Integer agreement_addToReceivable) {
        this.agreement_addToReceivable = agreement_addToReceivable;
    }
}
