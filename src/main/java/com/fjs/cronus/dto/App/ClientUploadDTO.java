package com.fjs.cronus.dto.App;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by msi on 2018/2/8.
 */
public class ClientUploadDTO implements Serializable {

    @ApiModelProperty(value = "图片路径")
    private String url;

    @ApiModelProperty(value = "附件id")
    private Integer document_Id;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getDocument_Id() {
        return document_Id;
    }

    public void setDocument_Id(Integer document_Id) {
        this.document_Id = document_Id;
    }
}
