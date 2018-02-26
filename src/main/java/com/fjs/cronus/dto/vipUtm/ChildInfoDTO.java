package com.fjs.cronus.dto.vipUtm;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by msi on 2018/2/26.
 */
public class ChildInfoDTO implements Serializable {

    @ApiModelProperty(value = "主键id",notes = "主键id")
    private Integer id;

    @ApiModelProperty(value = "渠道名称",notes = "渠道名称")
    private String utmSource;

    @ApiModelProperty(value = "创建时间",notes = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
