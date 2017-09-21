package com.fjs.cronus.service;

import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.controller.LeavelLinkAgeController;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.UploadDocumentDto;
import com.fjs.cronus.dto.cronus.DocumentCategoryDto;
import com.fjs.cronus.dto.cronus.NewDocumentDto;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.DocumentCategoryMapper;
import com.fjs.cronus.mappers.DocumentMapper;
import com.fjs.cronus.mappers.RContractDocumentMapper;
import com.fjs.cronus.model.Document;
import com.fjs.cronus.model.DocumentCategory;
import com.fjs.cronus.model.RContractDocument;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
/**
 * Created by msi on 2017/9/20.
 */
@Service
public class DocumentService {

    private  static  final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    private String FTP_ADDRESS;
    private Integer FTP_PORT;
    private String FTP_USERNAME;
    private String FTP_PASSWORD;
    private String FTP_BASE_PATH;
    private String IMAGE_BASE_URL;

    @Autowired
    UcService ucService;

    @Autowired
    DocumentMapper documentMapper;
    @Autowired
    RContractDocumentMapper rContractDocumentMapper;
    @Autowired
    DocumentCategoryMapper documentCategoryMapper;

    static final ThreadFactory supplyThreadFactory = new BasicThreadFactory.Builder().namingPattern("tuwenshibie-%d").daemon(true)
            .priority(Thread.MAX_PRIORITY).build();

    /**
     * 图文识别程池 用ArrayBlockingQueue比 LinkedBlockingQueue性能要好点。
     */
    public static final ExecutorService es = new ThreadPoolExecutor(100, 200, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(50000), supplyThreadFactory);
    public CronusDto uploadDocument(Integer customerId){

        //TODO 查询客户的基础信息
        //查询所有的父类信息
        CronusDto resultDto = new CronusDto();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("documentCParentId",0);
        List<DocumentCategory> documentCategoryList = documentCategoryMapper.getNextCategory(paramsMap);
        List<DocumentCategoryDto> resultList = new ArrayList<>();
        if (documentCategoryList != null && documentCategoryList.size() > 0 ){
            for (DocumentCategory documentCategory : documentCategoryList) {
                DocumentCategoryDto documentCategoryDTO = new DocumentCategoryDto();
                documentCategoryDTO.setId(documentCategory.getId());
                documentCategoryDTO.setDocumentCNameHeader(documentCategory.getDocumentCNameHeader());
                documentCategoryDTO.setDocumentCName(documentCategory.getDocumentCNameHeader()+" "+documentCategory.getDocumentCName());
                documentCategoryDTO.setDocumentCParentId(documentCategory.getDocumentCParentId());
                documentCategoryDTO.setCustomerId(customerId);
                resultList.add(documentCategoryDTO);
            }
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setData(resultList);
            return  resultDto;
        }

        resultDto.setResult(ResultResource.CODE_SUCCESS);
        resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        resultDto.setData("");
        return  resultDto;
    }
    @Transactional
    public CronusDto uploadDocumentOk(List<UploadDocumentDto> uploadDocumentDtoList,String token){
        CronusDto resultDto = new CronusDto();
        List<NewDocumentDto> resultList = new ArrayList<>();
        for (UploadDocumentDto uploadDocumentDto:uploadDocumentDtoList ) {
            Integer category = uploadDocumentDto.getCategory();
            if (category == null){
                category = 0;
            }
            //通过token获取用户信息
            Integer user_id = ucService.getUserIdByToken(token);
            if (user_id == null){
                throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR);
            }
            //校验文件的类型与大小
            Integer size = uploadDocumentDto.getSize();
            if (ResultResource.FILEMAXSIZE < size ){
                throw new CronusException(CronusException.Type.CRM_MAXSIZE_UPLOAD);
            }
            //校验文件格式
            String type = uploadDocumentDto.getType();
            if (!Arrays.<String> asList(ResultResource.FILETYPE).contains(type)){
                throw new CronusException(CronusException.Type.CRM_FILETYPR_UPLOAD);
            }
            //开始上传文件
            //取当前时间的长整形值包含毫秒
            long millis = System.currentTimeMillis();
            //long millis = System.nanoTime();
            //加上三位随机数
            Random random = new Random();
            int end3 = random.nextInt(999);
            //如果不足三位前面补0 图片新名称
            String name = millis + String.format("%03d", end3);
            //生成文件md5
            String md5 = MD5Util.getMd5Code(uploadDocumentDto.getImageBase64());
            CronusDto uploadDto = uploadStreamDocument(uploadDocumentDto.getImageBase64(),name+ "." + type);
            //生成缩略图
            if(uploadDto != null && uploadDto.getData() != null){
                String result = FastJsonUtils.obj2JsonString(uploadDto.getData());
                //把json格式的数据转为对象

                Map<String,Object>  map = FastJsonUtils.getSingleBean(result,Map.class);
                String thumbName  = map.get("name").toString();
                String thunbPath  = map.get("imagePath").toString();
                String remotePath = map.get("remotePath").toString();
                //开始缩放图片
                InputStream inputStream = FtpUtil.getInputStream(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, remotePath, thumbName);
                getThumbnail(inputStream,300,300,thumbName,thunbPath,"_S");
                getThumbnail(inputStream,500,500,thumbName,thunbPath,"_M");
                //TODO 验证是否生成
                if (uploadDocumentDto.getContractId() != null && uploadDocumentDto.getContractId() > 0){
                    Integer contratId = uploadDocumentDto.getContractId();
                    //TODO 通过合同查询客户id
                }
                //封装参数
                com.fjs.cronus.dto.cronus.UploadDocumentDto paramsDto = new  com.fjs.cronus.dto.cronus.UploadDocumentDto();
                paramsDto.setName(uploadDocumentDto.getFileName());
                paramsDto.setExt(uploadDocumentDto.getType());
                paramsDto.setMd5(md5);
                paramsDto.setSavename(thumbName);
                paramsDto.setSavepath(thunbPath);
                paramsDto.setSize(uploadDocumentDto.getSize());
                paramsDto.setSource(uploadDocumentDto.getSource());
                paramsDto.setType(uploadDocumentDto.getType());
                paramsDto.setKey(name);
                NewDocumentDto documentDto = newDocument(paramsDto,user_id,category,uploadDocumentDto.getCustomerId(),uploadDocumentDto.getContractId());
                if (documentDto.getStatus() == -1){
                    throw new CronusException(CronusException.Type.CRM_CONTROCTDOCU_ERROR);
                }
                resultList.add(documentDto);
                //调用图文识别接口
                Integer rc_document_id =documentDto.getContract_document_id();
                //addOcrInfo();
            }else {
                resultDto.setMessage(ResultResource.UPLOAD_ERROR_MESSAGE);
                resultDto.setResult(ResultResource.UPLOAD_ERROR);
            }
        }
        return  resultDto;
    }

    //生成一条新的合同数据
    @Transactional
    public NewDocumentDto newDocument (com.fjs.cronus.dto.cronus.UploadDocumentDto uploadDocumentDto,Integer user_id,Integer category,Integer customerId,Integer contratId ){
        Document document = new Document();
        Integer status = 0;
        Integer documentId = null;
        Map<String,Object> paramsMap = new HashMap<>();
        //检查文件是否重复
        String md5 = uploadDocumentDto.getMd5();
        paramsMap.put("documentMd5",md5);
        Document document1 = documentMapper.findByFeild(paramsMap);
        paramsMap.clear();
        Date date = new Date();
        if (document1 != null){
            document = document1;
            documentId = document1.getId();
        }else {
            //增加一条新的数据
            document.setDocumentName(uploadDocumentDto.getName());
            document.setDocumentSavename(uploadDocumentDto.getSavename());
            document.setDocumentMd5(uploadDocumentDto.getMd5());
            document.setDocumentType(uploadDocumentDto.getType());
            document.setDocumentExt(uploadDocumentDto.getExt());
            document.setDocumentSavepath(uploadDocumentDto.getSavepath());
            document.setDocumentSize(Integer.valueOf(uploadDocumentDto.getSize()));
            document.setCreateTime(date);
            document.setCreateUser(user_id);
            document.setLastUpdateTime(date);
            document.setLastUpdateUser(user_id);
            document.setIsDeleted(0);
            documentMapper.addDocument(document);
            documentId = document.getId();
        }
        if (documentId < 0){
            status = -1;
        }
        Map<String,Object> contract_document_data = new HashMap<>();
        contract_document_data.put("document_c_id",contratId);
        contract_document_data.put("rc_document_source",uploadDocumentDto.getSource());
        //添加一条附件与人的关联记录
        RContractDocument rContractDocument = new RContractDocument();
        if (contratId == null) {
            rContractDocument.setContractId(0);
        }else {
            rContractDocument.setContractId(contratId);
        }
        rContractDocument.setCustomerId(customerId);
        rContractDocument.setDocumentId(documentId);
        rContractDocument.setDocumentCId(category);
        rContractDocument.setRcDocumentSource(uploadDocumentDto.getSource());
        rContractDocument.setCreatorId(user_id);
        rContractDocument.setCreateTime(date);
        rContractDocument.setCreateUser(user_id);
        rContractDocument.setLastUpdateUser(user_id);
        rContractDocument.setLastUpdateTime(date);
        rContractDocument.setIsDeleted(0);
        //插入数据
       rContractDocumentMapper.addConDocument(rContractDocument);
       if (rContractDocument == null){
           throw new CronusException(CronusException.Type.CRM_CONTROCTDOCU_ERROR);
       }
        Integer rcontrac_doucument_id = rContractDocument.getId();//附件关系id
        NewDocumentDto newDocumentDto = new NewDocumentDto();
        newDocumentDto.setDocument(document.getDocumentSavepath() + document.getDocumentSavename());
        if (Arrays.<String> asList(ResultResource.FILETYPE).contains(document.getDocumentExt())){
            //添加缩略图_S _M
            newDocumentDto.setM_document(document.getDocumentSavepath() + "_M" + document.getDocumentSavename());
            newDocumentDto.setS_document(document.getDocumentSavepath() + "_S" + document.getDocumentSavename());
        }
        //根据id查询catory信息
        DocumentCategory documentCategory = documentCategoryMapper.selectByPrimaryKey(category);
        newDocumentDto.setContract_document_id(rContractDocument.getId());
        newDocumentDto.setCategory_id(category);
        newDocumentDto.setCategory_name(documentCategory.getDocumentCNameHeader()+ " " +documentCategory.getDocumentCName());
        newDocumentDto.setContract_id(contratId);
        newDocumentDto.setExt(document.getDocumentExt());
        newDocumentDto.setName(uploadDocumentDto.getName());
        if (contratId != null && contratId > 0){
            //TODO 根据合同id查询到合同信息
            newDocumentDto.setM_name("");
        }else {
            newDocumentDto.setM_name("");
        }
        //TODO 从缓存中查询到相关信息
        newDocumentDto.setUp_name("");
        newDocumentDto.setUp_date(date);
        newDocumentDto.setStatus(status);
        return  newDocumentDto;
    }
    public CronusDto uploadStreamDocument(String imageBase64,String name){
        CronusDto resultDto = new CronusDto();
        Map resultMap = new HashMap<>();
        //base64 解析成文件流
        try{
            //文件路径
            String imagePath = new DateTime().toString("/yyyy/MM/dd");
           InputStream inputStream = FileBase64ConvertUitl.decoderBase64File(imageBase64);
           boolean flag=FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, FTP_BASE_PATH, imagePath, name, inputStream);
            if(!flag) {
                resultDto.setResult(ResultResource.UPLOAD_ERROR);
                resultDto.setMessage(ResultResource.UPLOAD_ERROR_MESSAGE);
                return resultDto;
            }
            //上传成功
            resultMap.put("url", IMAGE_BASE_URL + imagePath + "/" + name);
            resultMap.put("remotePath",IMAGE_BASE_URL + imagePath + "/");//相对路径
            resultMap.put("name",name);//文件名
            resultMap.put("imagePath",IMAGE_BASE_URL+imagePath + "/");

            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setData(resultMap);
            return resultDto;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  resultDto;
    }
    public CronusDto getThumbnail(InputStream inputStream, int new_w, int new_h,String thumbName,String thunbPath,String flag){
        CronusDto resultDto = new CronusDto();
        Map resultMap = new HashMap<>();
        try{
            String base64 = ImageUtil.compressImage(inputStream,new_w,new_h);
            if ("0".equals(base64)){
                resultDto.setResult(ResultResource.CODE_SUCCESS);
                resultDto.setMessage(ResultResource.THUMP_ERROR_MESSAGE);
                return  resultDto;
            }
            String imagePath =thunbPath;
            String name = flag + thumbName;
            InputStream is = FileBase64ConvertUitl.decoderBase64File(base64);
            boolean result=FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, FTP_BASE_PATH, imagePath, name, is);
            if(!result) {
                resultDto.setResult(ResultResource.UPLOAD_ERROR);
                resultDto.setMessage(ResultResource.UPLOAD_ERROR_MESSAGE);
                return resultDto;
            }
            //上传成功
            resultMap.put("url", IMAGE_BASE_URL + imagePath + "/" + name);
            resultMap.put("remotePath",IMAGE_BASE_URL + imagePath + "/");//相对路径
            resultMap.put("name",name);//文件名
            resultMap.put("imagePath",imagePath);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
            resultDto.setData(resultMap);
            return resultDto;
        }catch (Exception e){
            e.printStackTrace();
        }
    return resultDto;
    }
   public boolean addOcrInfo(Integer category,Integer customer_id,String imageBase64,Integer rc_document_id,Integer user_id){
       final long step1Time = System.currentTimeMillis();
       try {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    //主要业务操作


                }
            });

       }catch (RejectedExecutionException e){
         logger.error("线程池已满",e);
         return  false;
       }
     return  true;
   }

}
