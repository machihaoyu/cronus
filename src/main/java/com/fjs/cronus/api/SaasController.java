package com.fjs.cronus.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.dto.crm.ResponseData;
import com.fjs.cronus.dto.saas.*;
import com.fjs.cronus.service.SaasService;
import io.swagger.annotations.*;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import static com.fjs.cronus.exception.ExceptionValidate.validateResponse;

/**
 * Created by Administrator on 2017/7/12 0012.
 */
@RestController
@RequestMapping("/saas")
public class SaasController {

    public final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${saas.url}")
    private String saasUrl;

    @Value("${saas.key}")
    private String saasKey;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    SaasService saasService;

    @ApiOperation(value="Saas首页数据", notes="Saas首页数据接口API")
    @RequestMapping(value = "/apiwithout/v1/getSaasIndexData", method = RequestMethod.GET)
    @ResponseBody
    public SaasApiDTO getSaasIndexData() {
        try {
            LOGGER.warn("11111=");
            long l = System.currentTimeMillis();
            String url = saasUrl + "crmCountData?key=" + saasKey;
            String res = restTemplate.getForObject(url, String.class);
            LOGGER.warn("Saas首页数据返回：" + res);
            ResponseData data = JSONObject.parseObject(res, ResponseData.class);
            validateResponse(data);
            SaasIndexDTO saasIndexDTO = JSON.parseObject(data.getRetData(), SaasIndexDTO.class);
            LOGGER.warn("2222=" + (System.currentTimeMillis() - l));
            return new SaasApiDTO(0, null, saasIndexDTO);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return new SaasApiDTO(0, null, new SaasIndexDTO());
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
    public SaasApiDTO getCrmAchievement(@RequestParam String userId) {
        String url = saasUrl + "getMyData?key=" + saasKey + "&user_id=" + userId;
        LOGGER.warn("获取'我的'业绩: " + url);
        String res = restTemplate.getForObject(url, String.class);
        LOGGER.warn("获取'我的'业绩接口返回: " + res);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        MineAchievementDTO mineAchievementDTO = JSON.parseObject(data.getRetData(), MineAchievementDTO.class);
        return new SaasApiDTO(0, null, mineAchievementDTO);

    }

    /**
     * 获取我的排名接口
     */
    @ApiOperation(value="获取'我的'业绩排行", notes="获取'我的'业绩排行接口API")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer test8665aea5-04e3-4ebd-a7f3-b66442512762", dataType = "string"),
        @ApiImplicitParam(name = "achievementRankParamDTO", value = "type(1-团队,2-公司,3-城市,4-国家);page页码;perpage每页的个数;user_id用户编码;queryType(业绩=achieve，有效客户协议率=ValidCus_AgreemRate)",
                required = true, paramType = "body",  dataType = "AchievementRankParamDTO")
    })
    @RequestMapping(value = "/api/v1/getCrmAchievementRank", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public SaasApiDTO getCrmAchievementRank(@RequestBody AchievementRankParamDTO achievementRankParamDTO) {
        achievementRankParamDTO.setPerpage("12");
        LOGGER.warn("我的排名参数: " + ReflectionToStringBuilder.toString(achievementRankParamDTO));
        AchievementRankPageDTO dto = saasService.getCrmRank(achievementRankParamDTO, saasUrl, saasKey);
        if (null != dto) {
            if (dto.getSelf() == null) dto.setSelf(new AchievementRankDTO());
        } else {
            dto = new AchievementRankPageDTO();
            dto.setSelf(new AchievementRankDTO());
        }
        return new SaasApiDTO(0, null, dto);
    }



}
