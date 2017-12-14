package com.fjs.cronus.service;

import com.fjs.cronus.dto.AutoCleanManageDTO;
import com.fjs.cronus.dto.AutoCleanManageDTO2;
import com.fjs.cronus.dto.api.SimpleUserInfoDTO;
import com.fjs.cronus.dto.cronus.UcUserDTO;
import com.fjs.cronus.mappers.AutoCleanManageMapper;
import com.fjs.cronus.model.AutoCleanManage;
import com.fjs.cronus.model.CustomerMeet;
import com.fjs.cronus.service.client.ThorInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2017/10/12.
 */
@Service
public class AutoCleanManageService {

    @Autowired
    private AutoCleanManageMapper autoCleanManageMapper;
    @Autowired
    private ThorInterfaceService thorUcService;

    /**
     * 获取集合
     * @param map
     * @return
     */
    public List<AutoCleanManage> selectByParamsMap(Map<String, Object> map){
        return autoCleanManageMapper.selectByParamsMap(map);
    }

    public Integer add(AutoCleanManage autoCleanManage,Integer userId){
        Date date=new Date();
        autoCleanManage.setUserId(userId);
        autoCleanManage.setCreateUser(userId);
        autoCleanManage.setCreateTime(date);
        autoCleanManage.setLastUpdateUser(userId);
        autoCleanManage.setLastUpdateTime(date);
        autoCleanManage.setIsDeleted(1);
        return autoCleanManageMapper.insert(autoCleanManage);
    }

    public AutoCleanManage copyProperty(AutoCleanManageDTO autoCleanManageDTO){
        AutoCleanManage autoCleanManage=new AutoCleanManage();
        autoCleanManage.setUtmSource(autoCleanManageDTO.getUtmSource());
        autoCleanManage.setCustomerSource(autoCleanManageDTO.getCustomerSource());
        return autoCleanManage;
    }

    /**
     * 屏蔽列表
     * @param token
     * @return
     */
    public List<AutoCleanManageDTO2> getList(String token){
        List<AutoCleanManageDTO2> dtoList = new ArrayList<>();
        List<AutoCleanManage> autoCleanManageList = new ArrayList<>();
        Example example=new Example(AutoCleanManage.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("isDeleted",1);
        criteria.andEqualTo("utmSource","");
        criteria.andEqualTo("customerSource","");
        autoCleanManageList = autoCleanManageMapper.selectByExample(example);
        for (AutoCleanManage autoCleanManage:autoCleanManageList){
            AutoCleanManageDTO2 autoCleanManageDTO = new AutoCleanManageDTO2();
            autoCleanManageDTO.setId(autoCleanManage.getId());
            autoCleanManageDTO.setUserId(autoCleanManage.getUserId());
            //获取业务员信息
            SimpleUserInfoDTO simpleUserInfoDTO = thorUcService.getUserInfoById(token,autoCleanManage.getUserId()).getData();
            if (simpleUserInfoDTO == null){
                return null;
            }
            autoCleanManageDTO.setOwnerUserName(simpleUserInfoDTO.getName());
            autoCleanManageDTO.setCreateTime(autoCleanManage.getCreateTime());
            autoCleanManageDTO.setTelephone(simpleUserInfoDTO.getTelephone());
            dtoList.add(autoCleanManageDTO);
        }
        return dtoList;
    }

    /**
     * 展开列表
     * @param userId
     * @return
     */
    public List<AutoCleanManageDTO> getByUserId(Integer userId){
        List<AutoCleanManageDTO> dtoList = new ArrayList<>();
        List<AutoCleanManage> autoCleanManageList = new ArrayList<>();
        Example example=new Example(AutoCleanManage.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("isDeleted",1);
        criteria.andEqualTo("userId",userId);
        criteria.andNotEqualTo("utmSource","");
        criteria.andNotEqualTo("customerSource","");
        autoCleanManageList = autoCleanManageMapper.selectByExample(example);
        for (AutoCleanManage autoCleanManage:autoCleanManageList){
            AutoCleanManageDTO autoCleanManageDTO = new AutoCleanManageDTO();
            autoCleanManageDTO.setId(autoCleanManage.getId());
            autoCleanManageDTO.setCustomerSource(autoCleanManage.getCustomerSource());
            autoCleanManageDTO.setUtmSource(autoCleanManage.getUtmSource());
            autoCleanManageDTO.setCreateTime(autoCleanManage.getCreateTime());
            dtoList.add(autoCleanManageDTO);
        }
        return dtoList;
    }

    public Integer deleteById(Integer id,Integer userId){
        AutoCleanManage autoCleanManage = new AutoCleanManage();
        autoCleanManage.setId(id);
        autoCleanManage = autoCleanManageMapper.selectOne(autoCleanManage);
        Date date= new Date();
        autoCleanManage.setIsDeleted(2);
        autoCleanManage.setLastUpdateUser(userId);
        autoCleanManage.setLastUpdateTime(date);
        return autoCleanManageMapper.update(autoCleanManage);
    }
}
