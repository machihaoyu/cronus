package com.fjs.cronus.dto.cronus;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by msi on 2017/12/8.
 */
public class ImportInfoDTO implements Serializable {

    @ApiModelProperty(value = "客户id",notes = "客户id")
    private Integer id;

    @ApiModelProperty(value = "客户来源",notes = "客户来源")
    private String customerSource;

    @ApiModelProperty(value = "城市",notes = "城市")
    private String city;

    @ApiModelProperty(value = "客户渠道",notes = "客户渠道")
    private String utmSource;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(String customerSource) {
        this.customerSource = customerSource;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }
}
