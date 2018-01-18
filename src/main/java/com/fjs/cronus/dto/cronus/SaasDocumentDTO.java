package com.fjs.cronus.dto.cronus;

import java.io.Serializable;

/**
 * Created by msi on 2018/1/18.
 */
public class SaasDocumentDTO implements Serializable {
    private String url;

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
