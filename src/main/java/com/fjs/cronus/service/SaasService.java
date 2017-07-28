package com.fjs.cronus.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.dto.crm.ResponseData;
import com.fjs.cronus.dto.saas.AchievementRankPageDTO;
import com.fjs.cronus.dto.saas.AchievementRankParamDTO;
import com.fjs.cronus.exception.CronusException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.fjs.cronus.exception.ExceptionValidate.validateResponse;

/**
 * Created by Administrator on 2017/7/28 0028.
 */
@Service
public class SaasService {

    @Autowired
    RestTemplate restTemplate;

    public AchievementRankPageDTO getCrmRank(AchievementRankParamDTO achievementRankParamDTO, String saleUrl, String saasKey) {
        if (StringUtils.isNotEmpty(achievementRankParamDTO.getQueryType())) {
            AchievementRankPageDTO dto = new AchievementRankPageDTO();
            if (AchievementRankParamDTO.QUERY_TYPE_ACHIEVE.equals(achievementRankParamDTO.getQueryType())) {
                String url = saleUrl + "getAchievementRanking?key=" + saasKey + "&type=" + achievementRankParamDTO.getType() + "&page=" + achievementRankParamDTO.getPage() +
                        "&perpage=" + achievementRankParamDTO.getPerpage() + "&user_id=" + achievementRankParamDTO.getUserId();
                dto = getResult(url);
            } else if (AchievementRankParamDTO.QUERY_TYPE_VALIDCUS_AGREEMRATE.equals(achievementRankParamDTO.getQueryType())) {
                String url = saleUrl + "getUsefulAgreementRanking?key=" + saasKey + "&type=" + achievementRankParamDTO.getType() + "&page=" + achievementRankParamDTO.getPage() +
                        "&perpage=" + achievementRankParamDTO.getPerpage() + "&user_id=" + achievementRankParamDTO.getUserId();
                dto = getResult(url);
            }
            return dto;
        } else {
            throw new CronusException(CronusException.Type.SAAS_CRM_EXCEPTION, "查询排名类型不能为空!");
        }
    }

    private AchievementRankPageDTO getResult(String url) {
        String res = restTemplate.getForObject(url, String.class);
        ResponseData data = JSON.parseObject(res, ResponseData.class);
        validateResponse(data);
        AchievementRankPageDTO dto = JSONObject.parseObject(data.getRetData(), AchievementRankPageDTO.class);
        return dto;
    }

}
