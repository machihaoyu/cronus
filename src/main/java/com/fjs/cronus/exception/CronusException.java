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
        CEM_CUSTOMERINTERVIEW("6009","非法参数"),
        CRM_NOTNULL_UPLOAD("6921","请添加附件"),
        CRM_MAXSIZE_UPLOAD("6010","附件过大，附件大小不能超过20M"),
        CRM_FILETYPR_UPLOAD("6011","不支持此文件类型上传"),
        CRM_CONTROCTDOCU_ERROR("6012","新增附件客户关系失败"),
        CRM_OCRIDENTITY_ERROR("6013","插入身份证信息操作失败"),
        CRM_OCRDOCUMENTCAGORY_ERROR("6014","获取附件类型出错"),
        CRM_OCRDRIVERLICENCE_ERROR("6015","插驾驶证操作失败"),
        CRM_OCRDRIVERLICENCEVEH_ERROR("6016","插行驶证操作失败"),
        CRM_OCRHOUSEREGISTION_ERROR("6017","增加房产证信息失败"),
        CRM_OCRINFO_ERROR("6018","图文识别非法参数"),
        CEM_CUSTOMERBASEINFO_ERROR("6019","不存在该面谈信息"),
        CEM_CUSTOMERIDENTITYINFO_ERROR("6920","查询出错，未查到相关信息"),
        CRM_UPLOADERROR_ERROR("6922","文件上传失败"),
        CRM_DOWNLOADERROR_ERROR("6923","文件下载失败"),
        CRM_CUSTOMEHOUSE_ERROR("6924","客户房产不能为空"),
        CRM_CALLBACKCUSTOMER_ERROR("6925","非法操作"),
        CRM_CALLBACK_CONFIG_ERROR("6926","获取不到配置信息"),
        CRM_CUSTOMERLOAN_ERROR("6027","未找到当前客户交易信息"),
        CRM_CUSTOMERBAXKLOG_ERROR("6028","未找到该日志信息"),
        CRM_CUSTOMERUNFIND_ERROR("6029","未找到该客户信息"),
        CRM_CUSTOMCALLSTATUS_ERROR("6030","请选择回访状态"),
        CRM_CUSTOMQUESTION_ERROR("6031","回防状态正常请提交问题"),
        CRM_CUSTOANSWER_ERROR("6032","请回答所有的问题"),
        CRM_CUSTOMERLOG_ERROR("6033","插入日志失败"),
        CRM_PHONEAREA_ERROR("6035","查询手机归属地失败"),
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
