package com.fjs.cronus.dto.cronus;

import com.fjs.cronus.model.DocumentCategory;
import com.fjs.cronus.model.RContractDocument;

import java.util.Date;

/**
 * Created by msi on 2017/9/22.
 */
public class OcrSaveBaseInfoDTO {

    private  String side;//正反面
    private  Integer type; //证件类型
    private RContractDocument r_contract_document;
    private DocumentCategory categoryInfo;
    private Integer customer_id;//客户id
    private String customer_name;//客户姓名
    private String customer_telephone;//解密后的手机号
    private String imgBase64;
    private Integer user_id;
    private String user_name;
    private Integer create_user_id;
    private String create_user_name;
    private Integer crm_attach_id;//附件id
    private String status;
    private Date   create_time;
    private Integer update_user_id;
    private Date update_user_name;
    private Long id;



    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public RContractDocument getR_contract_document() {
        return r_contract_document;
    }

    public void setR_contract_document(RContractDocument r_contract_document) {
        this.r_contract_document = r_contract_document;
    }

    public DocumentCategory getCategoryInfo() {
        return categoryInfo;
    }

    public void setCategoryInfo(DocumentCategory categoryInfo) {
        this.categoryInfo = categoryInfo;
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

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
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

    public Integer getCrm_attach_id() {
        return crm_attach_id;
    }

    public void setCrm_attach_id(Integer crm_attach_id) {
        this.crm_attach_id = crm_attach_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Integer getUpdate_user_id() {
        return update_user_id;
    }

    public void setUpdate_user_id(Integer update_user_id) {
        this.update_user_id = update_user_id;
    }

    public Date getUpdate_user_name() {
        return update_user_name;
    }

    public void setUpdate_user_name(Date update_user_name) {
        this.update_user_name = update_user_name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
