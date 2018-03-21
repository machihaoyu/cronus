package com.fjs.cronus.dto.cronus;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by msi on 2017/12/7.
 */
public class PrdComunicationDTO implements Serializable {


    @ApiModelProperty(value = "创建人id")
    private Integer create_user_id;

    @ApiModelProperty(value = "沟通内容")
    private String content;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    private Integer create_time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(Integer create_user_id) {
        this.create_user_id = create_user_id;
    }

    public Integer getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Integer create_time) {
        this.create_time = create_time;
    }
}
