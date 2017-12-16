package com.fjs.cronus.controller;

import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.OcrDocumentDto;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.RContractDocumentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by msi on 2017/10/10.
 */

@RequestMapping("/api/v1")
@Controller
public class RContractDocumentController {

    private  static  final Logger logger = LoggerFactory.getLogger(RContractDocumentController.class);


    @Autowired
    RContractDocumentService rContractDocumentService;
    @ApiOperation(value="根据客户id查找附件信息", notes="根据客户id查找附件信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/findDocByCustomerId", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<List<OcrDocumentDto>> findDocByCustomerId(@RequestParam Integer customerId ){
        CronusDto<List<OcrDocumentDto>> cronusDto = new CronusDto();
        try {
            if (customerId == null || "".equals(customerId)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
            }
            cronusDto = rContractDocumentService.findDocByCustomerId(customerId);

            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->findDocByCustomerId获取用户附件信息失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value="校验客户是否上传了身份证或者房产证", notes="校验客户是否上传了身份证或者房产证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/checkCustomer", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto checkCustomer(@RequestParam Integer customerId ){
        CronusDto cronusDto = new CronusDto();
        try {
            if (customerId == null || "".equals(customerId)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
            }
            cronusDto = rContractDocumentService.checkCustomerIsUpload(customerId);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->checkCustomerIsUpload获取用户附件信息失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

    @ApiOperation(value="确认C端附件状态", notes="确认C端附件状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "document_id", value = "文件id", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/confirmDocument",method = RequestMethod.GET)
    @ResponseBody
    public CronusDto  confirmDocument(@RequestParam(value = "document_id",required = true) Integer document_id){
        logger.info("start deleteDocument!");
        CronusDto resultDto = new CronusDto();
        //校验参数
        if (document_id == null || "".equals(document_id)){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
        }
        try {
            boolean result  = rContractDocumentService.confirmDocument(document_id);
            resultDto.setData(result);
            resultDto.setResult(ResultResource.CODE_SUCCESS);
            resultDto.setMessage(ResultResource.MESSAGE_SUCCESS);
        }catch (Exception e) {
            logger.error("确认C端附件状态", e);
            if (e instanceof CronusException) {
                CronusException cronusException = (CronusException)e;
                throw cronusException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  resultDto;
    }
}
