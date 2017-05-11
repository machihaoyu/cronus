package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * Created by crm on 2017/4/29.
 */
public class MineCommissionDTO extends BaseToStringDTO implements Serializable {

    private static final long serialVersionUID = -7699429716403417514L;

    private Double total;
    private Double month;

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getMonth() {
        return month;
    }

    public void setMonth(Double month) {
        this.month = month;
    }
}
