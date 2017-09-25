package com.fjs.cronus.controller;

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
    public CronusDto uploadDocumentOk(@RequestHeader("Authorization") String token, @RequestBody List<UploadDocumentDto> uploadDocumentDto ){

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

}
