package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * Created by crm on 2017/4/29.
 */
public class MineAccountDTO extends BaseToStringDTO implements Serializable {

    private static final long serialVersionUID = 4978823136905615447L;

    private Integer receivables;
    private Integer payment;

    public Integer getReceivables() {
        return receivables;
    }

    public void setReceivables(Integer receivables) {
        this.receivables = receivables;
    }

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

}
