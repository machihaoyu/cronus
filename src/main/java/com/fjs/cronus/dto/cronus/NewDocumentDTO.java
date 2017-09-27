package com.fjs.cronus.dto.cronus;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by msi on 2017/9/21.
 */
public class NewDocumentDTO implements Serializable{

    private String document;//原始文件地址

    private String m_document;//中号缩略图地址

    private String s_document;//小号缩略图地址

    private Integer contract_document_id;//附件关系主键id

    private Integer category_id;//附件类型Id

    private String category_name;//附件类型名称

    private Integer contract_id;//合同id

    private String ext;//附件扩展名

    private String name;//原始文件名称

    private String m_name;

    private String up_name;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date up_date;

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getM_document() {
        return m_document;
    }

    public void setM_document(String m_document) {
        this.m_document = m_document;
    }

    public String getS_document() {
        return s_document;
    }

    public void setS_document(String s_document) {
        this.s_document = s_document;
    }

    public Integer getContract_document_id() {
        return contract_document_id;
    }

    public void setContract_document_id(Integer contract_document_id) {
        this.contract_document_id = contract_document_id;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Integer getContract_id() {
        return contract_id;
    }

    public void setContract_id(Integer contract_id) {
        this.contract_id = contract_id;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getUp_name() {
        return up_name;
    }

    public void setUp_name(String up_name) {
        this.up_name = up_name;
    }

    public Date getUp_date() {
        return up_date;
    }

    public void setUp_date(Date up_date) {
        this.up_date = up_date;
    }
}
