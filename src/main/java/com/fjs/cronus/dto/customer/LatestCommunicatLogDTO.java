package com.fjs.cronus.dto.customer;

/**
 * 最新一条沟通日志DTO
 * Created by crm on 2017/6/12.
 */
public class LatestCommunicatLogDTO {

    private String create_user_name;
    private String create_date;
    private Integer customer_id;
    private String content;

    public String getCreate_user_name() {
        return create_user_name;
    }

    public void setCreate_user_name(String create_user_name) {
        this.create_user_name = create_user_name;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
