package com.fjs.cronus.service;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.api.thea.Config;
import com.fjs.cronus.dto.api.crius.CriusApiDTO;
import com.fjs.cronus.service.client.TheaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;

/**
 * Created by yinzf on 2017/11/21.
 */
@Service
public class CleanMangerService {
    @Autowired
    private TheaService theaService;
//    private ConfigService configService;

    public Integer add(Integer subCompanyId, List<String> strCompany, Config companyConfig, String token){
        //获取屏蔽的公司
        String value = companyConfig.getConValue();
        JSONObject jsonObject = new JSONObject();
        if (!CollectionUtils.isEmpty(strCompany)){
            int size = strCompany.size();
            for (int i=0;i < size; i ++) {
                jsonObject.put((i+1) +"",strCompany.get(i).toString());
            }
        }
        String str = jsonObject.toString();
        Integer a=0;
        companyConfig.setConValue(str);

        CriusApiDTO criusApiDTO = theaService.updatebConfig(token, companyConfig);
        if (criusApiDTO.getResult() == 0){
            a = 1;
        }
        return a;
    }

    public Integer addEmp(Config companyConfig, String token){
        //获取屏蔽的公司
        Integer a=0;
        CriusApiDTO criusApiDTO = theaService.updatebConfig(token, companyConfig);
        if (criusApiDTO.getResult() == 0){
            a = 1;
        }
        return a;
    }
}

