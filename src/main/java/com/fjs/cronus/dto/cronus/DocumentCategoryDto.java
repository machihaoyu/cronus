package com.fjs.cronus.dto.cronus;

import java.io.Serializable;

/**
 * Created by msi on 2017/9/20.
 */
public class DocumentCategoryDto implements Serializable{

    private Integer id;
    private Integer documentCParentId;
    private String documentCNameHeader;
    private String documentCName;
    private Integer customerId;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDocumentCParentId() {
        return documentCParentId;
    }

    public void setDocumentCParentId(Integer documentCParentId) {
        this.documentCParentId = documentCParentId;
    }

    public String getDocumentCNameHeader() {
        return documentCNameHeader;
    }

    public void setDocumentCNameHeader(String documentCNameHeader) {
        this.documentCNameHeader = documentCNameHeader;
    }

    public String getDocumentCName() {
        return documentCName;
    }

    public void setDocumentCName(String documentCName) {
        this.documentCName = documentCName;
    }
}
