package com.fjs.cronus.mappers;

import com.fjs.cronus.entity.CompanyMediaQueue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyMediaQueueMapper {

    List<CompanyMediaQueue> findByExample(CompanyMediaQueue companyMediaQueue);

    List<CompanyMediaQueue> findByCompanyId(@Param("companyid") Integer companyid, @Param("status") Integer status, @Param("yearmonth") String yearmonth);

    void addBatchCompanyMediaQueue(@Param("list")List<CompanyMediaQueue> list);

    void updateCompanyMediaQueue(@Param("valueParams")CompanyMediaQueue valueParams, @Param("whereParams")CompanyMediaQueue whereParams);
}
