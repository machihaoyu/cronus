package com.fjs.cronus.dto.App;

import java.io.Serializable;
import java.util.List;

/**
 * Created by msi on 2018/1/4.
 */
public class ThreeCateDTO implements Serializable {

    private Integer id;

    private Integer documentCParentId;

    private String documentCName;

    private List<ThreeDTO> sonDto;

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

    public String getDocumentCName() {
        return documentCName;
    }

    public void setDocumentCName(String documentCName) {
        this.documentCName = documentCName;
    }

    public List<ThreeDTO> getSonDto() {
        return sonDto;
    }

    public void setSonDto(List<ThreeDTO> sonDto) {
        this.sonDto = sonDto;
    }
}
