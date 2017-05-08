package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * Created by xdj on 2017/4/23.
 */
public class DocumentFileDTO implements Serializable {

    private static final long serialVersionUID = 4313955041671059945L;

    //r_contract_document 表
    private Integer rc_document_id;// int(10) unsigned NOT NULL AUTO_INCREMENT,
    private Integer contract_id;// int(10) unsigned NOT NULL DEFAULT '0' COMMENT '合同ID',
    private Integer customer_id;// int(10) NOT NULL DEFAULT '0' COMMENT '客户的id',
    private Integer document_id;// int(10) unsigned NOT NULL DEFAULT '0' COMMENT '文档ID',
    private Integer creator_id;// int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建者ID',
    private Integer document_c_id;// int(11) NOT NULL DEFAULT '0' COMMENT '文档类别',
    private String document_name;// varchar(100) DEFAULT NULL COMMENT '类型显示名字',
    private Integer rc_document_create_time;// int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
    private Integer rc_document_update_time;// int(10) NOT NULL DEFAULT '0' COMMENT '更新时间',
    private String rc_document_source;// enum('PC','MOBILE') DEFAULT NULL COMMENT '上传途径',
    private Integer rc_document_status;// int(10) unsigned NOT NULL DEFAULT '1' COMMENT '状态 1：正常；0：删除',

    //  document_category 表
//    private Integer document_c_id;// int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
    private Integer document_c_parent_id;// int(10) NOT NULL DEFAULT '0' COMMENT '上层ID',
    private Integer document_c_level;// tinyint(1) NOT NULL DEFAULT '0' COMMENT '层级',
    private Integer document_c_level_id;// tinyint(1) NOT NULL DEFAULT '0' COMMENT '层级ID',
    private String document_c_name_header;// varchar(100) DEFAULT NULL COMMENT '分类名称头',
    private String document_c_name;// varchar(100) DEFAULT NULL COMMENT '分类名称',
    private Integer document_c_status;// tinyint(1) NOT NULL DEFAULT '1' COMMENT '可用状态 1 可用 0 不可用',
    private Integer document_c_create_time;// int(10) NOT NULL DEFAULT '0' COMMENT '创建时间',
    private Integer document_c_update_time;// int(10) NOT NULL DEFAULT '0' COMMENT '更新时间',

    // document 表
//    private Integer document_id;// int(10) unsigned NOT NULL AUTO_INCREMENT,
//    private String document_name;// varchar(255) DEFAULT NULL COMMENT '原始文件名',
    private String document_type;// varchar(255) NOT NULL COMMENT '文件类型，如：text/plain',
    private Integer document_size;// int(10) unsigned NOT NULL DEFAULT '0' COMMENT '文件大小',
    private String document_ext;// varchar(100) CHARACTER SET latin1 DEFAULT NULL COMMENT '扩展名',
    private String document_md5;// varchar(100) DEFAULT NULL COMMENT '文件md5值',
    private String document_savename;// varchar(100) CHARACTER SET latin1 DEFAULT NULL COMMENT '存储名',
    private String document_savepath;// varchar(100) CHARACTER SET latin1 DEFAULT NULL COMMENT '存储路径',

    public Integer getRc_document_id() {
        return rc_document_id;
    }

    public void setRc_document_id(Integer rc_document_id) {
        this.rc_document_id = rc_document_id;
    }

    public Integer getContract_id() {
        return contract_id;
    }

    public void setContract_id(Integer contract_id) {
        this.contract_id = contract_id;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public Integer getDocument_id() {
        return document_id;
    }

    public void setDocument_id(Integer document_id) {
        this.document_id = document_id;
    }

    public Integer getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(Integer creator_id) {
        this.creator_id = creator_id;
    }

    public Integer getDocument_c_id() {
        return document_c_id;
    }

    public void setDocument_c_id(Integer document_c_id) {
        this.document_c_id = document_c_id;
    }

    public String getDocument_name() {
        return document_name;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }

    public Integer getRc_document_create_time() {
        return rc_document_create_time;
    }

    public void setRc_document_create_time(Integer rc_document_create_time) {
        this.rc_document_create_time = rc_document_create_time;
    }

    public Integer getRc_document_update_time() {
        return rc_document_update_time;
    }

    public void setRc_document_update_time(Integer rc_document_update_time) {
        this.rc_document_update_time = rc_document_update_time;
    }

    public String getRc_document_source() {
        return rc_document_source;
    }

    public void setRc_document_source(String rc_document_source) {
        this.rc_document_source = rc_document_source;
    }

    public Integer getRc_document_status() {
        return rc_document_status;
    }

    public void setRc_document_status(Integer rc_document_status) {
        this.rc_document_status = rc_document_status;
    }

    public Integer getDocument_c_parent_id() {
        return document_c_parent_id;
    }

    public void setDocument_c_parent_id(Integer document_c_parent_id) {
        this.document_c_parent_id = document_c_parent_id;
    }

    public Integer getDocument_c_level() {
        return document_c_level;
    }

    public void setDocument_c_level(Integer document_c_level) {
        this.document_c_level = document_c_level;
    }

    public Integer getDocument_c_level_id() {
        return document_c_level_id;
    }

    public void setDocument_c_level_id(Integer document_c_level_id) {
        this.document_c_level_id = document_c_level_id;
    }

    public String getDocument_c_name_header() {
        return document_c_name_header;
    }

    public void setDocument_c_name_header(String document_c_name_header) {
        this.document_c_name_header = document_c_name_header;
    }

    public String getDocument_c_name() {
        return document_c_name;
    }

    public void setDocument_c_name(String document_c_name) {
        this.document_c_name = document_c_name;
    }

    public Integer getDocument_c_status() {
        return document_c_status;
    }

    public void setDocument_c_status(Integer document_c_status) {
        this.document_c_status = document_c_status;
    }

    public Integer getDocument_c_create_time() {
        return document_c_create_time;
    }

    public void setDocument_c_create_time(Integer document_c_create_time) {
        this.document_c_create_time = document_c_create_time;
    }

    public Integer getDocument_c_update_time() {
        return document_c_update_time;
    }

    public void setDocument_c_update_time(Integer document_c_update_time) {
        this.document_c_update_time = document_c_update_time;
    }

    public String getDocument_type() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public Integer getDocument_size() {
        return document_size;
    }

    public void setDocument_size(Integer document_size) {
        this.document_size = document_size;
    }

    public String getDocument_ext() {
        return document_ext;
    }

    public void setDocument_ext(String document_ext) {
        this.document_ext = document_ext;
    }

    public String getDocument_md5() {
        return document_md5;
    }

    public void setDocument_md5(String document_md5) {
        this.document_md5 = document_md5;
    }

    public String getDocument_savename() {
        return document_savename;
    }

    public void setDocument_savename(String document_savename) {
        this.document_savename = document_savename;
    }

    public String getDocument_savepath() {
        return document_savepath;
    }

    public void setDocument_savepath(String document_savepath) {
        this.document_savepath = document_savepath;
    }
}
