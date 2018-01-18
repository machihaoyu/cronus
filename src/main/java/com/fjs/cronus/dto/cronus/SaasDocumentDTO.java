package com.fjs.cronus.dto.cronus;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by msi on 2018/1/18.
 */
public class SaasDocumentDTO implements Serializable {

    @ApiModelProperty(value = "图片路径")
    private String url;

    @ApiModelProperty(value = "删除图片所需要的参数")
    private Integer rc_document_Id;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getRc_document_Id() {
        return rc_document_Id;
    }

    public void setRc_document_Id(Integer rc_document_Id) {
        this.rc_document_Id = rc_document_Id;
    }
}
