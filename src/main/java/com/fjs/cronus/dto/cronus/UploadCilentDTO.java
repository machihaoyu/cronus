package com.fjs.cronus.dto.cronus;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by msi on 2017/12/18.
 */
public class UploadCilentDTO implements Serializable {

    @ApiModelProperty(value = "文件base64编码" ,required = false)
    private String imageBase64;

    @ApiModelProperty(value = "原始文件名称:a.jpg" ,required = false)
    private String fileName;

    @ApiModelProperty(value = "手机号唯一标识" ,required = false)
    private String telephone;

    @ApiModelProperty(value = "附件类型id" ,required = false)
    private String categoryId;

    @ApiModelProperty(value = "C端传C",required = false)
    private String  source;

    @ApiModelProperty(value = "文件大小",required = false)
    private Integer  size;

    @ApiModelProperty(value = "文件id",required = false)
    private Integer  documentId;

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
