package com.fjs.cronus.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.dto.crm.ResponseData;
import com.fjs.cronus.dto.saas.MineAchievementDTO;
import com.fjs.cronus.dto.saas.SaasIndexDTO;
import com.fjs.cronus.dto.uc.BaseUcDTO;
import com.fjs.cronus.exception.ExceptionValidate;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Administrator on 2017/7/12 0012.
 */
@RestController
@RequestMapping("/saas")
public class SaasController {

    public final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

//    @Value("${sale.url}")
    private String saleUrl = "http://beta-sale.fang-crm.com/Api/Api/";

//    @Value("${saas.key}")
    private String saasKey = "67gXBstOju5LX7WOxSaQnjZguNs2citrJ99ZSJl8";

    @Autowired
    RestTemplate restTemplate;

    @ApiOperation(value="Saas首页数据", notes="Saas首页数据接口API")
    @RequestMapping(value = "/api/v1/getSaasIndexData", method = RequestMethod.GET)
    @ResponseBody
    public BaseUcDTO getSaasIndexData() {
        try {
            String url = saleUrl + "crmCountData?key=" + saasKey;
            String res = restTemplate.getForObject(url, String.class);
            ResponseData data = JSONObject.parseObject(res, ResponseData.class);
            ExceptionValidate.validateResponse(data);
            SaasIndexDTO saasIndexDTO = JSON.parseObject(data.getRetData(), SaasIndexDTO.class);
            return new BaseUcDTO(0, null, saasIndexDTO);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new BaseUcDTO(8000, e.getMessage(), null);
        }
    }

    /**
     * 获取'我的'业绩接口
     */
    @ApiOperation(value="获取'我的'业绩", notes="获取'我的'业绩接口API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer test8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "query",  dataType = "int")
    })
    @RequestMapping(value = "/api/v1/getCrmAchievement", method = RequestMethod.GET)
    @ResponseBody
    public BaseUcDTO getCrmAchievement(@RequestParam String userId) {
        try {
            String url = saleUrl + "getMyData?key=" + saasKey + "&user_id=" + userId;
            String res = restTemplate.getForObject(url, String.class);
            ResponseData data = JSONObject.parseObject(res, ResponseData.class);
            ExceptionValidate.validateResponse(data);
            MineAchievementDTO mineAchievementDTO = JSON.parseObject(data.getRetData(), MineAchievementDTO.class);
            return new BaseUcDTO(0, null, mineAchievementDTO);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new BaseUcDTO(8001, e.getMessage(), null);
        }
    }



}
