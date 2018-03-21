package com.fjs.cronus.dto.vipUtm;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by msi on 2018/2/26.
 */
public class OcdcReturnDTO implements Serializable {

    private List<OcdcCustomerDTO> list;

    @ApiModelProperty(value = "总数",notes = "总数")
    private String count ;

    @ApiModelProperty(value = "页数",notes = "页数")
    private Integer p;

    @ApiModelProperty(value = "每页显示",notes = "每页显示")
    private Integer size;

    public List<OcdcCustomerDTO> getList() {
        return list;
    }

    public void setList(List<OcdcCustomerDTO> list) {
        this.list = list;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Integer getP() {
        return p;
    }

    public void setP(Integer p) {
        this.p = p;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
