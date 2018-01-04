package com.fjs.cronus.controller.app;

import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.ResultResource;
import com.fjs.cronus.controller.AllocateController;
import com.fjs.cronus.dto.App.ReceiveAndKeepCountDTO;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.dto.cronus.OcrDocumentDto;
import com.fjs.cronus.dto.cronus.RemoveDTO;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.App.AppService;
import com.fjs.cronus.service.DocumentCategoryService;
import com.fjs.cronus.service.RContractDocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by msi on 2017/12/28.
 */

@Controller
@Api(description = "App端接口控制器")
@RequestMapping("/api/v1")
public class AppController {

    @Autowired
    AppService appService;
    private  static  final Logger logger = LoggerFactory.getLogger(AppController.class);
    @Autowired
    DocumentCategoryService documentCategoryService;
    @ApiOperation(value="App获取业务员当天的沟通数与领取数", notes="App获取业务员当天的沟通数与领取数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string")})

    @RequestMapping(value = "/getReceiveAndKeepCount", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<ReceiveAndKeepCountDTO> getReceiveAndKeepCount(@RequestHeader("Authorization")String token){

        CronusDto<ReceiveAndKeepCountDTO> cronusDto = new CronusDto();
        //校验权限
        Integer userId = Integer.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userId == null){
            throw new CronusException(CronusException.Type.THEA_SYSTEM_ERROR);
        }
        try {
            cronusDto  = appService.getReceiveAndKeepCount(userId);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->getReceiveAndKeepCount失败",e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }

    }

    @ApiOperation(value="根据客户id查找附件信息", notes="根据客户id查找附件信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "customerId", value = "客户id", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "page", required = false, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "size", required = false, paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/findClientDoc", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto<QueryResult<OcrDocumentDto>> findClientDoc(@RequestParam(value = "customerId",required = true) Integer customerId,
                                                         @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                                         @RequestParam(value = "size",required = false,defaultValue = "10") Integer size){
        CronusDto<QueryResult<OcrDocumentDto>> cronusDto = new CronusDto();
        QueryResult<OcrDocumentDto> queryResult = new QueryResult<>();
        try {
            if (customerId == null || "".equals(customerId)) {
                throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR);
            }
            queryResult = appService.findDocByCustomerId(customerId,page,size);
            cronusDto.setData(queryResult);
            cronusDto.setResult(ResultResource.CODE_SUCCESS);
            cronusDto.setMessage(ResultResource.MESSAGE_SUCCESS);
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

    @ApiOperation(value="App获取附件三价联动的信息", notes="App获取附件三价联动的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
    })
    @RequestMapping(value = "/getClientCatory", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto getClientCatory(HttpServletRequest request){
        CronusDto cronusDto = new CronusDto();
        try {
            cronusDto = documentCategoryService.getThreeCategory();
        }catch (Exception e){
            logger.error("--------------->getNextCategory 获取附件三价联动的信息", e);
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
        return  cronusDto;
    }

}
