package com.fjs.cronus.Common;

/**
 * Created by msi on 2017/9/14.
 */
public class ResultResource {
    public static int CODE_SUCCESS = 0;
    public static String MESSAGE_SUCCESS = "成功";
    // 其他错误
    public static int CODE_ERROR = 0;
    public static String CODE_REMOVE_MESSAGE="您没有权限！";

    public static int CODE_OTHER_ERROR = 999999;
    public static String MESSAGE_OTHER_ERROR = "其他错误";

    public static  String LEAVELLINKAGE_KEY = "PROVIENCECITYAREA";
    public static  String USERINFOBYID = "UserInfoByID_";

    public static String SUBUSERBYIDS = "SubUserByUserId_";

    public static Integer FILEMAXSIZE = 20971520;
    public static String[] FILETYPE = {"pdf","doc","docx","xls","xlsx","txt","rar","zip","jpg","png","jpeg","gif"};

    public static int UPLOAD_ERROR = 1;
    public static String UPLOAD_ERROR_MESSAGE ="上传图片失败";

    public static String THUMP_ERROR_MESSAGE = "图片不需要缩放";

    public static String PHONEKEY = "37mri8MKfMoxjB0g";

    public static String OCRSTATUS = "已校验";

    public static String SYSTEMNAME ="sale";

    public static String DELETEERROR = "删除文件失败！";

    public static String INENTITY = "借款人(正)";

    public static String HOUSEHOLD = "房产证";

    public static String[] CITYS = {"'北京'","'深圳'","'郑州'","'上海'","'杭州'","'苏州'"};

    public static  String CALLBACKCONFIG_KEY = "CALLBACJCONFIG";

    public static  String CUSTOMERTYPE = "意向客户";

    public static  String CUSTOMERSTATUS = "正常";

    public static String[] CALLBACKSTATUS ={"'正常'","'未接'","'待联系'","'空号'","'拒接'","'拒访'","'其他'"};

    public static String[] DOCUMENT_C_NAMES = {"'借款人(正)'","'房产证'","'借款人户口簿'","'借款人银行流水'","'公积金证明'","'保单证明'","'行驶证证明'","'借款合同'","'放款凭证'","'纸质证明材料'"};

    public static String HOUSEREGISTER = "借款人户口簿";

    public static String BACKDEBUT = "借款人银行流水";

    public static String ACCUMULATION = "公积金证明";

    public static String PROOFPOLICY = "保单证明";

    public static String CERTIFICATE = "行驶证证明";

    /**
     * 借款合同/放款凭证/纸质证明材料
     */
    public static String  CONTRACT = "借款合同";

    public static String  VOUCHER ="放款凭证";

    public static String PAPERMATERIAL = "纸质证明材料";

    public static String DOWNLOADFOOTPATH = "/Uploads/";

    public static String DOWNLOADSUFFX = "/";

    public static String PHNOEERROR ="手机号已存在";

    public static String REPEATDOCUMET = "附件包含已经上传过的附件，请重新选择";

    public static String MESSAGE_REMOVECUSTOERAll_ERROR = "请选择需要转移的负责人";

    public static String MESSAGE_REMOVECUSTOERSTATUS_ERROR = "请选择在职的员工转移";

    public static String MESSAGE_REMOVECUSTNOTNULL_ERROR = "请先选择客户";

    public static String CRM_CUSOMERALLACATE_ERROR = "存在新分配未沟通客户,不允许此操作";

    public static String CRM_CONTRACTINFO_ERROR = "链接交易系统，更改合同状态失败";

    public static String CRM_THEA_ERROR = "链接交易系统，更改回款等信息状态失败";

    public static String CRM_MOVE_SUCESSS = "转移成功";

}
