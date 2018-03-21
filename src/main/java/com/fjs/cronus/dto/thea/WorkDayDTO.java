package com.fjs.cronus.dto.thea;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by msi on 2017/10/16.
 */
public class WorkDayDTO implements Serializable{

    @ApiModelProperty(value = "id",notes = "id")
    private Integer id;

    @ApiModelProperty(value = "月份",notes = "月份")
    private String month;

    @ApiModelProperty(value = "工作日",notes = "工作日")
    private String workdays;

    @ApiModelProperty(value = "更新时间",notes = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date lastUpdateTime;

    @ApiModelProperty(value = "创建用户id",notes = "创建用户id")
    private Integer userId;

    @ApiModelProperty(value = "创建用户姓名",notes = "创建用户姓名")
    private String userName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWorkdays() {
        return workdays;
    }

    public void setWorkdays(String workdays) {
        this.workdays = workdays;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
