package com.fjs.cronus.exception;

import com.fjs.cronus.dto.crm.CRMData;
import com.fjs.cronus.dto.crm.FileData;
import com.fjs.cronus.dto.crm.ResponseData;
import com.fjs.cronus.enums.ErrorNumEnum;
import com.fjs.cronus.util.StringAsciiUtil;
import org.apache.commons.lang.StringUtils;

/**
 * 接口异常校验
 * Created by crm on 2017/4/27.
 */
public class ExceptionValidate {

    /**
     * 异常处理sale
     * @param data
     */
    public static void validateResponse(ResponseData data) {
        if (null != data){
            if (!ErrorNumEnum.SUCCESS.getCode().equals(data.getErrNum())){
                if (StringUtils.isNotEmpty(data.getRetData())) {
                    throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, StringAsciiUtil.asciiToString(data.getRetData()));
                } else {
                    throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, CronusException.Type.SYSTEM_CRM_ERROR.getError()+"返回异常");
                }
            }
        }
    }

    public static void validateCRMData(CRMData data) {
        if (null != data){
            if (!ErrorNumEnum.CRM_DATA_SUCCESS.getCode().equals(data.getStatus())){
                throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, StringAsciiUtil.asciiToString(data.getMessage()));
            }
        }
    }

    public static void validateFileData(FileData data) {
        if (null != data){
            if (!ErrorNumEnum.CRM_FILE_DATA_SUCCESS.getCode().equals(data.getErrNum())){
                throw new CronusException(CronusException.Type.SYSTEM_CRM_ERROR, StringAsciiUtil.asciiToString(data.getErrMsg()));
            }
        }
    }

}
