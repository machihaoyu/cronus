package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * 获取附件分类
 * Created by xdj on 2017/4/23.
 */
public class DocumentCategoryDTO implements Serializable {

    private static final long serialVersionUID = -3734955946940261563L;

    private Integer document_c_id;// int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
    private Integer document_c_parent_id;// int(10) NOT NULL DEFAULT '0' COMMENT '上层ID',
    private Integer document_c_level;// tinyint(1) NOT NULL DEFAULT '0' COMMENT '层级',
    private Integer document_c_level_id;// tinyint(1) NOT NULL DEFAULT '0' COMMENT '层级ID',
    private String document_c_name_header;// varchar(100) DEFAULT NULL COMMENT '分类名称头',
    private String document_c_name;// varchar(100) DEFAULT NULL COMMENT '分类名称',
    private Integer document_c_status;// tinyint(1) NOT NULL DEFAULT '1' COMMENT '可用状态 1 可用 0 不可用',
    private Integer document_c_create_time;// int(10) NOT NULL DEFAULT '0' COMMENT '创建时间',
    private Integer document_c_update_time;// int(10) NOT NULL DEFAULT '0' COMMENT '更新时间',
    private String name;  //分类名称

    public Integer getDocument_c_id() {
        return document_c_id;
    }

    public void setDocument_c_id(Integer document_c_id) {
        this.document_c_id = document_c_id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
