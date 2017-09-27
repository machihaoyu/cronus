package com.fjs.cronus.dto.ocr;

import com.fjs.cronus.dto.cronus.OcrDocumentDto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenjie on 2017/8/17.
 */
public class DriverVehicleDTO extends OcrCronusBaseDTO implements Serializable{

    private static final long serialVersionUID = -4943409623264012950L;

    private String driver_owner;// 所有人名称
    private String driver_plate_num;// 车牌号码
    private String driver_vehicle_type;// 车辆类型
    private String driver_vin;// 车辆识别代号
    private String driver_engine_num;// 发动机号码
    private String driver_register_date;// 注册日期
    private List<OcrDocumentDto> ocrDocumentDto;

    public List<OcrDocumentDto> getOcrDocumentDto() {
        return ocrDocumentDto;
    }

    public void setOcrDocumentDto(List<OcrDocumentDto> ocrDocumentDto) {
        this.ocrDocumentDto = ocrDocumentDto;
    }

    public String getDriver_owner() {
        return driver_owner;
    }

    public void setDriver_owner(String driver_owner) {
        this.driver_owner = driver_owner;
    }

    public String getDriver_plate_num() {
        return driver_plate_num;
    }

    public void setDriver_plate_num(String driver_plate_num) {
        this.driver_plate_num = driver_plate_num;
    }

    public String getDriver_vehicle_type() {
        return driver_vehicle_type;
    }

    public void setDriver_vehicle_type(String driver_vehicle_type) {
        this.driver_vehicle_type = driver_vehicle_type;
    }

    public String getDriver_vin() {
        return driver_vin;
    }

    public void setDriver_vin(String driver_vin) {
        this.driver_vin = driver_vin;
    }

    public String getDriver_engine_num() {
        return driver_engine_num;
    }

    public void setDriver_engine_num(String driver_engine_num) {
        this.driver_engine_num = driver_engine_num;
    }

    public String getDriver_register_date() {
        return driver_register_date;
    }

    public void setDriver_register_date(String driver_register_date) {
        this.driver_register_date = driver_register_date;
    }
}
