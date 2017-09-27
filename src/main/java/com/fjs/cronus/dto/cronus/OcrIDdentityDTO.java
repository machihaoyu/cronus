package com.fjs.cronus.dto.cronus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fjs.cronus.dto.ocr.OcrCronusBaseDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by msi on 2017/9/25.
 */
public class OcrIDdentityDTO implements Serializable {

    private Integer id;
    private String card_name;// varchar(50) DEFAULT NULL COMMENT '身份证-姓名',
    private String card_sex;// varchar(10) DEFAULT NULL COMMENT '身份证-性别',
    private String card_nation;// varchar(50) DEFAULT NULL COMMENT '身份证-民族',
    private String card_birth;// varchar(30) DEFAULT NULL COMMENT '身份证-出生日期',
    private String card_address;// varchar(100) DEFAULT NULL COMMENT '身份证-住址',
    private String card_num;// varchar(20) DEFAULT NULL COMMENT '身份证-身份证号',
    private String card_sign_org;// varchar(50) DEFAULT NULL COMMENT '身份证-签发机关',
    private String card_valid_start;// varchar(50) DEFAULT NULL COMMENT '身份证-有效期-开始',
    private String card_valid_end;// varchar(50) DEFAULT NULL COMMENT '身份证-有效期-截止',
    private String side; //身份证正反面 #### 正面 : face ; 反面 : back
    private Integer category;
    private Integer customer_id;// int(11) NOT NULL COMMENT '客户ID',
    private String customer_name;// varchar(50) DEFAULT NULL COMMENT '客户姓名',
    private String customer_telephone;// varchar(11) NOT NULL COMMENT '客户手机号',
    private Integer crm_attachback_id; // INT NULL COMMENT 'CRM附件ID',

    private Integer create_user_id;// int(11) DEFAULT '0' COMMENT '创建人ID',
    private String create_user_name;// varchar(50) DEFAULT 'OCR' COMMENT '创建人名称',
    private Integer update_user_id;// int(11) NOT NULL COMMENT '更新人ID',
    private String update_user_name;// varchar(50) DEFAULT NULL COMMENT '更新人名称',

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date   create_time;

    private List<OcrDocumentDto> ocrDocumentDto;

    public List<OcrDocumentDto> getOcrDocumentDto() {
        return ocrDocumentDto;
    }

    public void setOcrDocumentDto(List<OcrDocumentDto> ocrDocumentDto) {
        this.ocrDocumentDto = ocrDocumentDto;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getCard_sex() {
        return card_sex;
    }

    public void setCard_sex(String card_sex) {
        this.card_sex = card_sex;
    }

    public String getCard_nation() {
        return card_nation;
    }

    public void setCard_nation(String card_nation) {
        this.card_nation = card_nation;
    }

    public String getCard_birth() {
        return card_birth;
    }

    public void setCard_birth(String card_birth) {
        this.card_birth = card_birth;
    }

    public String getCard_address() {
        return card_address;
    }

    public void setCard_address(String card_address) {
        this.card_address = card_address;
    }

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public String getCard_sign_org() {
        return card_sign_org;
    }

    public void setCard_sign_org(String card_sign_org) {
        this.card_sign_org = card_sign_org;
    }

    public String getCard_valid_start() {
        return card_valid_start;
    }

    public void setCard_valid_start(String card_valid_start) {
        this.card_valid_start = card_valid_start;
    }

    public String getCard_valid_end() {
        return card_valid_end;
    }

    public void setCard_valid_end(String card_valid_end) {
        this.card_valid_end = card_valid_end;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
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

    public Integer getCrm_attachback_id() {
        return crm_attachback_id;
    }

    public void setCrm_attachback_id(Integer crm_attachback_id) {
        this.crm_attachback_id = crm_attachback_id;
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

    public Integer getUpdate_user_id() {
        return update_user_id;
    }

    public void setUpdate_user_id(Integer update_user_id) {
        this.update_user_id = update_user_id;
    }

    public String getUpdate_user_name() {
        return update_user_name;
    }

    public void setUpdate_user_name(String update_user_name) {
        this.update_user_name = update_user_name;
    }
}
