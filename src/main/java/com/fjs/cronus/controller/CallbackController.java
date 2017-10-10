package com.fjs.cronus.controller;

import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.CallbackQueryDto;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.service.CallbackService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by msi on 2017/10/10.
 */
@RequestMapping("/api/v1")
@Controller
public class CallbackController {

    private  static  final Logger logger = LoggerFactory.getLogger(CallbackController.class);

    @Autowired
    CallbackService callbackService;

    @ApiOperation(value="回访客户列表", notes="回访客户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "callbackQueryDto", value = "参数", required = true, paramType = "body", dataType = "CallbackQueryDto")
    })
    @RequestMapping(value = "/callbackCustomerList", method = RequestMethod.GET)
    @ResponseBody
    public CronusDto callbackCustomerList(@RequestParam(value = "callback_user",required = false) String callback_user,
                                          @RequestParam(value = "callback_start_time",required = false) String callback_start_time,
                                          @RequestParam(value = "callback_end_time",required = false) String callback_end_time,
                                          @RequestParam(value = "search_name",required = false) String search_name,
                                          @RequestParam(value = "type",required = false) Integer type,
                                          @RequestParam(value = "search_city",required = false) String search_city,
                                          @RequestParam(value = "search_telephone",required = false) String search_telephone,
                                          @RequestParam(value = "search_callback_status",required = false) String search_callback_status,
                                          @RequestParam(value = "communication_order",required = false) Integer communication_order,
                                          @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                                          @RequestParam(value = "size",required = false,defaultValue = "20") Integer size){
        CronusDto cronusDto = new CronusDto();
        try {
            cronusDto  = callbackService.callbackCustomerList(callback_user,callback_start_time,callback_end_time,search_name,type,search_city,search_telephone,search_callback_status,page,size,communication_order);
            return cronusDto;
        } catch (Exception e) {
            logger.error("--------------->callbackCustomerList获取用户附件信息失败", e);
            if (e instanceof CronusException) {
                CronusException thorException = (CronusException)e;
                throw thorException;
            }
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR);
        }
    }

}
