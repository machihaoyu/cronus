package com.fjs.cronus.dto;

import java.io.Serializable;

/**
 * Created by chenjie on 2017/5/3.
 */
public class TemplateOriginalDTO implements Serializable{

    private static final long serialVersionUID = 7895215389830588223L;

    private Integer id;
    private String name;
    private String config;
    private Long create_time;
    private Long update_time;
    private String self_pos_x;
    private String self_pos_y;
    private String tem_pic_w;
    private String tem_pic_h;
    private String tem_pic_src;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public Long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Long update_time) {
        this.update_time = update_time;
    }

    public String getSelf_pos_x() {
        return self_pos_x;
    }

    public void setSelf_pos_x(String self_pos_x) {
        this.self_pos_x = self_pos_x;
    }

    public String getSelf_pos_y() {
        return self_pos_y;
    }

    public void setSelf_pos_y(String self_pos_y) {
        this.self_pos_y = self_pos_y;
    }

    public String getTem_pic_w() {
        return tem_pic_w;
    }

    public void setTem_pic_w(String tem_pic_w) {
        this.tem_pic_w = tem_pic_w;
    }

    public String getTem_pic_h() {
        return tem_pic_h;
    }

    public void setTem_pic_h(String tem_pic_h) {
        this.tem_pic_h = tem_pic_h;
    }

    public String getTem_pic_src() {
        return tem_pic_src;
    }

    public void setTem_pic_src(String tem_pic_src) {
        this.tem_pic_src = tem_pic_src;
    }
}
