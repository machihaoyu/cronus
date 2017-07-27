package com.fjs.cronus.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.dto.crm.ResponseData;
import com.fjs.cronus.dto.saas.*;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

//    @Value("${sale.url}")
    private String saleUrl = "http://beta-sale.fang-crm.com/Api/Api/";

//    @Value("${saas.key}")
    private String saasKey = "67gXBstOju5LX7WOxSaQnjZguNs2citrJ99ZSJl8";

    @Autowired
    RestTemplate restTemplate;

    @ApiOperation(value="Saas首页数据", notes="Saas首页数据接口API")
    @RequestMapping(value = "/apiwithout/v1/getSaasIndexData", method = RequestMethod.GET)
    @ResponseBody
    public SaasApiDTO getSaasIndexData() {
        String url = saleUrl + "crmCountData?key=" + saasKey;
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSONObject.parseObject(res, ResponseData.class);
        validateResponse(data);
        SaasIndexDTO saasIndexDTO = JSON.parseObject(data.getRetData(), SaasIndexDTO.class);
        return new SaasApiDTO(0, null, saasIndexDTO);
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
        String url = saleUrl + "getMyData?key=" + saasKey + "&user_id=" + userId;
        String res = restTemplate.getForObject(url, String.class);
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
        @ApiImplicitParam(name = "achievementRankParamDTO", value = "type(1-团队,2-公司,3-城市,4-国家);page页码;perpage每页的个数;user_id用户编码", required = true, paramType = "body",  dataType = "AchievementRankParamDTO")
    })
    @RequestMapping(value = "/api/v1/getCrmAchievementRank", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public SaasApiDTO getCrmAchievementRank(@RequestBody AchievementRankParamDTO achievementRankParamDTO) {
        String url = saleUrl + "getAchievementRanking";
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("key",saasKey);
        param.add("type",achievementRankParamDTO.getType());
        param.add("page", achievementRankParamDTO.getPage());
        param.add("perpage", achievementRankParamDTO.getPerpage());
        param.add("user_id", achievementRankParamDTO.getUserId());

        String str = restTemplate.postForObject(url, param, String.class);
        ResponseData data = JSON.parseObject(str, ResponseData.class);
        validateResponse(data);
        AchievementRankPageDTO dto = JSONObject.parseObject(data.getRetData(), AchievementRankPageDTO.class);
        if (null == dto) dto = new AchievementRankPageDTO();
        return new SaasApiDTO(0, null, dto);
    }



}
