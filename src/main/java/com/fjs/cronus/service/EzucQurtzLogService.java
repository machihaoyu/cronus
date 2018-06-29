package com.fjs.cronus.service;

import com.fjs.cronus.entity.EzucQurtzLog;
import com.fjs.cronus.mappers.EzucQurtzLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EzucQurtzLogService {

    @Autowired
    private EzucQurtzLogMapper ezucQurtzLogMapper;

    public List<EzucQurtzLog> findEzucSyncLog(EzucQurtzLog params, Integer pageNum, Integer pageSize) {

        if (pageNum == null || pageNum <= 0) pageNum = 0;
        if (pageSize == null || pageSize <= 0) pageSize = 10;
        params = params == null ? new EzucQurtzLog() : params;

        return ezucQurtzLogMapper.findEzucSyncLog(params, pageNum*pageSize, pageSize);
    }
}
