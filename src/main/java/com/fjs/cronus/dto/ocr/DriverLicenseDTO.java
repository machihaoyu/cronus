package com.fjs.cronus.dto.ocr;

import com.fjs.cronus.dto.cronus.OcrDocumentDto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenjie on 2017/8/17.
 */
public class DriverLicenseDTO extends OcrCronusBaseDTO implements Serializable{

    private static final long serialVersionUID = -1521787265459689441L;

    private String driver_name;
    private String driver_num;
    private String driver_vehicle_type;
    private String driver_start_date;
    private String driver_end_date;

    private List<OcrDocumentDto> ocrDocumentDto;

    public List<OcrDocumentDto> getOcrDocumentDto() {
        return ocrDocumentDto;
    }

    public void setOcrDocumentDto(List<OcrDocumentDto> ocrDocumentDto) {
        this.ocrDocumentDto = ocrDocumentDto;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_num() {
        return driver_num;
    }

    public void setDriver_num(String driver_num) {
        this.driver_num = driver_num;
    }

    public String getDriver_vehicle_type() {
        return driver_vehicle_type;
    }

    public void setDriver_vehicle_type(String driver_vehicle_type) {
        this.driver_vehicle_type = driver_vehicle_type;
    }

    public String getDriver_start_date() {
        return driver_start_date;
    }

    public void setDriver_start_date(String driver_start_date) {
        this.driver_start_date = driver_start_date;
    }

    public String getDriver_end_date() {
        return driver_end_date;
    }

    public void setDriver_end_date(String driver_end_date) {
        this.driver_end_date = driver_end_date;
    }
}
