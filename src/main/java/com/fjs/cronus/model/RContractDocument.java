package com.fjs.cronus.model;



public class RContractDocument extends  BaseModel{
    private Integer id;

    private Integer contractId;

    private Integer customerId;

    private Integer documentId;

    private Integer creatorId;

    private Integer documentCId;

    private String documentName;

    private String rcDocumentSource;

    private Document document;

    private DocumentCategory documentCategory;
    private Integer serviceContractId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getDocumentCId() {
        return documentCId;
    }

    public void setDocumentCId(Integer documentCId) {
        this.documentCId = documentCId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getRcDocumentSource() {
        return rcDocumentSource;
    }

    public void setRcDocumentSource(String rcDocumentSource) {
        this.rcDocumentSource = rcDocumentSource;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public DocumentCategory getDocumentCategory() {
        return documentCategory;
    }

    public void setDocumentCategory(DocumentCategory documentCategory) {
        this.documentCategory = documentCategory;
    }

    public Integer getServiceContractId() {
        return serviceContractId;
    }

    public void setServiceContractId(Integer serviceContractId) {
        this.serviceContractId = serviceContractId;
    }
}