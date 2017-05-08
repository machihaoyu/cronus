package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * Created by crm on 2017/4/29.
 */
public class MineCustomerDTO extends BaseToStringDTO implements Serializable {

    private static final long serialVersionUID = -272992526292912139L;

    private MineTotalDTO total;
    private MineTotalDTO today;

    public MineTotalDTO getTotal() {
        return total;
    }

    public void setTotal(MineTotalDTO total) {
        this.total = total;
    }

    public MineTotalDTO getToday() {
        return today;
    }

    public void setToday(MineTotalDTO today) {
        this.today = today;
    }

}
