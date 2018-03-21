package com.fjs.cronus.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 自动清洗屏蔽的公司
 * Created by yinzf on 2017/11/21.
 */
public class CleanMangerCompanyDTO {
    @ApiModelProperty(value = "分公司id（必填）")
    private Integer subCompanyId;

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    @Override
    public String toString() {
        return "CleanMangerCompanyDTO{" +
                "subCompanyId=" + subCompanyId +
                '}';
    }
}

