package com.fjs.cronus.dto.cronus;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by yinzf on 2017/11/24.
 */
public class CommentDTO {
    private Integer id;
    @ApiModelProperty(value = "沟通记录id")
    private Integer communicationLogId;
    @ApiModelProperty(value = "评论内容")
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCommunicationLogId() {
        return communicationLogId;
    }

    public void setCommunicationLogId(Integer communicationLogId) {
        this.communicationLogId = communicationLogId;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", communicationLogId=" + communicationLogId +
                ", content='" + content + '\'' +
                '}';
    }
}
