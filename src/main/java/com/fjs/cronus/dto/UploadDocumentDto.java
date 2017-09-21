package com.fjs.cronus.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by msi on 2017/9/20.
 */
public class UploadDocumentDto implements Serializable {

    @ApiModelProperty(value = "文件base64编码" ,required = true)
    private String imageBase64;

    @ApiModelProperty(value = "原始文件名称" ,required = true)
    private String fileName;

    @ApiModelProperty(value = "文件后缀名" ,required = true)
    private String type;

    @ApiModelProperty(value = "文件size" ,required = true)
    private Integer size;

    @ApiModelProperty(value = "合同编号" ,required = false)
    private Integer contractId;

    @ApiModelProperty(value = "客户id" ,required = false)
    private Integer customerId;

    @ApiModelProperty(value = "附件类型" ,required = false)
    private Integer category;

    @ApiModelProperty(value = "pc or mobile",required = true)
    private String  source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
}
