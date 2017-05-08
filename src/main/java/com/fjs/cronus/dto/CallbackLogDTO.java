package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * 回访日志表(callback_log)dto
 * Created by crm on 2017/4/14.
 */
public class CallbackLogDTO implements Serializable {

    private static final long serialVersionUID = -4913797254714206217L;

    private Integer id;// int(10) NOT NULL AUTO_INCREMENT,
    private Integer customer_id;// int(10) NOT NULL DEFAULT '0' COMMENT '客户id',
    private Integer create_user_id;// int(10) NOT NULL DEFAULT '0' COMMENT '创建人id',
    private String create_user_name;// varchar(100) NOT NULL COMMENT '创建人姓名',
    private String question;// varchar(255) NOT NULL COMMENT '问的问题',
    private String answer;// varchar(255) NOT NULL COMMENT '问的答案',
    private Long create_time;// int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public Integer getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(Integer create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getCreate_user_name() {
        return create_user_name;
    }

    public void setCreate_user_name(String create_user_name) {
        this.create_user_name = create_user_name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }
}
