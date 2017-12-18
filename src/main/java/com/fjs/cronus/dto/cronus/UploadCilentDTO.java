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


    @ApiModelProperty(value = "客户id" ,required = false)
    private String customerId;

    @ApiModelProperty(value = "附件类型" ,required = false)
    private String category;

    @ApiModelProperty(value = "C端传C",required = false)
    private String  source;

    @ApiModelProperty(value = "文件大小",required = false)
    private Integer  size;

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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
