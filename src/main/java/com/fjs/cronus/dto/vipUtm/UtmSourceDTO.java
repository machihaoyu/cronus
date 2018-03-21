package com.fjs.cronus.dto.vipUtm;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by msi on 2018/2/26.
 */
public class UtmSourceDTO implements Serializable {

    @ApiModelProperty(value = "业务员姓名", notes = "业务员姓名")
    private String name;

    @ApiModelProperty(value = "业务员编号", notes = "业务员编号")
    private String userId;

    @ApiModelProperty(value = "渠道名称",notes = "渠道名称")
    private String utmSource;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
