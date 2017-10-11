package com.fjs.cronus.dto.cronus;

import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;

/**
 * Created by msi on 2017/10/11.
 */
public class CallbackConfigDTO implements Serializable{


    private Integer confId;

    private String cycle;

    private JSONArray question;

    public Integer getConfId() {
        return confId;
    }

    public void setConfId(Integer confId) {
        this.confId = confId;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public JSONArray getQuestion() {
        return question;
    }

    public void setQuestion(JSONArray question) {
        this.question = question;
    }
}
