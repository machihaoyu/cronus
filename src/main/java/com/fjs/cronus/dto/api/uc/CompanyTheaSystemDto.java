package com.fjs.cronus.dto.api.uc;

import java.io.Serializable;

/**
 * Created by msi on 2017/10/25.
 */
public class CompanyTheaSystemDto implements Serializable {

    private Integer companyId;

    private String companyName;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
