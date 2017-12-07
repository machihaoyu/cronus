package com.fjs.cronus.exception;


import com.fjs.framework.exception.BaseException;
import com.fjs.framework.exception.ResponseError;

/**
 *
 * Created by bianxj on 2017/4
 */
public class CronusException extends BaseException {

    public enum Type {
        THEA_SYSTEM_ERROR("9999","系统异常"),

        /**
         * 短息发送失败
         */
        SYSTEM_CRM_ERROR("5001", "CRONUS_SYSTEM_CRM_ERROR"),

        SAAS_CRM_EXCEPTION("6001", "SAAS_CRM_EXCEPTION"),

        CRM_PARAMS_ERROR("6003","参数错误"),

        CRM_OTHER_ERROR("6002","其它错误"),

        CRM_CUSTOMER_ERROR("6004","新增客户出错"),

        CRM_CUSTOMERNAME_ERROR("6005","客户姓名不能为空"),

        CRM_CUSTOMERPHONE_ERROR("6006","手机号格式有误"),

        CRM_CUSTOMERPHONERE_ERROR("6006","该手机号已存在"),

        CRM_CUSTOMEINFO_ERROR("6007","客户不存在"),

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

        CRM_CALLBAXKCONFIG_ERROR("6036","未查询到相关配置"),

        CRM_CUSTOMERSEX_ERROR("6037","客户性别不能为空"),

        CRM_CUSTOMERAGE_ERROR("6038","客户年龄不能为空"),

        CRM_CUSTOMERMERAEATATUS_ERROR("6039","客户婚姻状态不能为空"),

        CRM_CUSTOMEREDUCATION_ERROR("6040","客户学历状态不能为空"),

        CRM_CUSTOMERLOANMOUNT_ERROR("6041","客户借款金额不能为空"),

        CRM_CUSTOMERLOANTIME_ERROR("6043","客户借款期限不能为空"),

        CRM_CUSTOMERLOANUSETIME_ERROR("6044","客户用款时间不能为空"),

        CRM_CUSTOMERLOANPURPOSE_ERROR("6045","客户借款用途不能为空"),

        CRM_CUSTOMERCREATERECORD_ERROR("6046","客户征信记录不能为空"),

        CRM_CUSTOMERCDEBETAMOUNT_ERROR("6047","客户负债金额不能为空"),

        CRM_CUSTOMERISOVER_ERROR("6048","客户逾期状态不能为空"),

        CRM_CUSTOMERINENTITYNAME_ERROR("6049","身份证姓名不能为空"),

        CRM_CUSTOMERINENTITYSEX_ERROR("6050","身份证性别不能为空"),

        CRM_CUSTOMERINENTITYNATION_ERROR("6051","身份证民族不能为空"),

        CRM_CUSTOMERINENTITYBIRTH_ERROR("6052","身份证生日不能为空"),

        CRM_CUSTOMERINENTITYADDRESS_ERROR("6053","身份证地址不能为空"),

        CRM_CUSTOMERINENTITYNUMBER_ERROR("6054","身份证号码不能为空"),

        CRM_CUSTOMERINENTITYSIGNORG_ERROR("6055","身份证签发机关不能为空"),

        CRM_HOUSEHOLDREGNAME_ERROR("6056","户口簿姓名不能为空"),

        CRM_HOUSEHOLDRESEX_ERROR("6057","户口簿性别不能为空"),

        CRM_HOUSEHOLDREPLACE_ERROR("6058","户口簿籍贯不能为空"),

        CRM_HOUSEHOLDREBIRTH_ERROR("6059","户口簿出生日期不能为空"),

        CRM_HOUSEHOLDREPEOPLE_ERROR("6060","户口簿民族不能为空"),

        CRM_DRIVERLIENCENAME_ERROR("6061","驾驶人姓名不能为空"),

        CRM_DRIVERLIENCENUM_ERROR("6062","驾驶证号不能为空"),

        CRM_DRIVERLIEVELICHRTYPE_ERROR("6063","车辆类型不能为空"),

        CRM_DRIVERLIEVESTARTDATE_ERROR("6064","驾驶证起始期限不能为空"),

        CRM_DRIVERLIEVEENDATE_ERROR("6065","驾驶证有效期限不能为空"),

        CRM_DRIVERVEGHICLEOWNER_ERROR("6066","行驶证证所有人不能为空"),

        CRM_DRIVERVEGHICLEPLATNUM_ERROR("6067","行驶证车牌号不能为空"),

        CRM_DRIVERVEGHICLEWIN_ERROR("6068","行驶证车辆识别代码不能为空"),

        CRM_DRIVERVEGHICLEENGINENUM_ERROR("6069","发动机型号不能为空"),

        CRM_DRIVERVEGHICLEREGIST_ERROR("6070","注册日期不能为空"),

        CRM_VALIDAOCUMENRTOCON_ERROR("6071","productType不能为空"),

        CRM_VALIDAOCUMENRCOUNT_ERROR("6072","获取附件分类信息出错,请联系管理员"),

        CRM_CUSOMERALLACATE_ERROR("6073","存在新分配未沟通客户,不允许此操作"),

        MESSAGE_NOT_EXIST_LOAN("6074", "参数存在错误,不允许此操作"),

        MESSAGE_REMOVECUSTOERAll_ERROR("6075", "请选择要转的负责人"),

        MESSAGE_REMOVECUSTOERSTATUS_ERROR("6076", "请选择在职的负责人"),

        MESSAGE_REMOVECUSTNOTNULL_ERROR("6077","请先选择客户"),

        MESSAGE_REMOVENOTINJOB_ERROR("6078","离职客户批量转移失败"),

        MESSAGE_CUSTOMERCLEAN_ERROR("6079","清洗中，不能领取客户"),

        MESSAGE_PULLCUSTOMERCOUNT_ERROR("6080","您今天客户领取已到上限"),

        MESSAGE_PULLCUSTOMEROWNER_ERROR("6081","此客户已经有负责人"),

        MESSAGE_PULLCUSTOMERUPPDATE_ERROR("6082","已转入成功的客户不能再更改"),

        MESSAGE_PRDCUSTOMER_ERROR("6083","这个客户正在被别人操作中"),

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
