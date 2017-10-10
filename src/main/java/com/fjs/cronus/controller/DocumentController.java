package com.fjs.cronus.controller;

import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.UploadDocumentDto;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.model.Document;
import com.fjs.cronus.service.DocumentCategoryService;
import com.fjs.cronus.service.DocumentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by msi on 2017/9/20.
 */
@Controller
@RequestMapping("/api/v1")
public class DocumentController {

    private  static  final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    DocumentService documentService;
    @Autowired
    DocumentCategoryService documentCategoryService;
    @ApiOperation(value="打开附件管理页面", notes="打开附件管理页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/uploadDocument", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto uploadDocument(@RequestParam Integer customerId){
        CronusDto cronusDto = new CronusDto();

        try {
            cronusDto = documentService.uploadDocument(customerId);
        }catch (Exception e){
            logger.error("--------------->uploadDocument 打开附件管理页面", e);
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }


        return  cronusDto;
    }

    @ApiOperation(value="提交上传附件", notes="提交上传附件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "imageBase64", value = "文件流64编码", required = true, paramType = "query", dataType = "String"),
    })
    @RequestMapping(value = "/uploadDocumentOk",method = RequestMethod.POST)
    @ResponseBody
    public CronusDto uploadDocumentOk(@RequestHeader("Authorization") String token, @RequestBody UploadDocumentDto uploadDocumentDto ){

        CronusDto resultDto = new CronusDto();
        try {
            resultDto = documentService.uploadDocumentOk(uploadDocumentDto,token);
            return  resultDto;
        }catch (Exception e){
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException)e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }



    @ApiOperation(value="获取附件三价联动的信息", notes="获取附件三价联动的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "cateGoryParentId", value = "父级id", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/getNextCategoryCrm", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto getNextCategory(@RequestParam Integer cateGoryParentId){
        CronusDto cronusDto = new CronusDto();

        try {
            cronusDto = documentCategoryService.getNextCategory(cateGoryParentId);
        }catch (Exception e){
            logger.error("--------------->getNextCategory 获取附件三价联动的信息", e);
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  cronusDto;
    }
    @ApiOperation(value="Pc端提交上传附件", notes="Pc端提交上传附件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/uploadPcDocumentOk",method = RequestMethod.POST)
    @ResponseBody
    public CronusDto uploadPcDocumentOk(@RequestHeader("Authorization") String token,HttpServletRequest request){
        logger.info("start uploadTopicPictureList!");
        CronusDto resultDto = new CronusDto();
        List fileList=new ArrayList();
        try {
            //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
            CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                    request.getSession().getServletContext());
            logger.info("End CommonsMultipartResolver!");
            if(multipartResolver.isMultipart(request))
            {
                logger.info("multipartResolver.isMultipart(request)");
                //将request变成多部分request
                MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
                String contractId  = multiRequest.getParameter("contractId");
                String customerId = multiRequest.getParameter("customerId");
                String category = multiRequest.getParameter("category");
                String source   = multiRequest.getParameter("source");
                //获取multiRequest 中所有的文件名
                Iterator iter=multiRequest.getFileNames();
                while(iter.hasNext())
                {
                    logger.info("iter.hasNext()");
                    //一次遍历所有文件
                    MultipartFile file=multiRequest.getFile(iter.next().toString());
                    if(file!=null)
                    {
                        logger.info("file!=null");
                        String fileName = file.getOriginalFilename();
                        //开始上传图片
                        String path = documentService.uploadPcDocumentOk(file,fileName,contractId,customerId,category,source,token);
                        fileList.add(path);
                    }
                }
                resultDto.setData(fileList);
                resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
                resultDto.setResult(ResultResource.CODE_SUCCESS);
            }
        } catch (Exception e) {
            logger.error("上传图片失败", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException)e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  resultDto;
    }
    @ApiOperation(value="下载附件", notes="下载附件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/downloadDocument",method = RequestMethod.POST)
    @ResponseBody
    public void  downloadDocument(HttpServletResponse response,@RequestParam(value = "remotePath",required = true) String remotePath,
                                      @RequestParam(value = "fileName",required = true) String fileName){
        logger.info("start downloadDocument!");
        //校验参数
        if (remotePath == null || "".equals(remotePath)){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (fileName == null || "".equals(fileName)){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            documentService.downloadDocument(response,remotePath,fileName);
        }catch (Exception e) {
            logger.error("下载附件", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException)e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value="下载附件", notes="下载附件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/deleteDocument",method = RequestMethod.GET)
    @ResponseBody
    public CronusDto  deleteDocument(  @RequestParam(value = "remotePath",required = true) String remotePath,
                                  @RequestParam(value = "fileName",required = true) String fileName){
        logger.info("start deleteDocument!");
        CronusDto resultDto = new CronusDto();
        //校验参数
        if (remotePath == null || "".equals(remotePath)){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        if (fileName == null || "".equals(fileName)){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            resultDto  = documentService.deleteDocument(remotePath,fileName);
        }catch (Exception e) {
            logger.error("删除附件", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException)e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  resultDto;
    }
}
