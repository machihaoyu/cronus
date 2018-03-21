package com.fjs.cronus.dto.cronus;

import java.io.Serializable;

/**
 * Created by msi on 2017/12/8.
 */
public class PrdComuniDTO implements Serializable {


   private Integer create_user_id;

   private String content;

   private String create_time;

   private String create_user_name;

    public String getCreate_user_name() {
        return create_user_name;
    }

    public void setCreate_user_name(String create_user_name) {
        this.create_user_name = create_user_name;
    }

    public Integer getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(Integer create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
