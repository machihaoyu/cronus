package com.fjs.cronus.Common;

/**
 * Created by msi on 2017/9/14.
 */
public class ResultResource {
    public static int CODE_SUCCESS = 0;
    public static String MESSAGE_SUCCESS = "成功";
    // 其他错误
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
}
