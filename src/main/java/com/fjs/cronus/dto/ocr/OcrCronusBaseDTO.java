package com.fjs.cronus.dto.ocr;

import java.util.Date;

/**
 * Created by chenjie on 2017/8/17.
 */
public class OcrCronusBaseDTO {

    private Long customer_id;// int(11) NOT NULL COMMENT '客户ID',
    private String customer_name;// varchar(50) DEFAULT NULL COMMENT '客户姓名',
    private String customer_telephone;// varchar(11) NOT NULL COMMENT '客户手机号',
    private Long crm_attach_id; // INT NULL COMMENT 'CRM附件ID',

    private Long create_user_id;// int(11) DEFAULT '0' COMMENT '创建人ID',
    private String create_user_name;// varchar(50) DEFAULT 'OCR' COMMENT '创建人名称',
    private Date create_time = new Date();
    private Long update_user_id;// int(11) NOT NULL COMMENT '更新人ID',
    private String update_user_name;// varchar(50) DEFAULT NULL COMMENT '更新人名称',
    private Date update_time;

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_telephone() {
        return customer_telephone;
    }

    public void setCustomer_telephone(String customer_telephone) {
        this.customer_telephone = customer_telephone;
    }

    public Long getCrm_attach_id() {
        return crm_attach_id;
    }

    public void setCrm_attach_id(Long crm_attach_id) {
        this.crm_attach_id = crm_attach_id;
    }

    public Long getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(Long create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getCreate_user_name() {
        return create_user_name;
    }

    public void setCreate_user_name(String create_user_name) {
        this.create_user_name = create_user_name;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Long getUpdate_user_id() {
        return update_user_id;
    }

    public void setUpdate_user_id(Long update_user_id) {
        this.update_user_id = update_user_id;
    }

    public String getUpdate_user_name() {
        return update_user_name;
    }

    public void setUpdate_user_name(String update_user_name) {
        this.update_user_name = update_user_name;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
}
