package com.fjs.cronus.dto.cronus;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by msi on 2017/10/12.
 */
public class QuestionsDTO implements Serializable {

    @ApiModelProperty(value = "name问题名称")
    private String name;
    @ApiModelProperty(value = "answer问题答案")
    private String answer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
