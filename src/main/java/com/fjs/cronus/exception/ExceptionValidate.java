package com.fjs.cronus.exception;

import com.fjs.cronus.dto.crm.CRMData;
import com.fjs.cronus.dto.crm.FileData;
import com.fjs.cronus.enums.ErrorNumEnum;
import com.fjs.cronus.util.StringAsciiUtil;

/**
 * 接口异常校验
 * Created by crm on 2017/4/27.
 */
public class ExceptionValidate {

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
