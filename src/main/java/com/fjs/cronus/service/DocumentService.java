package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.OcrInfoEnum;
import com.fjs.cronus.Common.ProductTyoeEnum;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.App.ClientUploadDTO;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.Echo.MsgTmplDTO;
import com.fjs.cronus.dto.Echo.StationMsgReqDTO;
import com.fjs.cronus.dto.UploadDocumentDto;
import com.fjs.cronus.dto.cronus.*;
import com.fjs.cronus.dto.ocr.*;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.dto.uc.UserSortInfoDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.*;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.model.Document;
import com.fjs.cronus.model.DocumentCategory;
import com.fjs.cronus.model.RContractDocument;
import com.fjs.cronus.service.Echo.EchoService;
import com.fjs.cronus.service.client.TalosService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.*;
import io.swagger.models.auth.In;
import net.coobird.thumbnailator.Thumbnails;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.*;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


import javax.print.Doc;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by msi on 2017/9/20.
 */
@Service
public class DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);
    @Value("${ftp.address}")
    private String FTP_ADDRESS;
    @Value("${ftp.port}")
    private Integer FTP_PORT;
    @Value("${ftp.username}")
    private String FTP_USERNAME;
    @Value("${ftp.password}")
    private String FTP_PASSWORD;
    @Value("${ftp.baseUrl}")
    private String FTP_BASE_PATH;
    @Value("${ftp.basePath}")
    private String IMAGE_BASE_URL;

    private static String endpoint;

    private static String accessKeyId;

    private static String accessKeySecret;

    private static String bucketName;

    private static String aliyunOssUrl;

    @Value("${token.current}")
    private String currenToken;
    @Value("${aliyun.oss.endpoint}")
    public void setEndpoint(String endpoint) {
        DocumentService.endpoint = endpoint;
    }

    @Value("${aliyun.oss.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        DocumentService.accessKeyId = accessKeyId;
    }

    @Value("${aliyun.oss.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        DocumentService.accessKeySecret = accessKeySecret;
    }

    @Value("${aliyun.oss.bucketName}")
    public void setBucketName(String bucketName) {
        DocumentService.bucketName = bucketName;
    }

    @Value("${aliyun.oss.url}")
    public void setAliyunOssUrl(String aliyunOssUrl) {
        DocumentService.aliyunOssUrl = aliyunOssUrl;
    }
    @Autowired
    UcService ucService;
    @Autowired
    CustomerInfoMapper customerInfoMapper;

    @Autowired
    DocumentMapper documentMapper;
    @Autowired
    RContractDocumentMapper rContractDocumentMapper;
    @Autowired
    DocumentCategoryMapper documentCategoryMapper;
    @Autowired
    OcrIdentityService ocrIdentityService;
    @Autowired
    OcrHouseholdRegisterService ocrHouseholdRegisterService;
    @Autowired
    DriverLicenseService driverLicenseService;
    @Autowired
    OcrDriverVehicleService driverVehicleService;
    @Autowired
    HouseRegisterService houseRegisterService;
    @Autowired
    CustomerInfoService customerInfoService;
    /* static final ThreadFactory supplyThreadFactory = new BasicThreadFactory.Builder().namingPattern("tuwenshibie-%d").daemon(true)
             .priority(Thread.MAX_PRIORITY).build();*/
    @Autowired
    private TalosService talosService;
    @Value("${Echo.postAtt}")
    private String postAtt;
    @Autowired
    EchoService echoService;
  /*  */

    /**
     * 图文识别线程池池 用ArrayBlockingQueue比 LinkedBlockingQueue性能要好点。
     *//*
    public static final ExecutorService es = new ThreadPoolExecutor(2, 4, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(5), supplyThreadFactory);*/
    public CronusDto uploadDocument(Integer customerId) {
        CronusDto resultDto = new CronusDto();
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("documentCParentId", 0);
        List<DocumentCategory> documentCategoryList = documentCategoryMapper.getNextCategory(paramsMap);
        List<DocumentCategoryDTO> resultList = new ArrayList<>();
        if (documentCategoryList != null && documentCategoryList.size() > 0) {
            for (DocumentCategory documentCategory : documentCategoryList) {
                DocumentCategoryDTO documentCategoryDTO = new DocumentCategoryDTO();
                documentCategoryDTO.setId(documentCategory.getId());
                documentCategoryDTO.setDocumentCNameHeader(documentCategory.getDocumentCNameHeader());
                documentCategoryDTO.setDocumentCName(documentCategory.getDocumentCNameHeader() + " " + documentCategory.getDocumentCName());
                documentCategoryDTO.setDocumentCParentId(documentCategory.getDocumentCParentId());
                documentCategoryDTO.setCustomerId(customerId);
                resultList.add(documentCategoryDTO);
            }
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setData(resultList);
            return resultDto;
        }

        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setData("");
        return resultDto;
    }

    //生成一条新的合同数据
    @Transactional
    public NewDocumentDTO newDocument(UploadDocumentDTO uploadDocumentDTO, Integer user_id, Integer category, Integer customerId, Integer contratId, String userName) {
        Document document = new Document();
        Integer status = 0;
        Integer documentId = null;
        Map<String, Object> paramsMap = new HashMap<>();
        //检查文件是否重复
        String md5 = uploadDocumentDTO.getMd5();
        Date date = new Date();
        document.setDocumentName(uploadDocumentDTO.getName());
        document.setDocumentSavename(uploadDocumentDTO.getSavename());
        document.setDocumentMd5(uploadDocumentDTO.getMd5());
        document.setDocumentType(uploadDocumentDTO.getType());
        document.setDocumentExt(uploadDocumentDTO.getExt());
        document.setDocumentSavepath(uploadDocumentDTO.getSavepath());
        document.setDocumentSize(Integer.valueOf(uploadDocumentDTO.getSize()));
        document.setCreateTime(date);
        document.setCreateUser(user_id);
        document.setLastUpdateTime(date);
        document.setLastUpdateUser(user_id);
        document.setIsDeleted(0);
        //判断是不是c端
        if ("C".equals(uploadDocumentDTO.getSource())) {
            document.setIsFlag(1);
        } else {
            document.setIsFlag(0);
        }
        documentMapper.addDocument(document);
        documentId = document.getId();
        //}
        if (documentId < 0) {
            status = -1;
        }
        Map<String, Object> contract_document_data = new HashMap<>();
        contract_document_data.put("document_c_id", contratId);
        contract_document_data.put("rc_document_source", uploadDocumentDTO.getSource());
        //添加一条附件与人的关联记录
        RContractDocument rContractDocument = new RContractDocument();
        if (contratId == null) {
            rContractDocument.setContractId(0);
        } else {
            rContractDocument.setContractId(contratId);
        }
        rContractDocument.setCustomerId(customerId);
        rContractDocument.setDocumentId(documentId);
        rContractDocument.setDocumentCId(category);
        rContractDocument.setRcDocumentSource(uploadDocumentDTO.getSource());
        rContractDocument.setCreatorId(user_id);
        rContractDocument.setCreateTime(date);
        rContractDocument.setCreateUser(user_id);
        rContractDocument.setLastUpdateUser(user_id);
        rContractDocument.setLastUpdateTime(date);
        rContractDocument.setIsDeleted(0);
        //插入数据
        rContractDocumentMapper.addConDocument(rContractDocument);
        if (rContractDocument == null) {
            throw new CronusException(CronusException.Type.CRM_CONTROCTDOCU_ERROR);
        }
        Integer rcontrac_doucument_id = rContractDocument.getId();//附件关系id
        NewDocumentDTO newDocumentDTO = new NewDocumentDTO();
        newDocumentDTO.setDocument(document.getDocumentSavepath() + document.getDocumentSavename());
        if (Arrays.<String>asList(ResultResource.FILETYPE).contains(document.getDocumentExt())) {
            //添加缩略图_S _M
            newDocumentDTO.setM_document(document.getDocumentSavepath() + "_M" + document.getDocumentSavename());
            newDocumentDTO.setS_document(document.getDocumentSavepath() + "_S" + document.getDocumentSavename());
        }
        //根据id查询catory信息
        DocumentCategory documentCategory = documentCategoryMapper.selectByKey(category);
        newDocumentDTO.setContract_document_id(rContractDocument.getId());
        newDocumentDTO.setCategory_id(category);
        newDocumentDTO.setCategory_name(documentCategory.getDocumentCNameHeader() + " " + documentCategory.getDocumentCName());
        newDocumentDTO.setContract_id(contratId);
        newDocumentDTO.setExt(document.getDocumentExt());
        newDocumentDTO.setName(uploadDocumentDTO.getName());
        if (contratId != null && contratId > 0) {
            //TODO 根据合同id查询到合同信息
            newDocumentDTO.setM_name("");
        } else {
            newDocumentDTO.setM_name("");
        }
        //TODO 从缓存中查询到相关信息
        newDocumentDTO.setUp_name(userName);
        newDocumentDTO.setUp_date(date);
        newDocumentDTO.setStatus(status);
        return newDocumentDTO;
    }
  /*  public CronusDto getThumbnail(InputStream inputStream, int new_w, int new_h, String thumbName, String thunbPath, String flag) {
        CronusDto resultDto = new CronusDto();
        Map resultMap = new HashMap<>();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Thumbnails.of(inputStream).scale(0.4f).toOutputStream(baos);
            String base64 = new BASE64Encoder().encode(baos.toByteArray());
            if ("0".equals(base64)) {
                resultDto.setResult(ResultResource.CODE_SUCCESS);
                resultDto.setMessage(ResultResource.THUMP_ERROR_MESSAGE);
                return resultDto;
            }
            String imagePath = thunbPath;
            String name = flag + thumbName;
            InputStream is = FileBase64ConvertUitl.decoderBase64File(base64);
            boolean result = FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, IMAGE_BASE_URL, imagePath, name, is);
            if (!result) {
                resultDto.setResult(ResultResource.UPLOAD_ERROR);
                resultDto.setMessage(ResultResource.UPLOAD_ERROR_MESSAGE);
                return resultDto;
            }
            //上传成功
            resultMap.put("url", IMAGE_BASE_URL + imagePath + "/" + name);
            resultMap.put("remotePath", IMAGE_BASE_URL + imagePath + "/");//相对路径
            resultMap.put("name", name);//文件名
            resultMap.put("imagePath", imagePath);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setData(resultMap);
            baos.close();
            return resultDto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultDto;
    }*/

    @Async("mineAsyncPool")
    public boolean addOcrInfo(Integer category, Integer customer_id, String imageBase64, Integer rc_document_id, Integer user_id, String token, UserSortInfoDTO userSortInfoDTO, UserInfoDTO userInfoDTO) {
        final long step1Time = System.currentTimeMillis();
        try {
          /*  es.execute(new Runnable() {
                @Override
                public void run() {*/
                /*    final Thread currentThread = Thread.currentThread();
                    final String oldName = currentThread.getName();
                    currentThread.setName(String.format(currentThread.getName() + "图文识别线程"));
                    long step2Time = System.currentTimeMillis();*/
            try {
                logger.warn("开始通信");
                ReqParamDTO reqParamDTO = addOcrDealParam(category, customer_id, imageBase64, rc_document_id, user_id, token, userSortInfoDTO, userInfoDTO);
                //调用图文识别接口
                talosService.ocrServiceV2(reqParamDTO, currenToken);
            } catch (Exception e) {
                logger.error("charge error ", e);
            } finally {
                       /* logger.warn("通信完成,总耗时: " + (System.currentTimeMillis() - step1Time) + "ms 上游通信耗时:"
                                + (System.currentTimeMillis() - step2Time) + "ms");
                        currentThread.setName(oldName);*/
            }

            /*    }
            });*/

        } catch (RejectedExecutionException e) {
            logger.error("线程池已满", e);
            return false;
        }
        return true;
    }

    @Transactional
    public ReqParamDTO addOcrDealParam(Integer category, Integer customer_id, String imageBase64, Integer rc_document_id, Integer user_id, String token, UserSortInfoDTO userSortInfoDTO, UserInfoDTO userInfoDTO) {

        ReqParamDTO reqParamDTO = new ReqParamDTO();
        //主要业务操作
        // 根据rc_document_id获取document_id
        //拼装参数
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("rc_document_id", rc_document_id);
        RContractDocument rContractDocument = rContractDocumentMapper.findByFeild(paramsMap);
        paramsMap.clear();
        Integer document_id = rContractDocument.getDocumentId();
        //根据customer_id获取客户信息
        List paramsList = new ArrayList();
      /* paramsList.add(customer_id);
       paramsMap.put("paramsList",paramsList);*/
        paramsMap.put("id", customer_id);
        CustomerInfo customerInfo = customerInfoMapper.findByFeild(paramsMap);
        //通过$category获取这个图片的分类属性
        DocumentCategory documentCategory = documentCategoryMapper.selectByKey(category);
        //参数使用json
        JSONObject jsonObject = new JSONObject();
        //OcrSaveBaseInfoDTO ocrSaveBaseInfoDTO = new OcrSaveBaseInfoDTO();
        if (documentCategory.getDocumentCName().indexOf("(正)") != -1) {
            jsonObject.put("side", "face");
            jsonObject.put("type", 1);
        } else if (documentCategory.getDocumentCName().indexOf("(反)") != -1) {
            jsonObject.put("side", "back");
            jsonObject.put("type", 1);
        } else if (documentCategory.getDocumentCName().indexOf("户口簿") != -1) {
            jsonObject.put("type", 2);
        } else if (documentCategory.getDocumentCName().indexOf("驾驶证") != -1) {
            jsonObject.put("type", 3);
        } else if (documentCategory.getDocumentCName().indexOf("行驶证") != -1) {
            jsonObject.put("type", 4);
        } else if (documentCategory.getDocumentCName().indexOf("房产证") != -1) {
            jsonObject.put("type", 5);
        } else {
            jsonObject.put("type", null);
        }
        //UserInfoDTO userInfo = ucService.getUserInfoByID(token,user_id);
     /*  ocrSaveBaseInfoDTO.setR_contract_document(rContractDocument);
       ocrSaveBaseInfoDTO.setCategoryInfo(documentCategory);*/
        jsonObject.put("customer_id", customerInfo.getId());
        jsonObject.put("customer_name", customerInfo.getCustomerName());
        String telephone = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
        String phoneNumber = telephone.substring(0, 3) + "****" + telephone.substring(7, telephone.length());
        jsonObject.put("customer_telephone", phoneNumber);
        jsonObject.put("crm_attach_id", document_id);
        if (userSortInfoDTO != null) {
            jsonObject.put("create_user_id", userSortInfoDTO.getUser_id());
            jsonObject.put("create_user_name", userSortInfoDTO.getName());
            jsonObject.put("update_user_id", userSortInfoDTO.getUser_id());
            jsonObject.put("update_user_name", userSortInfoDTO.getName());
        } else {
            jsonObject.put("create_user_id", userInfoDTO.getUser_id());
            jsonObject.put("create_user_name", userInfoDTO.getName());
            jsonObject.put("update_user_id", userInfoDTO.getUser_id());
            jsonObject.put("update_user_name", userInfoDTO.getName());
        }
        jsonObject.put("category", category);
        // TODO 生成对应的ocr信息
        Integer id = addOrSaveInfo(jsonObject);
        if (id == null) {
            throw new CronusException(CronusException.Type.CRM_OCRINFO_ERROR);
        }
        //
        reqParamDTO.setAttachmentId(Long.parseLong(document_id.toString()));
        reqParamDTO.setCustomerId(Long.parseLong(customerInfo.getId().toString()));
        reqParamDTO.setCustomerName(customerInfo.getCustomerName());
        //解密
        String telephoneNumber = DEC3Util.des3DecodeCBC(customerInfo.getTelephonenumber());
        reqParamDTO.setCustomerTelephone(telephoneNumber);
        reqParamDTO.setImgBase64(imageBase64);
        reqParamDTO.setSide(jsonObject.getString("side"));
        if (jsonObject.getInteger("type") != null) {
            reqParamDTO.setType(jsonObject.getInteger("type").toString());
        }
        if (userSortInfoDTO != null) {
            reqParamDTO.setUserId(Long.parseLong(userSortInfoDTO.getUser_id()));
            reqParamDTO.setUserName(userSortInfoDTO.getName());
        } else {
            reqParamDTO.setUserId(Long.parseLong(userInfoDTO.getUser_id()));
            reqParamDTO.setUserName(userInfoDTO.getName());
        }
        reqParamDTO.setId(Long.parseLong(id.toString()));
        return reqParamDTO;
    }

    @Transactional
    public Integer addOrSaveInfo(JSONObject jsonObject) {
        ReqParamDTO resultDto = new ReqParamDTO();
        if (jsonObject == null) {
            throw new CronusException(CronusException.Type.CEM_CUSTOMERINTERVIEW);
        }
        Integer type = jsonObject.getInteger("type");
        Integer id = null;
        if (type == null) {
            type = 1;
        }
        switch (OcrInfoEnum.getByValue(type)) {
            case ocr_identity:
                //插入一条身份证信息
                //转为身份证
                IdCardDTO idCardDTO = FastJsonUtils.getSingleBean(jsonObject.toString(), IdCardDTO.class);
                id = ocrIdentityService.addOrUpdateOcrInden(idCardDTO);
                break;
            case ocr_householdregister:
                HouseholdRegisterDTO householdRegisterDTO = FastJsonUtils.getSingleBean(jsonObject.toString(), HouseholdRegisterDTO.class);
                id = ocrHouseholdRegisterService.addOrUpdateHouse(householdRegisterDTO);
                break;
            case ocr_driverlicense:
                DriverLicenseDTO driverLicenseDTO = FastJsonUtils.getSingleBean(jsonObject.toString(), DriverLicenseDTO.class);
                id = driverLicenseService.addOrUpdateDriverLicense(driverLicenseDTO);
                break;
            case ocr_drivervehicle:
                DriverVehicleDTO driverVehicleDTO = FastJsonUtils.getSingleBean(jsonObject.toString(), DriverVehicleDTO.class);
                id = driverVehicleService.addOrUpdateDriverVeh(driverVehicleDTO);
                break;
            case ocr_houseRegister:
                HouseRegisterDTO houseRegisterDTO = FastJsonUtils.getSingleBean(jsonObject.toString(), HouseRegisterDTO.class);
                id = houseRegisterService.addOrUpdateHousRegister(houseRegisterDTO);
                break;
            default:
                break;
        }
        return id;
    }

    public String uploadPcDocumentOk(MultipartFile file, String fileName, String contractId, String customerId,
                                     String category, String source, String token) {
        //校验参数
        if (category == null || "".equals(category)) {
            throw new CronusException(CronusException.Type.CRM_OCRDOCUMENTCAGORY_ERROR);
        }
        Integer categoryParam = Integer.valueOf(category);
        Integer contractIdParam = null;
        Integer customerIdParam = null;
        if (contractId != null && !"".equals(contractId)) {
            contractIdParam = Integer.valueOf(contractId);
        }
        if (customerId != null && !"".equals(customerId)) {
            customerIdParam = Integer.valueOf(customerId);
        }

        //参数类型转换

        UserInfoDTO userInfoDTO = ucService.getUserIdByToken(token, CommonConst.SYSTEM_NAME_ENGLISH);
        if (userInfoDTO == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        Integer user_id = Integer.valueOf(userInfoDTO.getUser_id());
        if (user_id == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        //校验文件的类型与大小
        if (file == null) {
            throw new CronusException(CronusException.Type.CRM_NOTNULL_UPLOAD);
        }
        if (file.getSize() > ResultResource.FILEMAXSIZE) {
            throw new CronusException(CronusException.Type.CRM_MAXSIZE_UPLOAD);
        }
        Long size = file.getSize();
        //校验文件格式
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!Arrays.<String>asList(ResultResource.FILETYPE).contains(suffix)) {
            throw new CronusException(CronusException.Type.CRM_FILETYPR_UPLOAD);
        }
        //取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        //long millis = System.nanoTime();
        //加上三位随机数
        Random random = new Random();
        int end3 = random.nextInt(999);
        //如果不足三位前面补0 图片新名称
        String name = millis + String.format("%03d", end3);
        //生成文件md5
        try {
            String md5 = MD5Util.getMd5CodeInputStream(file.getInputStream());
            //开始上传图片
            CronusDto uploadDto = uploadPcStreamDocument(file.getInputStream(), fileName,name + "." + suffix);
            if (uploadDto != null && uploadDto.getData() != null) {
                String result = FastJsonUtils.obj2JsonString(uploadDto.getData());
                //把json格式的数据转为对象

                Map<String, Object> map = FastJsonUtils.getSingleBean(result, Map.class);
                String thumbName = map.get("name").toString();
                String thunbPath = map.get("imagePath").toString();
                String remotePath = map.get("remotePath").toString();
                String url = map.get("url").toString();
              /*  //开始缩放图片
                String bytes = FtpUtil.getInputStream(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, remotePath, thumbName);
                try {
                    InputStream inputStream = FileBase64ConvertUitl.decoderBase64File(bytes);
                    getThumbnail(inputStream, 300, 300, thumbName, thunbPath, "_S");
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                //TODO 验证是否生成
                if (contractId != null && !"".equals(contractId)) {
                    Integer contratId = Integer.valueOf(contractId);
                    //TODO 通过合同查询客户id
                }
                //封装参数
                UploadDocumentDTO paramsDto = new UploadDocumentDTO();
                paramsDto.setName(fileName);
                paramsDto.setExt(suffix);
                paramsDto.setMd5(md5);
                paramsDto.setSavename(thumbName);
                paramsDto.setSavepath(thunbPath + "/");
                paramsDto.setSize(Integer.parseInt(size.toString()));
                paramsDto.setSource(source);
                paramsDto.setType(suffix);
                paramsDto.setKey(name);

                NewDocumentDTO documentDto = newDocument(paramsDto, user_id, categoryParam, customerIdParam, contractIdParam, userInfoDTO.getName());
                if (documentDto.getStatus() == -1) {
                    throw new CronusException(CronusException.Type.CRM_CONTROCTDOCU_ERROR);
                }
                //调用图文识别接口
                Integer rc_document_id = documentDto.getContract_document_id();
                //异步无回调
                //把附件转为base64
                String imageBase64 = FileBase64ConvertUitl.encodeBase64File(file.getInputStream());
                addOcrInfo(categoryParam, customerIdParam, imageBase64, rc_document_id, user_id, token, null, userInfoDTO);
                return url;
            } else {
                throw new CronusException(CronusException.Type.CRM_UPLOADERROR_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public CronusDto uploadPcStreamDocument(InputStream inputStream, String fileName,String newName) {
        CronusDto resultDto = new CronusDto();
        Map resultMap = new HashMap<>();
        //base64 解析成文件流
        try {
            String imagePath = new DateTime().toString("yyyy/MM/dd");
            String keyUrl =CommonConst.THEA_PERFEX + imagePath + "/" + newName;
            String url = OssUtil.uploadImag(fileName, inputStream, keyUrl);
            if (StringUtils.isEmpty(url)) {
                resultDto.setResult(ResultResource.UPLOAD_ERROR);
                resultDto.setMessage(ResultResource.UPLOAD_ERROR_MESSAGE);
                return resultDto;
            }
            //上传成功
            resultMap.put("url", url);
            resultMap.put("remotePath", CommonConst.THEA_PERFEX + imagePath + "/");//相对路径
            resultMap.put("name", newName);//文件名
            resultMap.put("imagePath", imagePath);

            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setData(resultMap);
            return resultDto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultDto;
    }

    public void downloadDocument(HttpServletResponse response, String remotePath, String fileName) {

        //判断参数
        if (remotePath == null || "".equals(remotePath)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (fileName == null || "".equals(fileName)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        //从ftp服务器获取io
        try {
            String bytes = FtpUtil.getInputStream(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, remotePath, fileName);
            byte[] buffer = new BASE64Decoder().decodeBuffer(bytes);
            if (buffer == null || buffer.length == 0) {
                throw new CronusException(CronusException.Type.CRM_DOWNLOADERROR_ERROR);
            }
            // 清空response
            response.reset();
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");

            //如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CronusDto deleteDocument(String remotePath, String fileName, Integer id) {

        CronusDto resultDto = new CronusDto();
        //判断参数
        boolean flag = false;
        //开始删除
        //开始更新数据
        Map<String, Object> parmasMap = new HashMap<>();
        parmasMap.put("rc_document_id", id);

        RContractDocument rContractDocument = rContractDocumentMapper.findByFeild(parmasMap);
        rContractDocument.setIsDeleted(1);
        //找到附件
        Document document = documentMapper.selectByKey(rContractDocument.getDocumentId());
        document.setIsDeleted(1);
        documentMapper.update(document);
        rContractDocumentMapper.update(rContractDocument);
        flag = true;
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(flag);
        return resultDto;
    }

    public CronusDto<Boolean> validDocumentToContract(Integer customerId, Integer productType, String token) {
        CronusDto resultDto = new CronusDto();
        //搜索检索的附件标示
        Map<String, Object> paramsMap = new HashMap<>();
        List<String> document_c_names = Arrays.<String>asList(ResultResource.DOCUMENT_C_NAMES);
        paramsMap.put("document_c_names", document_c_names);
        List<DocumentCategory> documentCategoryList = documentCategoryMapper.findListByFeild(paramsMap);
        //遍历存入
        Map<String, Integer> map = new HashMap<>();

        for (DocumentCategory documentCategory : documentCategoryList) {
            map.put(documentCategory.getDocumentCName(), documentCategory.getId());
        }
        if (documentCategoryList == null) {
            throw new CronusException(CronusException.Type.CRM_VALIDAOCUMENRCOUNT_ERROR);
        }
        if (document_c_names.size() != documentCategoryList.size()) {
            throw new CronusException(CronusException.Type.CRM_VALIDAOCUMENRCOUNT_ERROR);
        }
        //下面找出客户上传了哪些附件
        //根据客户id查询出上传了哪些附件customer_id
        paramsMap.clear();
        paramsMap.put("customerId", customerId);
        List<Integer> documentCids = rContractDocumentMapper.findListByFeild(paramsMap);
        //
        String message = "";
        switch (ProductTyoeEnum.getByValue(productType)) {

            case producttype_credit:
                //判断是否含有身份证正面信息
                if (!documentCids.contains(map.get(ResultResource.INENTITY))) {
                    message = "【借款人身份证】";
                }
                if (!documentCids.contains(map.get(ResultResource.HOUSEHOLD)) && !documentCids.contains(map.get(ResultResource.ACCUMULATION)) && !documentCids.contains(map.get(ResultResource.BACKDEBUT))
                        && !documentCids.contains(map.get(ResultResource.PROOFPOLICY)) && !documentCids.contains(map.get(ResultResource.CERTIFICATE))) {
                    message = message + "【借款人银行流水/房产证/公积金证明/保单证明/行驶证证明 至少需一】";
                }
                if (!documentCids.contains(map.get(ResultResource.CONTRACT)) && !documentCids.contains(map.get(ResultResource.VOUCHER)) && !documentCids.contains(map.get(ResultResource.PAPERMATERIAL))) {
                    message = message + "【借款合同/放款凭证/纸质证明材料 至少需一】";
                }
                //做判断是否上传借款合同/放款凭证/纸质证明材料至少其一

                break;
            case producttype_mortgage:
                if (!documentCids.contains(map.get(ResultResource.INENTITY))) {
                    message = "【请上传借款人身份证】";
                }
                if (!documentCids.contains(map.get(ResultResource.HOUSEREGISTER))) {
                    message = message + "【请上传户口本】";
                }

                if (!documentCids.contains(map.get(ResultResource.HOUSEHOLD)) && !documentCids.contains(map.get(ResultResource.CERTIFICATE))) {
                    message = message + "【房产证/行驶证证明 至少需一】";
                }
                if (!documentCids.contains(map.get(ResultResource.CONTRACT)) && !documentCids.contains(map.get(ResultResource.VOUCHER)) && !documentCids.contains(map.get(ResultResource.PAPERMATERIAL))) {
                    message = message + "【借款合同/放款凭证/纸质证明材料 至少需一】";
                }
                break;
            case producttype_ransomfloor:
                if (!documentCids.contains(map.get(ResultResource.INENTITY))) {
                    message = "【借款人身份证】";
                }
                if (!documentCids.contains(map.get(ResultResource.HOUSEHOLD))) {
                    message = message + "【房产证】";
                }
                if (!documentCids.contains(map.get(ResultResource.HOUSEREGISTER))) {
                    message = message + "【借款人户口簿】";
                }
                if (!documentCids.contains(map.get(ResultResource.CONTRACT)) && !documentCids.contains(map.get(ResultResource.VOUCHER)) && !documentCids.contains(map.get(ResultResource.PAPERMATERIAL))) {
                    message = message + "【借款合同/放款凭证/纸质证明材料 至少需一】";
                }
                break;
            default:
                message = "参数错误";
                break;
        }
        if (!"".equals(message)) {
            message = "请上传附件" + message;
            resultDto.setMessage(message);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setData(false);
            return resultDto;
        }
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(true);
        return resultDto;
    }


    public String uploadClientDocumentOk(InputStream file, String fileName, String contractId, String telephone,
                                         String category, String source, Integer size, String token, String base64, Integer documentId) {
        //校验参数
        if (category == null || "".equals(category)) {
            throw new CronusException(CronusException.Type.CRM_OCRDOCUMENTCAGORY_ERROR);
        }
        Integer categoryParam = Integer.valueOf(category);
        Integer contractIdParam = null;
        Integer customerIdParam = null;
        if (contractId != null && !"".equals(contractId)) {
            contractIdParam = Integer.valueOf(contractId);
        }
        //根据手机号查询
        CronusDto<CustomerDTO> resultDTO = customerInfoService.findCustomerByFeild(null, telephone);
        CustomerDTO customerInfoDTO = resultDTO.getData();
        if (customerInfoDTO == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        //参数类型转换
        customerIdParam = customerInfoDTO.getId();

        UserSortInfoDTO userInfoDTO = ucService.getSortUserInfo(token);
        if (userInfoDTO == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        Integer user_id = Integer.valueOf(userInfoDTO.getUser_id());
        if (user_id == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        //校验文件的类型与大小
        if (file == null) {
            throw new CronusException(CronusException.Type.CRM_NOTNULL_UPLOAD);
        }
        if (size > ResultResource.FILEMAXSIZE) {
            throw new CronusException(CronusException.Type.CRM_MAXSIZE_UPLOAD);
        }
        //
        //校验文件格式
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!Arrays.<String>asList(ResultResource.FILETYPE).contains(suffix)) {
            throw new CronusException(CronusException.Type.CRM_FILETYPR_UPLOAD);
        }
        //取当前时间的长整形值包含毫秒

        if (!StringUtils.isEmpty(documentId)) {
            Map<String, Object> parmasMap = new HashMap<>();
            parmasMap.put("documentId", documentId);
            RContractDocument rContractDocument = rContractDocumentMapper.findByFeild(parmasMap);
            rContractDocument.setIsDeleted(1);
            //找到附件
            Document document = documentMapper.selectByKey(rContractDocument.getDocumentId());
            document.setIsDeleted(1);
            documentMapper.update(document);
            rContractDocumentMapper.update(rContractDocument);
        }
        long millis = System.currentTimeMillis();
        //long millis = System.nanoTime();
        //加上三位随机数
        Random random = new Random();
        int end3 = random.nextInt(999);
        //如果不足三位前面补0 图片新名称
        String name = millis + String.format("%03d", end3);
        //生成文件md5
        try {
            InputStream inputStream = FileBase64ConvertUitl.decoderBase64File(base64);
            String md5 = MD5Util.getMd5CodeInputStream(file);
            //开始上传图片
            String imagePath = new DateTime().toString("yyyy/MM/dd");
            //boolean flag = FtpUtil.uploadClient(base64, FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, IMAGE_BASE_URL + "/", imagePath, name + "." + suffix, file);
            CronusDto uploadDto = uploadPcStreamDocument(inputStream, fileName,name + "." + suffix);
            if (!StringUtils.isEmpty(uploadDto.getData())) {
                //把json格式的数据转为对象

                //Map<String,Object>  map = FastJsonUtils.getSingleBean(result,Map.class);
                String result = FastJsonUtils.obj2JsonString(uploadDto.getData());
                //把json格式的数据转为对象

                Map<String, Object> map = FastJsonUtils.getSingleBean(result, Map.class);
                String thumbName = map.get("name").toString();
                String thunbPath = map.get("imagePath").toString();
                String remotePath = map.get("remotePath").toString();
                String url = map.get("url").toString();
                //开始缩放图片
                // String bytes = FtpUtil.getInputStream(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, remotePath, thumbName);
               /* try {
                    InputStream inputStream = FileBase64ConvertUitl.decoderBase64File(base64);
                    getThumbnail(inputStream, 300, 300, thumbName, remotePath, "_S");
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                //TODO 验证是否生成
                if (contractId != null && !"".equals(contractId)) {
                    Integer contratId = Integer.valueOf(contractId);
                    //TODO 通过合同查询客户id
                }
                //封装参数
                UploadDocumentDTO paramsDto = new UploadDocumentDTO();
                paramsDto.setName(fileName);
                paramsDto.setExt(suffix);
                paramsDto.setMd5(md5);
                paramsDto.setSavename(thumbName);
                paramsDto.setSavepath(thunbPath + "/");
                paramsDto.setSize(size);
                paramsDto.setSource(source);
                paramsDto.setType(suffix);
                paramsDto.setKey(name);

                NewDocumentDTO documentDto = newDocument(paramsDto, user_id, categoryParam, customerIdParam, contractIdParam, userInfoDTO.getName());
                if (documentDto.getStatus() == -1) {
                    throw new CronusException(CronusException.Type.CRM_CONTROCTDOCU_ERROR);
                }
                //调用图文识别接口
                Integer rc_document_id = documentDto.getContract_document_id();
                //异步无回调
                //把附件转为base64
                // String imageBase64 = FileBase64ConvertUitl.encodeBase64File(file);
                //TODO 调用发送消息接口
                try{
                    DocumentCategory documentCategory= documentCategoryMapper.selectByKey(Integer.valueOf(category));
                    MsgTmplDTO msgTmplDTO = echoService.queryMsgTmpl(postAtt);
                    String content = MessageFormat.format(msgTmplDTO.getTmpl(),documentCategory.getDocumentCName());
                    StationMsgReqDTO stationMsgReqDTO = new StationMsgReqDTO();
                    stationMsgReqDTO.setMsgClassify(postAtt);
                    stationMsgReqDTO.setMsgTitle(msgTmplDTO.getTitle());
                    stationMsgReqDTO.setSource("C");
                    stationMsgReqDTO.setMsgContent(content);
                    stationMsgReqDTO.setUserPhone(userInfoDTO.getTelephone());
                    echoService.addStationMsg(stationMsgReqDTO);
                }catch (Exception e){
                    e.printStackTrace();
                }
                addOcrInfo(categoryParam, customerIdParam, base64, rc_document_id, user_id, token, userInfoDTO, null);
                return url;
            } else {
                throw new CronusException(CronusException.Type.CRM_UPLOADERROR_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public SaasDocumentDTO uploadSaasDocumentOk(InputStream file, String fileName, String contractId, String telephone,
                                                String category, String source, Integer size, String token, String base64, Integer documentId) {
        SaasDocumentDTO saasDocumentDTO = new SaasDocumentDTO();
        //校验参数
        if (category == null || "".equals(category)) {
            throw new CronusException(CronusException.Type.CRM_OCRDOCUMENTCAGORY_ERROR);
        }
        Integer categoryParam = Integer.valueOf(category);
        Integer contractIdParam = null;
        Integer customerIdParam = null;
        if (contractId != null && !"".equals(contractId)) {
            contractIdParam = Integer.valueOf(contractId);
        }
        //根据手机号查询
        CronusDto<CustomerDTO> resultDTO = customerInfoService.findCustomerByFeild(null, telephone);
        CustomerDTO customerInfoDTO = resultDTO.getData();
        if (customerInfoDTO == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        //参数类型转换
        customerIdParam = customerInfoDTO.getId();

        UserSortInfoDTO userInfoDTO = ucService.getSortUserInfo(token);
        if (userInfoDTO == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        Integer user_id = Integer.valueOf(userInfoDTO.getUser_id());
        if (user_id == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        //校验文件的类型与大小
        if (file == null) {
            throw new CronusException(CronusException.Type.CRM_NOTNULL_UPLOAD);
        }
        if (size > ResultResource.FILEMAXSIZE) {
            throw new CronusException(CronusException.Type.CRM_MAXSIZE_UPLOAD);
        }
        //
        //校验文件格式
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!Arrays.<String>asList(ResultResource.FILETYPE).contains(suffix)) {
            throw new CronusException(CronusException.Type.CRM_FILETYPR_UPLOAD);
        }
        //取当前时间的长整形值包含毫秒

        if (!StringUtils.isEmpty(documentId)) {
            Map<String, Object> parmasMap = new HashMap<>();
            parmasMap.put("documentId", documentId);
            RContractDocument rContractDocument = rContractDocumentMapper.findByFeild(parmasMap);
            rContractDocument.setIsDeleted(1);
            //找到附件
            Document document = documentMapper.selectByKey(rContractDocument.getDocumentId());
            document.setIsDeleted(1);
            documentMapper.update(document);
            rContractDocumentMapper.update(rContractDocument);
        }
        long millis = System.currentTimeMillis();
        //long millis = System.nanoTime();
        //加上三位随机数
        Random random = new Random();
        int end3 = random.nextInt(999);
        //如果不足三位前面补0 图片新名称
        String name = millis + String.format("%03d", end3);
        //生成文件md5
        try {
            InputStream inputStream = FileBase64ConvertUitl.decoderBase64File(base64);
            String md5 = MD5Util.getMd5CodeInputStream(file);
            CronusDto uploadDto = uploadPcStreamDocument(inputStream, fileName,name + "." + suffix);
            if (!StringUtils.isEmpty(uploadDto.getData())) {
                //把json格式的数据转为对象

                //Map<String,Object>  map = FastJsonUtils.getSingleBean(result,Map.class);
                String result = FastJsonUtils.obj2JsonString(uploadDto.getData());
                //把json格式的数据转为对象

                Map<String, Object> map = FastJsonUtils.getSingleBean(result, Map.class);
                String thumbName = map.get("name").toString();
                String thunbPath = map.get("imagePath").toString();
                String remotePath = map.get("remotePath").toString();
                String url = map.get("url").toString();
                //TODO 验证是否生成
                if (contractId != null && !"".equals(contractId)) {
                    Integer contratId = Integer.valueOf(contractId);
                    //TODO 通过合同查询客户id
                }
                //封装参数
                UploadDocumentDTO paramsDto = new UploadDocumentDTO();
                paramsDto.setName(fileName);
                paramsDto.setExt(suffix);
                paramsDto.setMd5(md5);
                paramsDto.setSavename(thumbName);
                paramsDto.setSavepath(thunbPath + "/");
                paramsDto.setSize(size);
                paramsDto.setSource(source);
                paramsDto.setType(suffix);
                paramsDto.setKey(name);

                NewDocumentDTO documentDto = newDocument(paramsDto, user_id, categoryParam, customerIdParam, contractIdParam, userInfoDTO.getName());
                if (documentDto.getStatus() == -1) {
                    throw new CronusException(CronusException.Type.CRM_CONTROCTDOCU_ERROR);
                }
                //调用图文识别接口
                Integer rc_document_id = documentDto.getContract_document_id();
                addOcrInfo(categoryParam, customerIdParam, base64, rc_document_id, user_id, token, userInfoDTO, null);
                saasDocumentDTO.setUrl(url);
                saasDocumentDTO.setRc_document_Id(rc_document_id);
                return saasDocumentDTO;
            } else {
                throw new CronusException(CronusException.Type.CRM_UPLOADERROR_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ClientUploadDTO uploadH5DocumentOk(MultipartFile file, String fileName, String telephone, String category, String source, String documentId, String token) {
        //校验参数
        ClientUploadDTO clientUploadDTO = new ClientUploadDTO();
        if (category == null || "".equals(category)) {
            throw new CronusException(CronusException.Type.CRM_OCRDOCUMENTCAGORY_ERROR);
        }
        Integer categoryParam = Integer.valueOf(category);
        Integer contractIdParam = null;
        Integer customerIdParam = null;
        CronusDto<CustomerDTO> resultDTO = customerInfoService.findCustomerByFeild(null, telephone);
        CustomerDTO customerInfoDTO = resultDTO.getData();
        if (customerInfoDTO == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        //参数类型转换
        customerIdParam = customerInfoDTO.getId();

        UserSortInfoDTO userInfoDTO = ucService.getSortUserInfo(token);
        if (userInfoDTO == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        Integer user_id = Integer.valueOf(userInfoDTO.getUser_id());
        if (user_id == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        //校验文件的类型与大小
        if (file == null) {
            throw new CronusException(CronusException.Type.CRM_NOTNULL_UPLOAD);
        }
        if (file.getSize() > ResultResource.FILEMAXSIZE) {
            throw new CronusException(CronusException.Type.CRM_MAXSIZE_UPLOAD);
        }
        Long size = file.getSize();
        //校验文件格式
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!Arrays.<String>asList(ResultResource.FILETYPE).contains(suffix)) {
            throw new CronusException(CronusException.Type.CRM_FILETYPR_UPLOAD);
        }
        //取当前时间的长整形值包含毫秒
        if (!StringUtils.isEmpty(documentId)) {
            Map<String, Object> parmasMap = new HashMap<>();
            parmasMap.put("documentId", Integer.valueOf(documentId));
            RContractDocument rContractDocument = rContractDocumentMapper.findByFeild(parmasMap);
            rContractDocument.setIsDeleted(1);
            //找到附件
            Document document = documentMapper.selectByKey(rContractDocument.getDocumentId());
            document.setIsDeleted(1);
            documentMapper.update(document);
            rContractDocumentMapper.update(rContractDocument);
        }
        long millis = System.currentTimeMillis();
        //long millis = System.nanoTime();
        //加上三位随机数
        Random random = new Random();
        int end3 = random.nextInt(999);
        //如果不足三位前面补0 图片新名称
        String name = millis + String.format("%03d", end3);
        //生成文件md5
        try {
            String md5 = MD5Util.getMd5CodeInputStream(file.getInputStream());
            //开始上传图片
            CronusDto uploadDto = uploadPcStreamDocument(file.getInputStream(), fileName,name + "." + suffix);
            if (uploadDto != null && uploadDto.getData() != null) {
                String result = FastJsonUtils.obj2JsonString(uploadDto.getData());
                //把json格式的数据转为对象

                Map<String, Object> map = FastJsonUtils.getSingleBean(result, Map.class);
                String thumbName = map.get("name").toString();
                String thunbPath = map.get("imagePath").toString();
                String remotePath = map.get("remotePath").toString();
                String url = map.get("url").toString();
                //封装参数
                UploadDocumentDTO paramsDto = new UploadDocumentDTO();
                paramsDto.setName(fileName);
                paramsDto.setExt(suffix);
                paramsDto.setMd5(md5);
                paramsDto.setSavename(thumbName);
                paramsDto.setSavepath(thunbPath + "/");
                paramsDto.setSize(Integer.parseInt(size.toString()));
                paramsDto.setSource(source);
                paramsDto.setType(suffix);
                paramsDto.setKey(name);

                NewDocumentDTO documentDto = newDocument(paramsDto, user_id, categoryParam, customerIdParam, contractIdParam, userInfoDTO.getName());
                if (documentDto.getStatus() == -1) {
                    throw new CronusException(CronusException.Type.CRM_CONTROCTDOCU_ERROR);
                }
                //调用图文识别接口
                Integer rc_document_id = documentDto.getContract_document_id();
                //异步无回调
                //把附件转为base64
                try{
                    DocumentCategory documentCategory= documentCategoryMapper.selectByKey(Integer.valueOf(category));
                    MsgTmplDTO msgTmplDTO = echoService.queryMsgTmpl(postAtt);
                    String content = MessageFormat.format(msgTmplDTO.getTmpl(),documentCategory.getDocumentCName());
                    StationMsgReqDTO stationMsgReqDTO = new StationMsgReqDTO();
                    stationMsgReqDTO.setMsgClassify(postAtt);
                    stationMsgReqDTO.setMsgTitle(msgTmplDTO.getTitle());
                    stationMsgReqDTO.setSource("C");
                    stationMsgReqDTO.setMsgContent(content);
                    stationMsgReqDTO.setUserPhone(userInfoDTO.getTelephone());
                    echoService.addStationMsg(stationMsgReqDTO);
                }catch (Exception e){
                    e.printStackTrace();
                }

                String imageBase64 = FileBase64ConvertUitl.encodeBase64File(file.getInputStream());
                addOcrInfo(categoryParam, customerIdParam, imageBase64, rc_document_id, user_id, token, userInfoDTO, null);
                clientUploadDTO.setUrl(url);
                //查询
                Map<String,Object> paramsMap = new HashMap<>();
                paramsMap.put("rc_document_id",rc_document_id);
                RContractDocument rContractDocument = rContractDocumentMapper.findByFeild(paramsMap);
                clientUploadDTO.setDocument_Id(rContractDocument.getDocumentId());
                return clientUploadDTO;
            } else {
                throw new CronusException(CronusException.Type.CRM_UPLOADERROR_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String uploadType(MultipartFile file, String fileName, String contractId, String serviceContractId,String customerId,
                                     String category, String source, String token) {
        //校验参数
        if (category == null || "".equals(category)) {
            throw new CronusException(CronusException.Type.CRM_OCRDOCUMENTCAGORY_ERROR);
        }
        Integer categoryParam = Integer.valueOf(category);
        Integer contractIdParam = null;
        Integer customerIdParam = null;
        Integer serviceContractIdParam = null;
        if (contractId != null && !"".equals(contractId)) {
            contractIdParam = Integer.valueOf(contractId);
        }
        if (!StringUtils.isEmpty(serviceContractId)){
            serviceContractIdParam = Integer.valueOf(serviceContractId);
        }
        if (customerId != null && !"".equals(customerId) && !"null".equals(customerId)) {
            customerIdParam = Integer.valueOf(customerId);
        }
        UserInfoDTO userInfoDTO = ucService.getUserIdByToken(token, CommonConst.SYSTEM_NAME_ENGLISH);
        if (userInfoDTO == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        Integer user_id = Integer.valueOf(userInfoDTO.getUser_id());
        if (user_id == null) {
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
        }
        //校验文件的类型与大小
        if (file == null) {
            throw new CronusException(CronusException.Type.CRM_NOTNULL_UPLOAD);
        }
        if (file.getSize() > ResultResource.FILEMAXSIZE) {
            throw new CronusException(CronusException.Type.CRM_MAXSIZE_UPLOAD);
        }
        Long size = file.getSize();
        //校验文件格式
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!Arrays.<String>asList(ResultResource.FILETYPE).contains(suffix)) {
            throw new CronusException(CronusException.Type.CRM_FILETYPR_UPLOAD);
        }
        //取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        //加上三位随机数
        Random random = new Random();
        int end3 = random.nextInt(999);
        //如果不足三位前面补0 图片新名称
        String name = millis + String.format("%03d", end3);
        //生成文件md5
        try {
            String md5 = MD5Util.getMd5CodeInputStream(file.getInputStream());
            //开始上传图片
            CronusDto uploadDto = uploadPcStreamDocument(file.getInputStream(), fileName,name + "." + suffix);
            if (uploadDto != null && uploadDto.getData() != null) {
                String result = FastJsonUtils.obj2JsonString(uploadDto.getData());
                //把json格式的数据转为对象
                Map<String, Object> map = FastJsonUtils.getSingleBean(result, Map.class);
                String thumbName = map.get("name").toString();
                String thunbPath = map.get("imagePath").toString();
                String url = map.get("url").toString();
                //封装参数
                UploadDocumentDTO paramsDto = new UploadDocumentDTO();
                paramsDto.setName(fileName);
                paramsDto.setExt(suffix);
                paramsDto.setMd5(md5);
                paramsDto.setSavename(thumbName);
                paramsDto.setSavepath(thunbPath + "/");
                paramsDto.setSize(Integer.parseInt(size.toString()));
                paramsDto.setSource(source);
                paramsDto.setType(suffix);
                paramsDto.setKey(name);

                NewDocumentDTO documentDto = newType(paramsDto, user_id, categoryParam, customerIdParam, contractIdParam,
                        serviceContractIdParam, userInfoDTO.getName());
                if (documentDto.getStatus() == -1) {
                    throw new CronusException(CronusException.Type.CRM_CONTROCTDOCU_ERROR);
                }
                //调用图文识别接口
//                Integer rc_document_id = documentDto.getContract_document_id();
                //异步无回调
                //把附件转为base64
//                String imageBase64 = FileBase64ConvertUitl.encodeBase64File(file.getInputStream());
//                addOcrInfo(categoryParam, customerIdParam, imageBase64, rc_document_id, user_id, token, null, userInfoDTO);
                return url;
            } else {
                throw new CronusException(CronusException.Type.CRM_UPLOADERROR_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //生成一条新的合同数据
    @Transactional
    public NewDocumentDTO newType(UploadDocumentDTO uploadDocumentDTO, Integer user_id, Integer category, Integer customerId,
                                      Integer contratId, Integer serviceContratId, String userName) {
        Document document = new Document();
        Integer status = 0;
        Integer documentId = null;
        Map<String, Object> paramsMap = new HashMap<>();
        //检查文件是否重复
        String md5 = uploadDocumentDTO.getMd5();
        Date date = new Date();
        document.setDocumentName(uploadDocumentDTO.getName());
        document.setDocumentSavename(uploadDocumentDTO.getSavename());
        document.setDocumentMd5(uploadDocumentDTO.getMd5());
        document.setDocumentType(uploadDocumentDTO.getType());
        document.setDocumentExt(uploadDocumentDTO.getExt());
        document.setDocumentSavepath(uploadDocumentDTO.getSavepath());
        document.setDocumentSize(Integer.valueOf(uploadDocumentDTO.getSize()));
        document.setCreateTime(date);
        document.setCreateUser(user_id);
        document.setLastUpdateTime(date);
        document.setLastUpdateUser(user_id);
        document.setIsDeleted(0);
        //判断是不是c端
        if ("C".equals(uploadDocumentDTO.getSource())) {
            document.setIsFlag(1);
        } else {
            document.setIsFlag(0);
        }
        documentMapper.addDocument(document);
        documentId = document.getId();
        //}
        if (documentId < 0) {
            status = -1;
        }
        Map<String, Object> contract_document_data = new HashMap<>();
        contract_document_data.put("document_c_id", contratId);
        contract_document_data.put("rc_document_source", uploadDocumentDTO.getSource());
        //添加一条附件与人的关联记录
        RContractDocument rContractDocument = new RContractDocument();
        if (contratId == null) {
            rContractDocument.setContractId(0);
        } else {
            rContractDocument.setContractId(contratId);
        }
        if (serviceContratId == null) {
            rContractDocument.setServiceContractId(0);
        } else {
            rContractDocument.setServiceContractId(serviceContratId);
        }
        rContractDocument.setCustomerId(customerId);
        rContractDocument.setDocumentId(documentId);
        rContractDocument.setDocumentCId(category);
        rContractDocument.setRcDocumentSource(uploadDocumentDTO.getSource());
        rContractDocument.setCreatorId(user_id);
        rContractDocument.setCreateTime(date);
        rContractDocument.setCreateUser(user_id);
        rContractDocument.setLastUpdateUser(user_id);
        rContractDocument.setLastUpdateTime(date);
        rContractDocument.setIsDeleted(0);
        //插入数据
        rContractDocumentMapper.addConDocument(rContractDocument);
        if (rContractDocument == null) {
            throw new CronusException(CronusException.Type.CRM_CONTROCTDOCU_ERROR);
        }
        NewDocumentDTO newDocumentDTO = new NewDocumentDTO();
        newDocumentDTO.setDocument(document.getDocumentSavepath() + document.getDocumentSavename());
        if (Arrays.<String>asList(ResultResource.FILETYPE).contains(document.getDocumentExt())) {
            //添加缩略图_S _M
            newDocumentDTO.setM_document(document.getDocumentSavepath() + "_M" + document.getDocumentSavename());
            newDocumentDTO.setS_document(document.getDocumentSavepath() + "_S" + document.getDocumentSavename());
        }
        //根据id查询catory信息
        DocumentCategory documentCategory = documentCategoryMapper.selectByKey(category);
        newDocumentDTO.setContract_document_id(rContractDocument.getId());
        newDocumentDTO.setCategory_id(category);
        newDocumentDTO.setCategory_name(documentCategory.getDocumentCNameHeader() + " " + documentCategory.getDocumentCName());
        newDocumentDTO.setContract_id(contratId);
        newDocumentDTO.setExt(document.getDocumentExt());
        newDocumentDTO.setName(uploadDocumentDTO.getName());
        if (contratId != null && contratId > 0) {
            //TODO 根据合同id查询到合同信息
            newDocumentDTO.setM_name("");
        } else {
            newDocumentDTO.setM_name("");
        }
        //TODO 从缓存中查询到相关信息
        newDocumentDTO.setUp_name(userName);
        newDocumentDTO.setUp_date(date);
        newDocumentDTO.setStatus(status);
        return newDocumentDTO;
    }

    public CronusDto<Boolean> validType(Integer serviceContractId, Integer productType, String token) {
        CronusDto resultDto = new CronusDto();
        //搜索检索的附件标示
        Map<String, Object> paramsMap = new HashMap<>();
        List<String> document_c_names = Arrays.<String>asList(ResultResource.DOCUMENT_C_NAMES);
        paramsMap.put("document_c_names", document_c_names);
        List<DocumentCategory> documentCategoryList = documentCategoryMapper.findListByFeild(paramsMap);
        //遍历存入
        Map<String, Integer> map = new HashMap<>();
        for (DocumentCategory documentCategory : documentCategoryList) {
            map.put(documentCategory.getDocumentCName(), documentCategory.getId());
        }
        if (documentCategoryList == null) {
            throw new CronusException(CronusException.Type.CRM_VALIDAOCUMENRCOUNT_ERROR);
        }
        if (document_c_names.size() != documentCategoryList.size()) {
            throw new CronusException(CronusException.Type.CRM_VALIDAOCUMENRCOUNT_ERROR);
        }
        //下面找出客户上传了哪些附件
        //根据客户id查询出上传了哪些附件customer_id
        paramsMap.clear();
        paramsMap.put("serviceContractId", serviceContractId);
        List<Integer> documentCids = rContractDocumentMapper.findTypeByFeild(paramsMap);
        String message = "";
        switch (ProductTyoeEnum.getByValue(productType)) {
            case producttype_credit:
                //判断是否含有身份证正面信息
//                if (!documentCids.contains(map.get(ResultResource.INENTITY))) {
//                    message = "【借款人身份证】";
//                }
//                if (!documentCids.contains(map.get(ResultResource.HOUSEHOLD)) && !documentCids.contains(map.get(ResultResource.ACCUMULATION)) && !documentCids.contains(map.get(ResultResource.BACKDEBUT))
//                        && !documentCids.contains(map.get(ResultResource.PROOFPOLICY)) && !documentCids.contains(map.get(ResultResource.CERTIFICATE))) {
//                    message = message + "【借款人银行流水/房产证/公积金证明/保单证明/行驶证证明 至少需一】";
//                }
                if (!documentCids.contains(map.get(ResultResource.CONTRACT)) && !documentCids.contains(map.get(ResultResource.VOUCHER)) && !documentCids.contains(map.get(ResultResource.PAPERMATERIAL))) {
                    message = message + "【借款合同/放款凭证/纸质证明材料 至少需一】";
                }
                break;
            case producttype_mortgage:
//                if (!documentCids.contains(map.get(ResultResource.INENTITY))) {
//                    message = "【请上传借款人身份证】";
//                }
//                if (!documentCids.contains(map.get(ResultResource.HOUSEREGISTER))) {
//                    message = message + "【请上传户口本】";
//                }
//                if (!documentCids.contains(map.get(ResultResource.HOUSEHOLD)) && !documentCids.contains(map.get(ResultResource.CERTIFICATE))) {
//                    message = message + "【房产证/行驶证证明 至少需一】";
//                }
                if (!documentCids.contains(map.get(ResultResource.CONTRACT)) && !documentCids.contains(map.get(ResultResource.VOUCHER)) && !documentCids.contains(map.get(ResultResource.PAPERMATERIAL))) {
                    message = message + "【借款合同/放款凭证/纸质证明材料 至少需一】";
                }
                break;
            case producttype_ransomfloor:
//                if (!documentCids.contains(map.get(ResultResource.INENTITY))) {
//                    message = "【借款人身份证】";
//                }
//                if (!documentCids.contains(map.get(ResultResource.HOUSEHOLD))) {
//                    message = message + "【房产证】";
//                }
//                if (!documentCids.contains(map.get(ResultResource.HOUSEREGISTER))) {
//                    message = message + "【借款人户口簿】";
//                }
                if (!documentCids.contains(map.get(ResultResource.CONTRACT)) && !documentCids.contains(map.get(ResultResource.VOUCHER)) && !documentCids.contains(map.get(ResultResource.PAPERMATERIAL))) {
                    message = message + "【借款合同/放款凭证/纸质证明材料 至少需一】";
                }
                break;
            default:
                message = "参数错误";
                break;
        }
        if (!"".equals(message)) {
            message = "请上传附件" + message;
            resultDto.setMessage(message);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setData(false);
            return resultDto;
        }
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setData(true);
        return resultDto;
    }
}
