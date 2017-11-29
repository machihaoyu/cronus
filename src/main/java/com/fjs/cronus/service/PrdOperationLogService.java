package com.fjs.cronus.service;

import com.fjs.cronus.mappers.PrdOperationLogMapper;
import com.fjs.cronus.model.PrdOperationLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yinzf on 2017/11/1.
 */
@Service
public class PrdOperationLogService {
    @Autowired
    private PrdOperationLogMapper prdOperationLogMapper;


    public  Integer addPrdOperationLog(PrdOperationLog prdOperationLog){
        return prdOperationLogMapper.insert(prdOperationLog);
    }
}
