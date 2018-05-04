package com.fjs.cronus.mappers;

import com.fjs.cronus.entity.CompanyMediaQueue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyMediaQueueMapper {

    List<CompanyMediaQueue> findByCompanyId(@Param("companyid") Integer companyId, @Param("status") Integer status);

    void addBatchCompanyMediaQueue(@Param("list")List<CompanyMediaQueue> list);

    void updateCompanyMediaQueue(@Param("valueParams")CompanyMediaQueue valueParams, @Param("whereParams")CompanyMediaQueue whereParams);
}
