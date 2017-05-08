package com.fjs.cronus.dto;


import java.io.Serializable;

/**
 * 面见表(customer_meet)dto
 * Created by crm on 2017/4/14.
 */
public class CustomerMeetDTO implements Serializable {

    private static final long serialVersionUID = 6774377656551439899L;

    private Integer meet_id;// int(10) NOT NULL AUTO_INCREMENT,
    private Integer customer_id;// int(10) NOT NULL COMMENT '客户id',
    private Integer user_id;// int(10) NOT NULL COMMENT '业务员id',
    private String user_name;// varchar(200) NOT NULL COMMENT '业务员姓名',
    private Long meet_time;// int(10) NOT NULL COMMENT '面见时间',
    private Long create_time;// int(10) NOT NULL COMMENT '创建时间',

    public Integer getMeet_id() {
        return meet_id;
    }

    public void setMeet_id(Integer meet_id) {
        this.meet_id = meet_id;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Long getMeet_time() {
        return meet_time;
    }

    public void setMeet_time(Long meet_time) {
        this.meet_time = meet_time;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }
}
