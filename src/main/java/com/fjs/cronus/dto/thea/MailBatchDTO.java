package com.fjs.cronus.dto.thea;

import io.swagger.annotations.ApiModelProperty;

/**
 * 消息
 * Created by on 2017/10/14.
 */
public class MailBatchDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息发送人", required = false)
    private String fromId;

    @ApiModelProperty(value = "消息发送人名称", required = false)
    private String fromName;

    @ApiModelProperty(value = "消息接受人(多个)", required = false)
    private String toId;

    @ApiModelProperty(value = "消息内容", required = false)
    private String content;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
