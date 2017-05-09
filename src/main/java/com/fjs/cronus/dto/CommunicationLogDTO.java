package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * 沟通日志表(communication_log)DTO
 * Created by crm on 2017/4/13.
 */
public class CommunicationLogDTO implements Serializable {


    private static final long serialVersionUID = -320557644902364868L;

    private int id;// int(10) NOT NULL AUTO_INCREMENT,
    private int customer_id;// int(10) NOT NULL DEFAULT '0' COMMENT '客户ID',
    private int create_user_id;// int(10) NOT NULL DEFAULT '0' COMMENT '创建人ID',
    private int type;// int(10) NOT NULL DEFAULT '0' COMMENT '类型',
    private String content;// text COMMENT '内容',
    private String next_contact_time;// int(10) NOT NULL DEFAULT '0' COMMENT '再次沟通内容',
    private String next_contact_content;// text,
    private long create_time;// int(10) NOT NULL DEFAULT '0' COMMENT '创建时间',
    private String house_status;// enum('','无','有') NOT NULL DEFAULT '' COMMENT '有无房产',
    private int loan_amount;// int(11) NOT NULL DEFAULT '0' COMMENT '意向贷款金额',
    private String create_user_name; //
    private int meet;
    private String meet_time;
    private int user_id;
    private String user_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(int create_user_id) {
        this.create_user_id = create_user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNext_contact_time() {
        return next_contact_time;
    }

    public void setNext_contact_time(String next_contact_time) {
        this.next_contact_time = next_contact_time;
    }

    public String getNext_contact_content() {
        return next_contact_content;
    }

    public void setNext_contact_content(String next_contact_content) {
        this.next_contact_content = next_contact_content;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getHouse_status() {
        return house_status;
    }

    public void setHouse_status(String house_status) {
        this.house_status = house_status;
    }

    public int getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(int loan_amount) {
        this.loan_amount = loan_amount;
    }

    public String getCreate_user_name() {
        return create_user_name;
    }

    public void setCreate_user_name(String create_user_name) {
        this.create_user_name = create_user_name;
    }

    public int getMeet() {
        return meet;
    }

    public void setMeet(int meet) {
        this.meet = meet;
    }

    public String getMeet_time() {
        return meet_time;
    }

    public void setMeet_time(String meet_time) {
        this.meet_time = meet_time;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
