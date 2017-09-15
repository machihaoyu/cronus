package com.fjs.cronus.exception;


import com.fjs.framework.exception.BaseException;
import com.fjs.framework.exception.ResponseError;

/**
 *
 * Created by bianxj on 2017/4
 */
public class CronusException extends BaseException {

    public enum Type {

        /**
         * 短息发送失败
         */
        SYSTEM_CRM_ERROR("5001", "CRONUS_SYSTEM_CRM_ERROR"),
        SAAS_CRM_EXCEPTION("6001", "SAAS_CRM_EXCEPTION"),
        CRM_PARAMS_ERROR("6003","参数错误"),
        CRM_OTHER_ERROR("6002","其它错误"),
        CRM_CUSTOMER_ERROR("6004","新增客户出错"),
        CRM_CUSTOMERNAME_ERROR("6005","客户名称不能为空"),
        CRM_CUSTOMERPHONE_ERROR("6006","手机号格式有误"),
        CRM_CUSTOMERPHONERE_ERROR("6006","该手机号已存在"),
        CRM_CUSTOMEINFO_ERROR("6007","该用户不存在"),
        CRM_DATAAUTH_ERROR("6008","当前用户无数据权限"),

        ;

        private String status;
        private String error;

        Type(String status, String error) {
            this.status = status;
            this.error = error;
        }

        public String getStatus() {
            return status;
        }

        public String getError() {
            return error;
        }
    }



    public CronusException(Type type) {
        super(type.getError());
        setResponseError(new ResponseError(type.getStatus(), type.getError(), null,this.getClass().getTypeName()));
    }

    public CronusException(Type type, String message) {
        super(message);
        setResponseError(new ResponseError(type.getStatus(), type.getError(), message,this.getClass().getTypeName()));
    }

    public CronusException() {
    }

    public CronusException(String message) {
        super(message);
    }


    public CronusException(String message, Throwable cause) {
        super(message, cause);
    }

    public CronusException(Throwable cause) {
        super(cause);
    }

    public CronusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
