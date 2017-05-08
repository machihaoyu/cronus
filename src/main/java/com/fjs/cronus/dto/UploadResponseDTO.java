package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * 上传附件成功返回的DTO对象
 * Created by xdj on 2017/4/26.
 */
public class UploadResponseDTO implements Serializable {

    private static final long serialVersionUID = 4772201298371061226L;

    private String document; //UPLOAD_PATH2017-04-24//77871493027036.png,
    private String m_document; //UPLOAD_PATH2017-04-24//m_77871493027036.png,
    private String s_document; //UPLOAD_PATH2017-04-24//s_77871493027036.png,
    private Integer contract_document_id; //580,
    private Integer category_id; //:0,
    private String category_name; // ,
    private Integer contract_id;//:,
    private String ext; //png,
    private String name; //92761493185636.png,
    private String m_name; //- -580.png,
    private String up_name; //null,
    private String up_time; //2017-04-26

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

    public String getUp_time() {
        return up_time;
    }

    public void setUp_time(String up_time) {
        this.up_time = up_time;
    }
}
