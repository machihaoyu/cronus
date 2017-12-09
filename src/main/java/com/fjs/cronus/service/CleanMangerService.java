//package com.fjs.cronus.service;
//
//import com.alibaba.fastjson.JSONObject;
//import com.fjs.cronus.api.thea.Config;
//import com.fjs.cronus.dto.api.crius.CriusApiDTO;
//import com.fjs.cronus.service.client.TheaService;
//import com.fjs.thea.api.UserInfoDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//
///**
// * Created by yinzf on 2017/11/21.
// */
//@Service
//public class CleanMangerService {
//    @Autowired
//    private TheaService theaService;
////    private ConfigService configService;
//
//    public Integer add(Integer subCompanyId, List<String> strCompany, Config companyConfig, String token){
//        //获取屏蔽的公司
//        String value = companyConfig.getValue();
//        JSONObject jsonObject = new JSONObject();
//        if (!CollectionUtils.isEmpty(strCompany)){
//            int size = strCompany.size();
//            for (int i=0;i < size; i ++) {
//                jsonObject.put(i +"",strCompany.get(i).toString());
//            }
//        }
//        String str = jsonObject.toString();
//        Integer a=0;
//        companyConfig.setValue(str);
//
////        CriusApiDTO criusApiDTO = theaService.ModifyValueById(token,,);
//
//        a = configService.update(companyConfig,userInfoDTO);
//        return a;
//    }
//}
