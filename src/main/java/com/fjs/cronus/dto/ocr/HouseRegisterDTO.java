package com.fjs.cronus.dto.ocr;

import com.fjs.cronus.dto.cronus.OcrDocumentDto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by msi on 2017/9/23.
 */
public class HouseRegisterDTO extends OcrCronusBaseDTO implements Serializable {
    private static final long serialVersionUID = -744359352406200658L;

    private String house_ownner;
    private String house_address;
    private String house_purpose;
    private String house_usage_term;
    private String house_area;
    private String house_type;
    private String house_completion_date;
    private List<OcrDocumentDto> ocrDocumentDto;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OcrDocumentDto> getOcrDocumentDto() {
        return ocrDocumentDto;
    }

    public void setOcrDocumentDto(List<OcrDocumentDto> ocrDocumentDto) {
        this.ocrDocumentDto = ocrDocumentDto;
    }

    public String getHouse_ownner() {
        return house_ownner;
    }

    public void setHouse_ownner(String house_ownner) {
        this.house_ownner = house_ownner;
    }

    public String getHouse_address() {
        return house_address;
    }

    public void setHouse_address(String house_address) {
        this.house_address = house_address;
    }

    public String getHouse_purpose() {
        return house_purpose;
    }

    public void setHouse_purpose(String house_purpose) {
        this.house_purpose = house_purpose;
    }

    public String getHouse_usage_term() {
        return house_usage_term;
    }

    public void setHouse_usage_term(String house_usage_term) {
        this.house_usage_term = house_usage_term;
    }

    public String getHouse_area() {
        return house_area;
    }

    public void setHouse_area(String house_area) {
        this.house_area = house_area;
    }

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    public String getHouse_completion_date() {
        return house_completion_date;
    }

    public void setHouse_completion_date(String house_completion_date) {
        this.house_completion_date = house_completion_date;
    }
}
