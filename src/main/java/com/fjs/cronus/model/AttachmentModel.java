package com.fjs.cronus.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by chenjie on 2017/12/19.
 */
public class AttachmentModel {

    @ApiModelProperty(name = "id", value = "附件类型id")
    private Integer id;

    @ApiModelProperty(name = "documentName", value = "附件名称")
    private String documentName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
}
