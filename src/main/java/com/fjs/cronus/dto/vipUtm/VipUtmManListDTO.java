package com.fjs.cronus.dto.vipUtm;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by msi on 2018/2/26.
 */
public class VipUtmManListDTO implements Serializable {


    @ApiModelProperty(value = "主键id", notes = "主键id")
    private Integer id ;

    @ApiModelProperty(value = "业务员id", notes = "业务员id")
    private String userId;

    @ApiModelProperty(value = "业务员姓名", notes = "业务员姓名")
    private String name;

    @ApiModelProperty(value = "业务员手机号", notes = "业务员手机号")
    private String telephone;


    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
