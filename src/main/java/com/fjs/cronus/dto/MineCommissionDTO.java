package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * Created by crm on 2017/4/29.
 */
public class MineCommissionDTO extends BaseToStringDTO implements Serializable {

    private static final long serialVersionUID = -7699429716403417514L;

    private Integer total;
    private Integer month;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

}
