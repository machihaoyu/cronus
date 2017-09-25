package com.fjs.cronus.controller;

import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.OcrInfoService;
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

/**
 * Created by msi on 2017/9/25.
 */
@Controller
@RequestMapping("/api/v1")
public class OcrInfoControlller {

    private  static  final Logger logger = LoggerFactory.getLogger(OcrInfoControlller.class);

    @Autowired
    OcrInfoService ocrInfoService;

    @ApiOperation(value="获取附件列表", notes="获取附件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "create_user_id", value = "create_user_id", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "customer_telephone", value = "187XXXXXXX", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "customer_name", value = "XXXX", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "草稿", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "order", value = "排序方式", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "ocr_type", value = "1 身份证,2 户口薄,3 驾驶证,4 行驶证,5 房产证", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "第几页", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示多少", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/getOcrInfoList", method = RequestMethod.GET)
    @ResponseBody
    public QueryResult getOcrInfoList(@RequestParam(value = "create_user_id",required = false) Integer create_user_id,
                                      @RequestParam(value = "customer_telephone",required = false) String customer_telephone,
                                      @RequestParam(value = "customer_name",required = false) String customer_name,
                                      @RequestParam(value = "status",required = false) String status,
                                      @RequestParam(value = "order",required = false) String order,
                                      @RequestParam(value = "ocr_type",required = true) Integer ocr_type,
                                      @RequestParam(value = "page",required = true) Integer page,
                                      @RequestParam(value = "size",required = true) Integer size){
        QueryResult resultDto = new QueryResult();
        try{
            resultDto  = ocrInfoService.getOcrInfoList(create_user_id,customer_telephone,customer_name,status,ocr_type,page,size,order);

        }catch (Exception e){
            logger.error("--------------->getOcrInfoList 获取信息出错", e);
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  resultDto;
    }

    @ApiOperation(value="编辑OCR信息", notes="编辑OCR信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerid", value = "1", required = true, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/editOcrInfo", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto editOcrInfo(@RequestParam(value = "id",required = true) Integer id,
                                 @RequestParam(value = "ocr_type",required = true) Integer ocr_type) {
        CronusDto cronusDto = new CronusDto();
        try {
            //   String customerids = jsonObject.getString("customerids");
            if (id == null || "".equals(id)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
            }
            if (ocr_type == null || "".equals(ocr_type)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
            }
            cronusDto = ocrInfoService.editOcrInfo(id,ocr_type);
            return cronusDto;
        } catch (Exception e) {
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            logger.error("--------------->editOcrInfo 编辑OCR信息", e);
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }



}
